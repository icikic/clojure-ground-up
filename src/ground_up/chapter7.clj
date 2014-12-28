(ns ground-up.chapter7
	(:require [cheshire.core :refer :all]))

(defn load-json 
	"Given a filename, reads a JSON file and returns it, parsed, with keywords." 
	[file] 
	(parse-string (slurp file) true))

(def data (load-json "2008.json"))

(def fips 
	"A map of FIPS codes to their county names." 
	(->> "fips.json" load-json :table :rows (into {}))) 

(defn fips-code 
	"Given a county (a map with :fips_state_code and :fips_county_code keys), 
	returns the five-digit FIPS code for the county, as a string." 
	[county] 
	(str (:fips_state_code county) (:fips_county_code county))) 

(defn most-duis 
	"Given a JSON filename of UCR crime data for a particular year, finds the counties with the most DUIs." 
	[file] 
	(->> file 
		load-json 
		(sort-by :driving_under_influence) 
		(take-last 10) 
		(map (fn [county] 
			[(fips (fips-code county)) 
			(:driving_under_influence county)])) 
		(into {})))


;; Exercise 1
;; --------------------------------------------------------------------------
;; most-duis tells us about the raw number of reports, but doesn’t account for differences in county population.
;; One would naturally expect counties with more people to have more crime!
;; Divide the :driving_under_influence of each county by its :county_population to find a prevalence of DUIs,
;; and take the top ten counties based on prevalence. How should you handle counties with a population of zero?
;;
(defn safe-div
  "Divide x and y. If y == 0, return 0"
  [x y]
  (if (zero? y)
    0
    (float (/ x y))))

(defn most-duis-per-capita-macro
  "Given a JSON filename of UCR crime data for a particular year, finds the counties with the most DUIs per capita"
  [file]
  (->> file
       load-json
       (map (fn [county]
              [(fips (fips-code county))
               (safe-div (:driving_under_influence county) (:county_population county))]))
       (sort-by second)
       (take-last 10)
       (into {})))

(defn most-duis-per-capita
  "Given a JSON filename of UCR crime data for a particular year, finds the counties with the most DUIs per capita"
  [file]
  (into {}
      (take-last 10
         (sort-by second
            (map (fn [county] [(fips (fips-code county)) (safe-div (:driving_under_influence county) (:county_population county))])
              (load-json file))))))

;; Exercise 2
;; --------------------------------------------------------------------------
;; How do the prevalence counties compare to the original counties?
;; Expand most-duis to return vectors of [county-name, prevalence, report-count, population]
;; What are the populations of the high-prevalence counties?
;; Why do you suppose the data looks this way? If you were leading a public-health campaign to reduce drunk driving,
;; would you target your intervention based on report count or prevalence? Why?
;;
(defn most-duis-ext
  "Given a JSON filename of UCR crime data for a particular year, finds the counties with the most DUIs per capita"
  [file]
  (->> file
       load-json
       (map (fn [county]
              [(fips (fips-code county))
               (safe-div (:driving_under_influence county) (:county_population county))
               (:driving_under_influence county)
               (:county_population county)]))
       (sort-by second)
       (take-last 10)))


;; Exercise 3
;; --------------------------------------------------------------------------
;; We can generalize the most-duis function to handle any type of crime.
;; Write a function most-prevalent which takes a file and a field name, like :arson,
;; and finds the counties where that field is most often reported, per capita.
;;
(defn most-prevalent
  "Given a JSON filename of UCR crime data for a particular year, finds the counties with the most DUIs per capita"
  [file crime]
  (->> file
       load-json
       (map (fn [county]
              [(fips (fips-code county))
               (safe-div (crime county) (:county_population county))
               (crime county)
               (:county_population county)]))
       (sort-by second)
       (take-last 10)))


