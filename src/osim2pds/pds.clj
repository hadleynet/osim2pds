(ns osim2pds.pds
  (:gen-class)
  (:use [monger.core :only [connect! set-db! get-db]]
        [monger.collection :only [insert]])
  (:import [org.bson.types ObjectId]
           [com.mongodb DB WriteConcern])
)

;; localhost, default port
(connect!)
(set-db! (get-db "monger-test"))

(defn save
  "Save a patient"
  [forename surname gender dob address]
  (insert "documents" { 
    :_id (ObjectId.) 
    :first forename 
    :last surname 
    :dob dob 
    :gender gender
    :address address
    :expired false
  })
)

