(ns githubcomments.github
  (:require [cljs-http.client :as http])
  (:import [goog Uri]))

(def url (Uri. "https://api.github.com/"))
(defn api-call
  ([endpoint] (api-call http/get endpoint))
  ([method endpoint & [parameters]] (method (.resolve url endpoint)
                                            {:with-credentials? false
                                             :json-parameters parameters})))

(defn repo-comments [[owner name]]
  (api-call (Uri. (str "/repos/" owner "/" name "/comments"))))

(defn render-markdown [markdown mode context]
  (api-call http/post (Uri. "/markdown") {:text markdown
                                          :mode mode
                                          :context context}))

(defn render-plain-markdown [markdown]
  (render-markdown markdown :markdown nil))

(defn render-user-content-markdown [markdown [repo-name repo-owner]]
  (render-markdown markdown :gfm (str repo-owner "/" repo-name)))
