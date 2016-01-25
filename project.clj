(use '[clojure.java.shell :only [sh]])
(use '[clojure.string :only [trim]])

(def version
  (trim ((sh "python" "-c"
             "import vcversioner; print vcversioner.find_version().version")
         :out)))

(defproject sphinxcontrib/githubcomments version
  :source-paths ["src-cljs"]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [cljs-http "0.1.39"]
                 [hiccups "0.3.0"]]
  :cljsbuild {:builds [{:compiler {:output-to "sphinxcontrib/githubcomments/_static/githubcomments.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :profiles {:dev {:plugins [[com.cemerick/austin "0.1.6"]
                             [lein-cljsbuild "1.1.2"]]}})
