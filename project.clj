(defproject modern-cljs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.2.0"
  
  :source-paths ["src/clj"]
  
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1847"]
                 [compojure "1.1.5"]
                 [hiccups "0.2.0"]
                 [domina "1.0.2-SNAPSHOT"]
                 [org.clojars.magomimmo/shoreleave-remote-ring "0.3.1-SNAPSHOT"]
                 [org.clojars.magomimmo/shoreleave-remote "0.3.1-SNAPSHOT"]
                 [com.cemerick/valip "0.3.2"]
                 [enlive "1.1.4"]
                 ;[org.clojars.magomimmo/enfocus "2.0.0-SNAPSHOT"]
                 ]
  
  :plugins [[lein-ring "0.8.7"]
            [lein-cljsbuild "0.3.3"]]
  
  :hooks [leiningen.cljsbuild]

  :ring {:handler modern-cljs.core/app}

  :cljsbuild {:crossovers [valip.core
                           valip.predicates
                           modern-cljs.login.validators
                           modern-cljs.shopping.validators]
              
              :builds {:prod
                       {:source-paths ["src/cljs"]
                        
                        :compiler {:output-to "resources/public/js/modern.js"
                                   :optimizations :advanced
                                   :pretty-print false}}}}
  
  :profiles {:dev {:test-paths ["target/test/clj"]
                   :clean-targets ["out"]
                   
                   :dependencies [[com.cemerick/clojurescript.test "0.0.4"]
                                  [com.cemerick/piggieback "0.1.0"]]
                   
                   :plugins [[com.keminglabs/cljx "0.3.0"]]
                   
                   :cljx {:builds [{:source-paths ["test/cljx"]
                                    :output-path "target/test/clj"
                                    :rules :clj}
                                   
                                   {:source-paths ["test/cljx"]
                                    :output-path "target/test/cljs"
                                    :rules :cljs}]}
                   
                   :cljsbuild {:test-commands {"phantomjs-whitespace"
                                               ["runners/phantomjs.js" "target/test/js/testable_dbg.js"]
                                               
                                               "phantomjs-simple"
                                               ["runners/phantomjs.js" "target/test/js/testable_pre.js"]
                                               
                                               "phantomjs-advanced"
                                               ["runners/phantomjs.js" "target/test/js/testable.js"]}
                               :builds
                               {:dev
                                {:source-paths ["src/brepl" "src/cljs"]
                                 :compiler {:output-to "resources/public/js/modern_dbg.js"
                                            :optimizations :whitespace
                                            :pretty-print true}}
                                
                                :pre-prod
                                {:source-paths ["src/brepl" "src/cljs"]
                                 :compiler {:output-to "resources/public/js/modern_pre.js"
                                            :optimizations :simple
                                            :pretty-print false}}
                                
                                :ws-unit-tests
                                {:source-paths ["src/brepl" "src/cljs" "target/test/cljs"]
                                 :compiler {:output-to "target/test/js/testable_dbg.js"
                                            :optimizations :whitespace
                                            :pretty-print true}}
                                
                                :simple-unit-tests
                                { :source-paths ["src/brepl" "src/cljs" "target/test/cljs"]
                                 :compiler {:output-to "target/test/js/testable_pre.js"
                                            :optimizations :simple
                                            :pretty-print false}}
                                
                                :advanced-unit-tests
                                {:source-paths ["src/cljs" "target/test/cljs"]
                                 :compiler {:output-to "target/test/js/testable.js"
                                            :optimizations :advanced
                                            :pretty-print false}}}}
                   
                   :aliases {"clean-test!" ["do" "clean," "cljx" "once," "compile," "test"]
                             "clean-start!" ["do" "clean," "cljx" "once," "compile," "ring" "server-headless"]}            
                   
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :injections [(require '[cljs.repl.browser :as brepl]
                                         '[cemerick.piggieback :as pb])
                                (defn browser-repl []
                                  (pb/cljs-repl :repl-env
                                                (brepl/repl-env :port 9000)))]}})


