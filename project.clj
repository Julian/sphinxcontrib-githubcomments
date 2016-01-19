(use '[clojure.java.shell :only [sh]])
(use '[clojure.string :only [trim]])

(def version
  (trim ((sh "python" "-c"
             "import vcversioner; print vcversioner.find_version().version")
         :out)))

(defproject sphinxcontrib/githubcomments version
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [cljs-http "0.1.39"]]
  :plugins [[lein-cljsbuild "1.1.2"]]
  :cljsbuild {:builds
              {:min {:source-paths ["src"]
                     :compiler {:output-to "sphinxcontrib/githubcomments/_static/githubcomments.js"
                                :optimizations :advanced}}}}
  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [org.clojure/tools.nrepl "0.2.11"]]
                   :cljsbuild {:optimizations :whitespace}
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
