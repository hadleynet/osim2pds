; Patient data server access function

(ns osim2pds.pds
  (:gen-class)
  (:use [monger.core :only [connect! set-db! get-db]]
        [monger.collection :only [insert]])
  (:import [org.bson.types ObjectId]
           [java.util Date]
           [com.mongodb DB WriteConcern]))

;; localhost, default port
(defn initialize
  "Setup Mongo connection"
  []
  (connect!)
  (set-db! (get-db "synthea")))

(defn mongoidize
  "Add ObjectId and type to medication or condition hash"
  [item type]
  (assoc item "_id" (ObjectId.) "_type" type))

(defn save
  "Save a patient"
  [id {:keys [forename surname gender address]} dob meds conditions]
  (insert "records" { 
    :_id (ObjectId.)
    :created_at (Date.)
    :updated_at (Date.)
    :medical_record_number (.toString id)
    :first forename 
    :last surname 
    :birthdate dob 
    :gender gender
    :addresses [address]
    :expired false
    :medications (map mongoidize meds (repeatedly (fn [] "Medication")))
    :conditions (map mongoidize conditions (repeatedly (fn [] "Condition")))}))

