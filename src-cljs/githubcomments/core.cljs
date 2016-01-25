(ns githubcomments.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async]
            [goog.dom :as dom]
            [goog.dom.dataset :as dataset]
            [githubcomments.github :refer [repo-comments
                                           render-user-content-markdown]]))

(defn comment-to-li-tag [comment]
  (str "<li class='github-comment' id='comment-" (:id comment) "'>"
       (let [user (:user comment)]
         (str "<a href='" (:html_url user) "'>"
              "<img src='" (:avatar_url user) "'>"
              "</a>"))
       "<div class='github-comment-content'>"
       (:body_html comment)
       "</div>"
       "</li>"))

(defn display-comments [comments element]
  (set! (.-innerHTML element)
        (str "<ul>"
             (reduce #(str %1 (comment-to-li-tag %2)) "" (take 10 comments))
             "</ul>")))

(defn init []
  (go (let [element (dom/getElement "github-comments")
            repo [(dataset/get element "repositoryOwner")
                  (dataset/get element "repositoryName")]
            path (dataset/get element "path")
            response (async/<! (repo-comments repo "html"))
            relevant-comments (filter #(= (:path %1) path) (:body response))]
        (display-comments relevant-comments element))))

(init)
