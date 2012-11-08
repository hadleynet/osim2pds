(ns osim2pds.osim-test
  (:use clojure.test
        osim2pds.osim))

(deftest gender-code-lookup-test
  (testing "Gender code lookup."
    (is (= "M" (gender_code 8507M)))
    (is (= "F" (gender_code 8532M)))
  )
)