(ns osim2pds.core-test
  (:use clojure.test
        osim2pds.core))

(deftest date-test
  (testing "Conversion of SQL Timestamp to seconds since epoch"
    (let [ts (java.sql.Timestamp. 10000)]
      (is (= 10 (translate-date ts)))
    )))