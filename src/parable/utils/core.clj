(ns parable.utils.core
  (:require [clojure.string :as str]
            ))
(defn ok 
  ([] {:status 201})
  ([body]
    {:status 200
     :body body}))
