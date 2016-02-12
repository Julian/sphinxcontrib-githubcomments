(ns githubcomments.dev
  (:require [clojure.browser.repl :as repl]))

(enable-console-print!)

(goog-define repl-uri false)
(if repl-uri (defonce conn (repl/connect repl-uri)))
