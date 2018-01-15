(ns ground-up.chapter7-test
  (:require [clojure.test :refer :all]
            [ground-up.chapter7 :refer :all]))


(deftest fips-code-test 
	(is (= "12345" (fips-code {:fips_state_code "12" :fips_county_code "345"}))))

(deftest most-duis-test
  (is (= 17572 ((most-duis ucr-2008-url) "CA, Orange"))))