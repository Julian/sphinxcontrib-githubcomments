(ns githubcomments.core
  (:require-macros [cljs.core.async.macros :refer [go]])
   (:require [cljs.core.async :as async]
             [goog.dom :as dom]
             [githubcomments.github :refer [repo-comments
                                            render-user-content-markdown]]))

(def repo ["Magnetic" "Platform-Guild"])

(defn rendered-comment [repo comment]
  "Render the given comment in the context of the given repository."
  (go (let [response (async/<! (render-user-content-markdown (:body comment) repo))]
        (.log js/console (:body response)))))

(defn rendered-comments [repo comments]
  (map (partial rendered-comment repo) comments))

(defn init []
  (let [element (dom/getElement "github-comments")]
    (go (let [response (async/<! (repo-comments repo))]
          (dorun (rendered-comments repo (:body response)))))

    ))

(init)
