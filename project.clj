(use '[clojure.java.shell :only [sh]])
(use '[clojure.string :only [trim]])

(def version
  (trim ((sh "python" "-c"
             "import vcversioner; print vcversioner.find_version().version")
         :out)))

(defproject sphinxcontrib/githubcomments version
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [cljs-http "0.1.39"]
                 [cljsjs/moment "2.10.6-2"]
                 [hiccups "0.3.0"]]
  :plugins [[lein-cljsbuild "1.1.2"]]
  :source-paths ["src-cljs/main/"]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:compiler {:output-to "sphinxcontrib/githubcomments/_static/githubcomments.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :profiles {:dev {:source-paths ["src-cljs/main/" "src-cljs/dev/"]
                   :dependencies [[com.cemerick/piggieback  "0.2.1"]]
                   :repl-options  {:nrepl-middleware
                                   [cemerick.piggieback/wrap-cljs-repl]}}})
