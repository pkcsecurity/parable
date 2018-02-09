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
                   ;TODO: need to rm it from the map too
                   ; test this:
                   (reset! seeker->partners
                           (remove @seeker->partners #(and 
                                                        (= (get-in % :seekerNumber) body)
                                                        (= (get-in % :partnerNumber) partner))))
                   (swap! seeker->partners conj {:seekerNumber body
                                                 :partnerNumber partner})
                   {priv partner}))))) 

;provide a partner
;get a new seeker->private->partner mapping
(defn link-partner [{:keys [body]}]
  (for [[priv partners] @private->partners]
    (when (get @partners (:partnerNumber body))
      (reset! private->partners (update @private->partners priv disj (:partnerNumber body)))
     ;TODO: this is wrong, fix it for this function:
     (reset! seeker->partners
              (remove @seeker->partners #(and 
                                           (= (get-in % :seekerNumber) body)
                                           (= (get-in % :partnerNumber) partner))))     
      (swap! seeker->partners conj {:seekerNumber (:seekerNumber body)
                                                                                                                      :partnerNumber (:partnerNumber body)})
      priv)))

(defn available-partners []
  (reduce into (map val @private->partners)))

(defn list-all []
  (u/ok @seeker->partners))

