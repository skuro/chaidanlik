(ns chai.danlik
  (:require [clj-time.core :as time]
            [clj-time.coerce :as force]))

(def ^:dynamic *danlik*
  "The string ID of the chaidanlik to consider in all CRUD operations. Override using clojure.core/binding. Defaults to 'Backbaser R&D'"
  "Backbase R&D")

(def stock
  "All the available chaidanlik. Currently only one is supported, but you never know.. Currently implements an in-memory store / cache. Restart the app and you loose the state."
  (atom {*danlik* {:label *danlik*}}))

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

(defn cleanup
  "Clears the elapsed counters from the provided danlik"
  [danlik]
  (if-let [removables (seq
                       (filter #(elapsed? (% danlik))
                               [:time :brew]))]
    (apply dissoc danlik removables)
    danlik))

(defn- get-danlik-impl
  "In-memory implementation of a chaidanlik read."
  []
  (cleanup (@stock *danlik*)))

(defn create
  "In-memory implementation of a chaidanlink create"
  []
  (if-let [existing (get-danlik-impl)]
    existing
    (swap! stock assoc *danlik* {:time (now)
                                 :label *danlik*})))

(defn delete
  "In-memory implementation of a chaidanlink delete"
  []
  (if-let [existing (get-danlik-impl)]
    (swap! atom dissoc *danlik*)))

(defn get-danlik
  "Delegating implementation of a chaidanlik read"
  [_]
  (get-danlik-impl))

(defn update-danlik
  "In-memory implementation of a chaidanlik update"
  [k v]
  (letfn [(update-it [old]
            (if-let [previous (old *danlik*)]
              (assoc old *danlik* (assoc previous k v))
              old))]
    ((swap! stock update-it) *danlik*)))

(defn start
  "Starts the chaidanlik. If no parameters are passed, the water heating is started"
  ([] (start :time))
  ([key] (start :time (time/now)))
  ([key time] (update-danlik key (delayed time 25))))

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

(defn start-brew 
  "Starts brewing the tea"
  ([]
     (start :brew))
  ([[stamp]]
     (cleanup
      (if-let [t (parse-time stamp)]
        (start :brew t)
        (start :brew)))))