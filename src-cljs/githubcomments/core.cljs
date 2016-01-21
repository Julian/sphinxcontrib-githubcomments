(ns githubcomments.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async]
            [goog.dom :as dom]
            [goog.dom.dataset :as dataset]
            [githubcomments.github :refer [repo-comments
                                           render-user-content-markdown]]))

(defn rendered-comment [repo comment]
  "Render the given comment in the context of the given repository."
  (go (let [response (async/<!
                       (render-user-content-markdown (:body comment) repo))]
        (.log js/console (:body response)))))

(enable-console-print!)

(defn rendered-comments [repo comments]
  (map (partial rendered-comment repo) comments))

(defn init []
  (go (let [element (dom/getElement "github-comments")
            repo [(dataset/get element "repositoryOwner")
                  (dataset/get element "repositoryName")]
            response (async/<! (repo-comments repo))]
        (apply println (rendered-comments repo (:body response))))))

(init)
