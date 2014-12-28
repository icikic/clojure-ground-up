(ns ground-up.chapter6)

(defn sum
      [start end]
      (reduce + (range start end)))


(defn current-thread []
      (Thread/currentThread))

;;(def sum-future
;;  (future (sum start end) (->> (current-thread) .getName prn)))


;; Problem 2
;; -------------
;; We can do the computation in a new thread directly, using (.start (Thread. (fn [] (sum 0 1e7)))
;; but this simply runs the (sum) function and discards the results.
;; Use a promise to hand the result back out of the thread.
;; Use this technique to write your own version of the future macro.
;;

(def sum-result (promise))
(def sum-runner
    (future (Thread/sleep 5000) (deliver sum-result (sum 0 1e7))))
(time @sum-result)


;; Problem 3
;; -------------
;; If your computer has two cores, you can do this expensive computation twice as fast
;; by splitting it into two parts: (sum 0 (/ 1e7 2)), and (sum (/ 1e7 2) 1e7),
;; then adding those parts together.
;; Use future to do both parts at once, and show that this strategy gets the same answer as the single-threaded version,
;; but takes roughly half the time.

(time (+ (deref (future (sum 1 5e6))) (deref (future (sum 5e6 10e6)))))

(time (reduce + (map deref (conj  [] (future (sum 1 5e6)) (future (sum 5e6 10e6))))))

(def left (future (sum 1 50000000)))
(def right (future (sum 50000000 100000000)))
(time (+ @left @right))

;; Problem 4
;; -------------
;; Instead of using reduce, store the sum in an atom and use two futures to add each number from the lower and upper
;; range to that atom. Wait for both futures to complete using deref, then check that the atom contains the right number.
;; Is this technique faster or slower than reduce? Why do you think that might be?
;;
(def x (atom 0))
(def left (future (swap! x + (sum 1 5e6))))
(def right (future (swap! x + (sum 5e6 10e6))))
(time (when (and @left @right) @x))

(defn parallel-sum []
      (let [acc (atom 0)
            count (atom 0)
            p (promise)
            f (fn [] (swap! count inc) (when (= @count 2) (deliver p @acc)))
            ]
      (do
        (future (swap! acc + (sum 1 5e6)) (f))
        (future (swap! acc + (sum 5e6 10e6)) (f))
        (deref p)
       )
))


;; Problem 5
;; --------------
;; Instead of using a lazy list, imagine two threads are removing tasks from a pile of work.
;; Our work pile will be the list of all integers from 0 to 10000:
;;
(def work (ref (apply list (range 1 1e8))))

(def sum2 (ref 0))

(defn add-first
      [q acc]
      (dosync
        (let [to-add (first @q)]
        (alter acc + to-add)
        (alter q rest)))
      nil)


(defn parallel-sum5 []
      (let [
            q (ref (apply list (range 1 1e5)))
            acc (ref 0)]
      (do
        (map deref (conj []
          (future (dosync (while (seq @q) (add-first q acc))))
          (future (dosync (while (seq @q) (add-first q acc))))))
      (deref acc))))
