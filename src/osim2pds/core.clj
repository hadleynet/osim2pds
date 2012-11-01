(ns osim2pds.core
  (:gen-class)
  (:use osim2pds.osim)
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
