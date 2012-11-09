; Patient data server access function

(ns osim2pds.pds
  (:gen-class)
  (:use [monger.core :only [connect! set-db! get-db]]
        [monger.collection :only [insert]])
  (:import [org.bson.types ObjectId]
           [com.mongodb DB WriteConcern]))

;; localhost, default port
(defn initialize
  "Setup Mongo connection"
  []
  (connect!)
  (set-db! (get-db "monger-test")))

(defn mongoidize
  "Add ObjectId and type to medication or condition hash"
  [item type]
  (assoc item "_id" (ObjectId.) "_type" type))

(defn save
  "Save a patient"
  [id forename surname gender dob address meds conditions]
  (insert "documents" { 
    :_id (ObjectId.)
    :medical_record_number (.toString id)
    :first forename 
    :last surname 
    :dob dob 
    :gender gender
    :address address
    :expired false
    :medications (map mongoidize meds (repeatedly (fn [] "Medication")))
    :conditions (map mongoidize conditions (repeatedly (fn [] "Condition")))}))

