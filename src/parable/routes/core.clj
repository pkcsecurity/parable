(ns parable.routes.core
  (:require [ring.middleware.json :as json]
            [ring.middleware.file :as file]
            [ring.middleware.content-type :as ct]
            [compojure.core :as r]
            [parable.models.mapper :as m]
            [ring.middleware.cors :refer [wrap-cors]]
            [compojure.route :as route]))

(r/defroutes routes
   (r/POST "/link" [] m/link)
   (r/POST "/linkpartner" [] m/link-partner)
   (r/GET "/available" [] m/available-partners)
   (r/GET "/list" [] m/pairs)
   (r/GET "/table" [] m/list-all)
   (r/POST "/flush" [] m/flush)
  (route/not-found nil))

(def app
  (-> routes
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods  [:put :post :get :delete])
      (json/wrap-json-response)
      (json/wrap-json-body {:keywords? true})
      (file/wrap-file "static" {:index-files? false})
      (ct/wrap-content-type)))
