(ns ground-up.chapter5)

;; from-ground-up, macros, problem 1
(defn schedule [t] 
	(cond 
		(< t 8) :sleep
		(< t 17) :work
		(< t 18) :commute
		(< t 20) :climb
		(< t 21) :dinner
		(< t 24) :read
		:else :error))

;; from-ground-up, macros, problem 2 with map reduce
(defn reverse-num [n]
	(apply str (reverse (seq (str n)))))

(defn palindrom? 
	[n]
	(= (str n) (reverse n)))

(defn palindrom [n] 
	(if (palindrom? n)
		1
		0))

(defn palindrom-count [bound]
	(reduce + 0 (map palindrom (range 0 bound))))

;; from-ground-up, macros, problem 2 with -> macro
(defn palindrom-count2 [bound]
	(->> (range bound) (map palindrom) (reduce +))) 
	
;; from-ground-up, macros, problem 3
(defmacro id [f & args] 
	(cons f args))
	
;; from-ground-up, macros, problem 4
(defmacro log [msg]
	(if (logging-enabled) 
		(prn msg)
		nil))

