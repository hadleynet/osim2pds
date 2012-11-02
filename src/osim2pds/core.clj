(ns osim2pds.core
  (:gen-class)
  (:require [osim2pds.osim :as osim] [osim2pds.pds :as pds] [osim2pds.rand :as rand])
)

(defn -main
  "Migrate OSIM2 patient data to Patient Data Server."
  [& args]
 
  (doseq [id (range 1 11)]
    (let [patient (first (osim/person id))]
      (if patient
        (do 
          (let [
            gender (osim/gender_code (patient :GENDER_CONCEPT_ID))
            forename (rand/forename gender)
            surname (rand/surname)
            address (rand/address)
            yob (patient :YEAR_OF_BIRTH) 
            dob (java.util.GregorianCalendar. yob (rand-int 12) (+ 1 (rand-int 28)))
            ts (quot (.getTimeInMillis dob) 1000)
          ]
            (pds/save forename surname gender ts address)
            (print ".")
          )
        )
      )
    )
  )
  (print "\n")
)
