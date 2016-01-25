(ns githubcomments.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async]
            [goog.dom :as dom]
            [goog.dom.dataset :as dataset]
            [githubcomments.github :refer [repo-comments
                                           render-user-content-markdown]]))

(defn display-comments [comments element]
  (set! (.-innerHTML element)
        (str "<ul>"
             (reduce #(str %1 "<li class='github-comment'>" %2 "</li>")
                     ""
                     (take 10 comments))
             "</ul>")))

(defn init []
  (go (let [element (dom/getElement "github-comments")
            repo [(dataset/get element "repositoryOwner")
                  (dataset/get element "repositoryName")]
            path (dataset/get element "path")
            response (async/<! (repo-comments repo "html"))
            relevant (filter #(= (:path %1) path) (:body response))
            comments-html (map :body_html relevant)]
        (display-comments comments-html element))))

(init)
