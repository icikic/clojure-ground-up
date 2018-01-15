(ns ground-up.chapter8-test
  (:require [clojure.test :refer :all]
            [ground-up.chapter8 :refer :all]
            [clojure.pprint :refer :all]))


(deftest map-values-test
  (let [m {:x 1 :y 2 :z 5}]
  (is (= 2 (:x (map-values inc m))))
  (is (= 3 (:y (map-values inc m))))
  (is (= 6 (:z (map-values inc m))))))

;(deftest spherical-coordinate-test
;  (testing "spherical->cartesian"
;    (is (= (spherical->cartesian {:r 2
;                                  :phi 0
;                                  :theta 0})
;           {:x 0.0 :y 0.0 :z 2.0})))
;
;  (testing "roundtrip"
;    (let [pos {:x 1.0 :y 2.0 :z 3.0}]
;      (is (= pos (-> pos cartesian->spherical spherical->cartesian))))))
;
;(deftest makes-orbit
;  (let [trajectory (->> (atlas-v (centaur)) prepare (trajectory 1))]
;    (when (crashed? trajectory)
;      (println "Crashed at" (crash-time trajectory) "seconds")
;      (println "Maximum altitude" (apoapsis trajectory) "meters at"
;                                  (apoapsis-time trajectory) "seconds"))
;      ; Assert that the rocket eventually made it to orbit.
;      (is (not (crashed? trajectory)))))

(deftest trajectory-info
  (let [trajectory (->> (atlas-v (centaur)) prepare (trajectory 1))]
    (println "Altitudes" (pprint (take 3  (flight trajectory))) "meters"))
  ;(is (not (crashed? trajectory)))
  )

