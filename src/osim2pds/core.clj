; Map OSIM patient records to patient data server

(ns osim2pds.core
  (:gen-class)
  (:require [osim2pds.osim :as osim] [osim2pds.pds :as pds] [osim2pds.rand :as rand]))

(defn translate-date
  "Convert a timestamp from OSIM format to seconds since epoch"
  [ts]
  (quot (.getTime ts) 1000))

(defn extract-codes
  "Extract the standard codes for the supplied OSIM drug concept"
  [id]
  (let [codes (osim/codes id)]
    (if (> (count codes) 0)
      (apply merge-with concat codes)
      {})
  ))
  
(defn has-no-codes
  "Returns true if the supplied entry has zero codes, false otherwise"
  [entry]
  (= 0 (count (:codes entry))))

(defn med-map
  "Convert OSIM patient medication data to PDS format"
  [{:keys [DRUG_ERA_START_DATE DRUG_ERA_END_DATE DRUG_CONCEPT_ID]}]
  {
    :start_time (translate-date DRUG_ERA_START_DATE)
    :end_time (translate-date DRUG_ERA_END_DATE)
    :description (osim/concept-name DRUG_CONCEPT_ID)
    :mood_code "EVN"
    :codes (extract-codes DRUG_CONCEPT_ID)
    :status_code {"HL7 ActStatus" ["active"] "SNOMED-CT" ["55561003"]}
  })

(defn medications
  "Get the patient medications in PDS format"
  [id]
  (remove has-no-codes (map med-map (osim/medications id))))

(defn condition-map
  "Convert OSIM patient condition data to PDS format"
  [{:keys [CONDITION_ERA_START_DATE CONDITION_ERA_END_DATE CONDITION_CONCEPT_ID]}]
  {
    :start_time (translate-date CONDITION_ERA_START_DATE)
    :end_time (translate-date CONDITION_ERA_END_DATE)
    :description (osim/concept-name CONDITION_CONCEPT_ID)
    :mood_code "EVN"
    :codes (extract-codes CONDITION_CONCEPT_ID)
    :status_code {"HL7 ActStatus" ["active"] "SNOMED-CT" ["55561003"]}
  })

(defn conditions
  "Get the patient conditions in PDS format"
  [id]
  (remove has-no-codes (map condition-map (osim/conditions id))))
  
(defn yob2dob
  [yob]
  (-> yob
      (java.util.GregorianCalendar. (rand-int 12) (+ 1 (rand-int 28)))
      (.getTimeInMillis)
      (quot 1000)))

(defn -main
  "Migrate OSIM2 patient data to Patient Data Server."
  [& args]
 
  (pds/initialize)
  ; for each patient - relies on OSIM patient id being monotonically increasing integers
  (doseq [id (range 1 1000)]
    (if-let [patient (first (osim/person id))]
      (let [{:keys [PERSON_ID GENDER_CONCEPT_ID YEAR_OF_BIRTH]} patient
            fake-id (rand/fake-identity (osim/gender_code GENDER_CONCEPT_ID))
            dob (yob2dob YEAR_OF_BIRTH)
            meds (medications PERSON_ID)
            conditions (conditions PERSON_ID)]
        (pds/save PERSON_ID fake-id dob meds conditions)
        (print ".")
        (flush)
      )
    )
  )
  (print "\n"))
