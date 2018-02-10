(ns parable.core
  (:gen-class)
  (:require [parable.routes.core :as r]
            [parable.utils.core :as u]
            [immutant.web :as server]))

(def host "0.0.0.0")
(def port 8000)

(defn -main [& args]
 (server/run-dmc r/app :host host :port port))
