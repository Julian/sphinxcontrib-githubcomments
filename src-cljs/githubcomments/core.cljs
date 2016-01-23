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

(defn rendered-comment [repo comment]
  "Render the given comment in the context of the given repository."
  (go (let [response (async/<!
                       (render-user-content-markdown (:body comment) repo))]
        (:body response))))

(defn rendered-comments [repo comments]
  (map :body_html comments))

(defn init []
  (go (let [element (dom/getElement "github-comments")
            repo [(dataset/get element "repositoryOwner")
                  (dataset/get element "repositoryName")]
            path (dataset/get element "path")
            response (async/<! (repo-comments repo "html"))
            relevant (filter #(= (:path %1) path) (:body response))
            comments (rendered-comments repo relevant)]
        (display-comments comments element))))

(init)
