(ns parable.models.mapper
  (:require [parable.utils.core :as u]))

(def private->partners (atom {"1112223333" #{"2222222222"
                                             "3333333333"
                                             "4444444444"}
                              "0001112222" #{"2222222222"
                                             "3333333333"
                                             "4444444444"}}))

(def seeker->partners (atom nil))

(defn link [{:keys [body]}]
  (first (keep identity
           (for [[priv partners] @private->partners] 
             (when-let [partner (first partners)]
               (reset! private->partners (update @private->partners priv disj partner))
               (swap! seeker->partners conj {:seekerNumber body
                                             :partnerNumber partner})
               {priv partner}))))) 

(defn link-partner [{:keys [body]}]
  (for [[priv partners] @private->partners]
    (when (get @partners (:partnerNumber body))
      (swap! private->partners update @private->partners priv disj (:partnerNumber body))
      priv)))

(defn available-partners []
  (reduce into (map val @private->partners)))

(defn list-all []
  (u/ok @seeker->partners))

