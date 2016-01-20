(ns githubcomments.core
  (:require-macros [cljs.core.async.macros :refer [go]])
   (:require [cljs.core.async :as async]
             [goog.dom :as dom]
             [githubcomments.github :refer [repo-comments
                                            render-user-content-markdown]]))

(def repo ["Magnetic" "Platform-Guild"])

(defn render-comment [repo comment]
  "Render the given comment in the context of the given repository."
  (go (let [response (async/<! (render-user-content-markdown (:body comment) repo))]
        (.log js/console (:body response)))))

(go (let [response (async/<! (repo-comments repo))]
      (dorun (map (partial render-comment repo) (:body response)))))
