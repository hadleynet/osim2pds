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
  [dob gender]
  (insert "documents" { :_id (ObjectId.) :dob dob :gender gender })
)

