(ns chai.time
  (:require [clj-time.core :as time]
            [clj-time.coerce :as force]))

(defn delayed
  "Returns the timestamp (in long) of the moment mins minutes from now"
  ([from mins]
     (force/to-long (time/plus (force/from-long from) (time/minutes mins))))
  ([mins]
      (force/to-long (-> mins
                         time/minutes
                         time/from-now))))

(defn now
  "Returns the timestamp of now"
  []
  (delayed 0))

(defn elapsed?
  "Returns true if the provided date is in the past"
  [date]
  (time/after? (force/to-date-time (now))
               (force/to-date-time date)))

(defn parse-time
  "Accepts a string like '12:12' and returns a date describing today with the provided hours and minutes"
  [t]
  (try
    (let [[hh mm] (map #(Integer/parseInt %) (clojure.string/split t #":"))
          base (time/to-time-zone (time/now)
                                  (time/time-zone-for-offset 1))]
      (-> base
          (.withHourOfDay hh)
          (.withMinuteOfHour mm)
          force/to-long))
    (catch Exception e
      (.printStackTrace e)
      nil)))

