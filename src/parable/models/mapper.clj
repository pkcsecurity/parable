(ns parable.models.mapper
  (:require [parable.utils.core :as u]))

(def all-partners #{"Mike Mirabella" "+1 (408) 368-9913"})

(def private->partners [(atom all-partners)
                        (atom all-partners)])

(def seeker->private (atom {}))

(def seeker->partners (atom {}))

(defn flush [_]
  (doseq [ps private->partners]
    (reset! ps all-partners))
  (reset! seeker->private {})
  (reset! seeker->partners {}))

(defn link-all [seeker-id private-id partner-id]
  (swap! (nth private->partners private-id) disj partner-id)
  (swap! seeker->partners 
         assoc
         seeker-id
         {:seekerNumber seeker-id
          :partnerNumber partner-id})
  (swap! seeker->private assoc seeker-id private-id)
  (u/ok {:private-id private-id 
         :seeker-id seeker-id
         :partner-id partner-id}))

(defn link [{{:keys [seeker-id]} :body}]
  (loop [private-id 0]
    (when (< private-id (count private->partners))
      (let [partners (nth private->partners private-id)]
        (if-let [partner-id (first @partners)]
          (link-all seeker-id private-id partner-id)
          (recur (inc private-id)))))))

(defn link-partner [{{:keys [seeker-id partner-id]} :body}]
  (when-let [p-id (get @seeker->private seeker-id)]
    (swap! (nth private->partners p-id) conj partner-id)
    (loop [private-id 0]
      (when (< private-id (count private->partners))
        (let [partners (nth private->partners private-id)]
          (if (contains? @partners partner-id)
            (link-all seeker-id private-id partner-id)
            (recur (inc private-id))))))))

(defn available-partners [_]
  (u/ok {:partners (vec (reduce into (map deref private->partners)))}))

(defn pairs [_]
  (u/ok (vals @seeker->partners)))

(defn list-all [_]
  (u/ok {:seeker->private @seeker->private
         :seeker->partners @seeker->partners}))
