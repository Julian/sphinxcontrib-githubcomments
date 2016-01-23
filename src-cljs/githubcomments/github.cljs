(ns githubcomments.github
  (:require [cljs-http.client :as http])
  (:import [goog Uri]))

(def url (Uri. "https://api.github.com/"))
(defn api-call
  ([endpoint] (api-call endpoint nil))
  ([endpoint parameters] (api-call http/get endpoint nil))
  ([method endpoint parameters] (api-call method endpoint parameters nil))
  ([method endpoint parameters accept-param]
   (let [accept (str "application/vnd.github.v3"
                     (if accept-param (str "." accept-param) "")
                     "+json")]
     (method (.resolve url endpoint) {:with-credentials? false
                                      :accept accept
                                      :json-params parameters}))))

(defn repo-comments
  ([repo] (repo-comments repo "raw"))
  ([[owner name] body-type] (api-call
                              http/get
                              (Uri. (str "/repos/" owner "/" name "/comments"))
                              nil
                              body-type)))

(defn render-markdown [markdown mode context]
  (api-call http/post (Uri. "/markdown") {:text markdown
                                          :mode mode
                                          :context context}))

(defn render-plain-markdown [markdown]
  (render-markdown markdown :markdown nil))

(defn render-user-content-markdown [markdown [repo-name repo-owner]]
  (render-markdown markdown :gfm (str repo-owner "/" repo-name)))
