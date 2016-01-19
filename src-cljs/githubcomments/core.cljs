(ns githubcomments.core
  (:require-macros [cljs.core.async.macros :refer [go]])
   (:require [cljs.core.async :refer [<!]]
             [goog.dom :as dom]
             [githubcomments.github :refer [repo-comments]]))

(go (let [response (<! (repo-comments "Magnetic" "Platform-Guild"))]
      (.log js/console (first (map :body (:body response))))))
