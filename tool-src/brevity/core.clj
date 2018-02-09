(ns brevity.core
  (:require [clojure.java.shell :refer [sh]]
            [stencil.core :as stencil]))

(defn print-error [err]
  (println (str "Error running command: " err)))

(defn execute-command [shell-command]
  (println (str ">>>RUNNING: " shell-command))
  (let [{:keys [exit out err]} (apply sh (clojure.string/split shell-command #" "))]
    (if (= 1 exit)
      (print-error err)
      (do
        (print out)
        (println ">>>Success!")))))

(def default-command #(execute-command "echo Haven't implemented this command yet..."))

(defn generate-scaffolding [name [entity]]
  (let [data {:name name
              :entity entity
              :entity-plural (str entity "s")}
        result (stencil/render-string (slurp "tool-src/templates/entity.clj") data)]
    (spit (str "src/" (:name data) "/models/" (:entity data) ".clj") result)))

(defn generate [name [c & commands]]
  (case c
    "scaffolding" (generate-scaffolding name commands)
    (println "Not a valid command!")))

(defn handle-commands [c & [command & commands]]
  (let [name (first (clojure.string/split c #"\."))]
    (case command
      "generate" (generate name commands)
      "start" (default-command)
      (println "Not a valid command!"))
    (shutdown-agents)))
