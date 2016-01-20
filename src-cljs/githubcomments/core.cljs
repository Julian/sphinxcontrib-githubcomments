(ns githubcomments.core
  (:require-macros [cljs.core.async.macros :refer [go]])
   (:require [cljs.core.async :refer [<!]]
             [goog.dom :as dom]
             [githubcomments.github :refer [repo-comments
                                            render-user-content-markdown]]))

(def repo ["Magnetic" "Platform-Guild"])

(defn render-comment [repo comment]
  (go (let [response (<! (render-user-content-markdown (:body comment) repo))]
        (.log js/console (:body response)))))

(go (let [response (<! (repo-comments repo))]
      (dorun (map (partial render-comment repo) (:body response)))))
