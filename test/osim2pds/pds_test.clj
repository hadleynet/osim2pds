(ns osim2pds.pds-test
  (:use clojure.test
        osim2pds.pds))

(deftest mongoidize-test
  (testing "Mongoidization of a hash."
    (let [h (mongoidize {"foo" "bar"} "Test")]
      (is (= "Test" (h "_type")))
      (is (not (nil? (h "_id"))))
    )))