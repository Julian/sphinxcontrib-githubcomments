(ns githubcomments.core
  (:require-macros [cljs.core.async.macros :refer [go]])
   (:require [cljs.core.async :refer [<!]]
             [goog.dom :as dom]
             [githubcomments.github :refer [repo-comments
                                            render-user-content-markdown]]))

(def repo ["Magnetic" "Platform-Guild"])

(defn render-comment [comment] (.log js/console (:body comment)))

(go (let [response (<! (repo-comments repo))]
      (dorun (map render-comment (:body response)))))
