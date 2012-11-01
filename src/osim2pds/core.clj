(ns osim2pds.core
  (:gen-class)
  (:require [osim2pds.osim :as osim] [osim2pds.pds :as pds])
)

(defn -main
  "Migrate OSIM2 patient data to Patient Data Server."
  [& args]
 
  (doseq [id (range 1 11)]
    (let [patient (first (osim/person id))]
      (if patient
        (do 
          (let [yob (patient :YEAR_OF_BIRTH) gender (patient :GENDER_CONCEPT_ID) ts (java.util.Calendar/getInstance)]
            (print ".")
            (. ts set (.intValue yob) java.util.Calendar/JANUARY 31)
            (pds/save (quot (.getTimeInMillis ts) 1000) (.intValue gender))
          )
        )
      )
    )
  )
  (print "\n")
)
