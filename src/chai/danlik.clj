(ns chai.danlik
  (:require [chai.time :as time]))

(def ^:dynamic *danlik*
  "The string ID of the chaidanlik to consider in all CRUD operations. Override using clojure.core/binding. Defaults to 'Backbaser R&D'"
  "Backbase R&D")

(def stock
  "All the available chaidanlik. Currently only one is supported, but you never know.. Currently implements an in-memory store / cache. Restart the app and you loose the state."
  (atom {*danlik* {:label *danlik*}}))

(defn cleanup
  "Clears the elapsed counters from the provided danlik"
  [danlik]
  (if-let [removables (seq
                       (filter #(time/elapsed? (% danlik))
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
    (swap! stock assoc *danlik* {:time (time/now)
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
  ([key time] (update-danlik key (time/delayed time 25))))

(defn start-brew 
  "Starts brewing the tea"
  ([]
     (start :brew))
  ([[stamp]]
     (cleanup
      (if-let [t (time/parse-time stamp)]
        (start :brew t)
        (start :brew)))))