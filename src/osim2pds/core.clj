(ns osim2pds.core
  (:gen-class))

(use 'korma.db)
(use 'korma.core)

(defdb prod ( oracle {
  :subname "@//stagevpc27.mitre.org:1521/ora1"
  :user "osim_cdm"
  :password "osim_cdm"}
))
  
(defentity person
  (pk :PERSON_ID)
  (table :PERSON)
)  

(defentity medication
  (pk :DRUG_ERA_ID)
  (table :DRUG_ERA)
  (belongs-to person {:fk :PERSON_ID})
)

(defentity condition
  (pk :CONDITION_ERA_ID)
  (table :CONDITION_ERA)
  (belongs-to person {:fk :PERSON_ID})
)

(defn osim_person
  "Lookup a patient by id"
  [id]
  (select person
    (where {:PERSON_ID id})
  )
)

(defn osim_person_medications
  "Lookup a patients medications by patient_id"
  [id]
  (select medication
    (where {:PERSON_ID id})
  )
)

(defn osim_person_conditions
  "Lookup a patients conditions by patient_id"
  [id]
  (select condition
    (where {:PERSON_ID id})
  )
)

(defn -main
  "Migrate OSIM2 patient data to Patient Data Server."
  [& args]
 
  (doseq [id (range 10)]
    (let [patient (first (osim_person id))]
      (if patient
        (do 
          (def yob (patient :YEAR_OF_BIRTH))
          (def gender (patient :GENDER_CONCEPT_ID))
          (println (format "%s - Birth Year: %s, Gender: %s" id yob gender))
        )
      )
    )
  )
)
