(ns githubcomments.core
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [hiccups.core :as hiccups :refer [html]])
  (:require [cljs.core.async :as async]
            [goog.dom :as dom]
            [goog.dom.dataset :as dataset]
            [hiccups.runtime :as hiccupsrt]
            [githubcomments.github :refer [repo-comments
                                           render-user-content-markdown]]))

(defn comment-to-li-tag [comment]
  (html [:li.github-comment {:id (str "comment-" (:id comment))}
         [:div.github-comment-metadata
          (let [user (:user comment)]
            (list [:a {:href (:html_url user)}
                   [:img.github-avatar {:src (:avatar_url user)}]]
                  [:a {:href (:html_url user)}
                   [:p.github-comment-author (:login user)]]))]
         [:div.github-comment-content (:body_html comment)]]))

(defn comments-to-ul [comments]
  (html [:ul (for [comment (take 10 comments)]
               (comment-to-li-tag comment))]))

(defn error-msg [response] (html [:p "Cannot display comments."]))

(defn init []
  (go (let [element (dom/getElement "github-comments")
            repo [(dataset/get element "repositoryOwner")
                  (dataset/get element "repositoryName")]
            path (dataset/get element "path")
            response (async/<!  (repo-comments repo "html"))
            relevant-comments (filter #(= (:path %1) path)(:body response))]
        (set! (.-innerHTML element) (if (:success response)
                                      (comments-to-ul relevant-comments)
                                      (error-msg response))))))

(init)
