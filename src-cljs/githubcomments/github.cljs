(ns githubcomments.github
  (:require [cljs-http.client :as http])
  (:import [goog Uri]))

(def url (Uri. "https://api.github.com/"))
(defn api-call
  ([endpoint] (api-call http/get endpoint))
  ([method endpoint] (method (.resolve url endpoint)
                             {:with-credentials? false})))

(defn repo-comments [owner name]
  (api-call (Uri. (str "/repos/" owner "/" name "/comments"))))
