
(ns chai.danlik-test
  (:use chai.danlik
        clojure.test)
  (:require [clj-time.core :as time]))

(deftest elapsed-tests
  (let [d1 (time/now)
        d2 (-> 1 time/minutes time/from-now)
        _  (Thread/sleep 100)]
    (is (= true (elapsed? d1)) "Can't spot past dates")))

(deftest delayed-tests
  (let [d1 (delayed 5)]
    (is (= false (elapsed? d1)))))