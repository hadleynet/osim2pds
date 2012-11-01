(ns osim2pds.osim
  (:gen-class)
  (:use korma.db korma.core)
)

(defdb prod ( oracle {
  :subname "@//stagevpc27.mitre.org:1521/ora1"
  :user "osim_cdm"
  :password "osim_cdm"}
))
  
(defentity person_entity
  (pk :PERSON_ID)
  (table :PERSON)
)  

(defentity medication_entity
  (pk :DRUG_ERA_ID)
  (table :DRUG_ERA)
  (belongs-to person_entity {:fk :PERSON_ID})
)

(defentity condition_entity
  (pk :CONDITION_ERA_ID)
  (table :CONDITION_ERA)
  (belongs-to person_entity {:fk :PERSON_ID})
)

(defn person
  "Lookup a patient by id"
  [id]
  (select person_entity
    (where {:PERSON_ID id})
  )
)

(defn medications
  "Lookup a patients medications by patient_id"
  [id]
  (select medication_entity
    (where {:PERSON_ID id})
  )
)

(defn conditions
  "Lookup a patients conditions by patient_id"
  [id]
  (select condition_entity
    (where {:PERSON_ID id})
  )
)
