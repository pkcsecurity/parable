(defproject parable "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:source-paths ["src" "tool-src"]}
             :uberjar {:aot :all}}
  :aliases {"brevity" ["run" "-m" "brevity.core/handle-commands" :project/main]}
  :main ^:skip-aot parable.core
  :dependencies [[org.clojure/clojure "LATEST"]
                 [org.immutant/web "LATEST"]
                 [ring/ring-core "LATEST"]
                 [ring/ring-devel "LATEST"]
                 [ring/ring-json "LATEST"]
                 [ring-cors "LATEST"]
                 [compojure "LATEST"]
                 ])
