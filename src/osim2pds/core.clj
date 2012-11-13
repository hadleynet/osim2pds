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

(defn med-map
  "Convert OSIM patient medication data to PDS format"
  [osim-med]
  {
    :start_time (translate-date (osim-med :DRUG_ERA_START_DATE))
    :end_time (translate-date (osim-med :DRUG_ERA_END_DATE))
    :description (osim/concept-name (osim-med :DRUG_CONCEPT_ID))
    :mood_code "EVN"
    :codes (extract-codes (osim-med :DRUG_CONCEPT_ID))
    :status_code {"HL7 ActStatus" ["active"] "SNOMED-CT" ["55561003"]}
  })

(defn medications
  "Get the patient medications in PDS format"
  [id]
  (map med-map (osim/medications id)))

(defn condition-map
  "Convert OSIM patient condition data to PDS format"
  [osim-condition]
  {
    :start_time (translate-date (osim-condition :CONDITION_ERA_START_DATE))
    :end_time (translate-date (osim-condition :CONDITION_ERA_END_DATE))
    :description (osim/concept-name (osim-condition :CONDITION_CONCEPT_ID))
    :mood_code "EVN"
    :codes (extract-codes (osim-condition :CONDITION_CONCEPT_ID))
    :status_code {"HL7 ActStatus" ["active"] "SNOMED-CT" ["55561003"]}
  })

(defn conditions
  "Get the patient conditions in PDS format"
  [id]
  (map condition-map (osim/conditions id)))

(defn -main
  "Migrate OSIM2 patient data to Patient Data Server."
  [& args]
 
  (pds/initialize)
  ; for each patient - relies on OSIM patient id being monotonically increasing integers
  (doseq [id (range 1 11)]
    (let [patient (first (osim/person id))]
      (if patient
        (do 
          (let [
            id (patient :PERSON_ID)
            gender (osim/gender_code (patient :GENDER_CONCEPT_ID))
            forename (rand/forename gender)
            surname (rand/surname)
            address (rand/address)
            yob (patient :YEAR_OF_BIRTH) 
            dob (java.util.GregorianCalendar. yob (rand-int 12) (+ 1 (rand-int 28)))
            ts (quot (.getTimeInMillis dob) 1000)
            meds (medications (patient :PERSON_ID))
            conditions (conditions (patient :PERSON_ID))
          ]
            (pds/save id forename surname gender ts address meds conditions)
            (print ".")
          )
        )
      )
    )
  )
  (print "\n"))
