; OSIM database access functions

(ns osim2pds.osim
  (:gen-class)
  (:use korma.db korma.core))

(defdb prod ( oracle {
  :subname "@//stagevpc27.mitre.org:1521/ora1"
  :user "osim_cdm"
  :password "osim_cdm"}))
  
(defentity person_entity
  (pk :PERSON_ID)
  (table :PERSON))  

(defentity medication_entity
  (pk :DRUG_ERA_ID)
  (table :DRUG_ERA)
  (belongs-to person_entity {:fk :PERSON_ID}))

(defentity condition_entity
  (pk :CONDITION_ERA_ID)
  (table :CONDITION_ERA)
  (belongs-to person_entity {:fk :PERSON_ID}))

(defentity concept_entity
  (pk :CONCEPT_ID)
  (table :CONCEPT))

(defn person
  "Lookup a patient by id"
  [id]
  (select person_entity
    (where {:PERSON_ID id})
  ))

(defn medications
  "Lookup a patients medications by patient_id"
  [id]
  (select medication_entity
    (where {:PERSON_ID id})))

(defn conditions
  "Lookup a patients conditions by patient_id"
  [id]
  (select condition_entity
    (where {:PERSON_ID id})))

(defn concept-name
  "Return the human readable name of the supplied concept id"
  [id]
  (let [concept (first (select concept_entity (where {:CONCEPT_ID id})))]
    (if concept
      (concept :CONCEPT_NAME)
      "Unknown")))

(defn codes
  "Return the codes for a supplied concept id as a map of code-set-name code-list, e.g. ({\"RxNorm\" [\"foo\"]} {\"RxNorm\" [\"bar\"]} {\"SNOMED-CT\" [\"yyz\"]})"
  [id]
  (map (fn [entry] {(entry :CODE_SET) [(entry :CODE)]}) 
    (exec-raw ["SELECT map.SOURCE_CODE as CODE, vocab.VOCABULARY_NAME as CODE_SET FROM SOURCE_TO_CONCEPT_MAP map, VOCABULARY vocab WHERE map.SOURCE_VOCABULARY_ID = vocab.VOCABULARY_ID AND TARGET_CONCEPT_ID = ?" [id]] :results)))

(defn gender_code [gender_concept_id]
  ({8507M "M" 8532M "F"} gender_concept_id))
