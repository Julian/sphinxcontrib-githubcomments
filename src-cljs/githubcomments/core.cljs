(ns githubcomments.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async]
            [goog.dom :as dom]
            [goog.dom.dataset :as dataset]
            [githubcomments.github :refer [repo-comments
                                           render-user-content-markdown]]))

(defn display-comments [comments element]
  (do (go (set! (.-innerHTML element)
                (loop [concated-comments ""]
                  (let [comment (async/<! comments)]
                    (if (nil? comment)
                      concated-comments
                      (recur (str concated-comments comment)))))))))

(defn rendered-comment [repo comment]
  "Render the given comment in the context of the given repository."
  (go (let [response (async/<!
                       (render-user-content-markdown (:body comment) repo))]
        (:body response))))

(defn rendered-comments [repo comments]
  (async/merge (map (partial rendered-comment repo) comments)))

(defn init []
  (go (let [element (dom/getElement "github-comments")
            repo [(dataset/get element "repositoryOwner")
                  (dataset/get element "repositoryName")]
            response (async/<! (repo-comments repo))
            comments (rendered-comments repo (:body response))]
        (display-comments comments element))))

(init)
