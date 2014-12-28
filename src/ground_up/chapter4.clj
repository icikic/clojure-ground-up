(ns ground-up.chapter4)

;; Problem 1
;; -------------
;; Write a function to find out if a string is a palindrome–that is, if it looks the same forwards and backwards.
;;

(defn reverse-str [s]
  (apply str (reverse (seq s))))

(defn palindrom?
  [s]
  (= (str s) (reverse-str s)))

;; Problem 2
;; --------------
;; Find the number of ‘c’s in “abracadabra”
;;
(defn times [s ch]
  (reduce + (map #(if (= %1 ch) 1 0) (seq s))))

;; Problem 3
;;---------------
;; Write your own version of filter
;;
(defn my-filter
      [f seq]
      (reduce (fn [results x] (if (f x) (conj results x) results))
              [] seq))


;; Problem 4
;; --------------
;; Find the first 100 prime numbers: 2, 3, 5, 7, 11, 13, 17
;;
(defn next-prime-slow
      "Find the next prime number, checking against our already existing list"
      ([sofar guess]
       (if (not-any? #(zero? (mod guess %)) sofar)
         guess                         ; Then we have a prime
         (recur sofar (+ guess 2)))))  ; Try again

(defn find-primes-slow
      "Finds prime numbers, slowly"
      ([]
       (find-primes-slow 10001 [2 3]))   ; How many we need, initial prime seeds
      ([needed sofar]
       (if (<= needed (count sofar))
         sofar                         ; Found enough, we're done
         (recur needed (concat sofar [(next-prime-slow sofar (last sofar))])))))


(defn sieve [s]
      (cons (first s)
            (lazy-seq (sieve (filter #(not= 0 (mod % (first s)))
                                     (rest s))))))

;; Optmized version
(defn clense
      "Walks through the sieve and nils out multiples of step"
      [primes step i]
      (if (<= i (count primes))
        (recur
          (assoc! primes i nil)
          step
          (+ i step))
        primes))

(defn sieve-step
      "Only works if i is >= 3"
      [primes i]
      (if (< i (count primes))
        (recur
          (if (nil? (primes i)) primes (clense primes (* 2 i) (* i i)))
          (+ 2 i))
        primes))

(defn prime-sieve
      "Returns a lazy list of all primes smaller than x"
      [x]
      (drop 2
            (filter (complement nil?)
                    (persistent! (sieve-step
                                   (clense (transient (vec (range x))) 2 4) 3)))))
