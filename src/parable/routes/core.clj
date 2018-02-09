(ns parable.routes.core
  (:require [ring.middleware.json :as json]
            [ring.middleware.file :as file]
            [ring.middleware.content-type :as ct]
            [compojure.core :as r]
            [parable.models.mapper :as m]
            [ring.middleware.cors :refer [wrap-cors]]
            [compojure.route :as route]))

(r/defroutes routes
  (r/context "/sp" []
             (r/DELETE "/" [] m/rm-sp)
             (r/PUT "/" [] m/add-sp)
             (r/POST "/" [] m/update-sp)
             (r/GET "/" [] m/get-sp))

  (r/context "/pp" []
             (r/DELETE "/" [] m/rm-pp)
             (r/PUT "/" [] m/add-pp)
             (r/POST "/" [] m/update-pp)
             (r/GET "/" [] m/get-pp))

  (r/context "/np" []
             (r/DELETE "/" [] m/rm-np)
             (r/PUT "/" [] m/add-np)
             (r/POST "/" [] m/update-np)
             (r/GET "/" [] m/get-np))

  (r/POST "/link" [] m/link)

  (route/not-found nil))

(def app
  (-> routes
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods  [:put :post :get :delete])
      (json/wrap-json-response)
      (json/wrap-json-body {:keywords? true})
      (file/wrap-file "static" {:index-files? false})
      (ct/wrap-content-type)))
