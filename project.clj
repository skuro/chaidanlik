(defproject chai "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://chai.herokuapp.com"
  :license {:name "FIXME: choose"
            :url "http://example.com/FIXME"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.1"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [ring/ring-devel "1.1.0"]
                 [ring-basic-authentication "1.0.1"]
                 [environ "0.2.1"]
                 [de.ubercode.clostache/clostache "1.3.1"]
                 [org.codehaus.jackson/jackson-core-asl "1.9.5"]
                 [org.codehaus.jackson/jackson-smile "1.9.5"]
                 [com.cemerick/drawbridge "0.0.6"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.2.1"]]
  :hooks [environ.leiningen.hooks]
  :main chai.web
  :profiles {:production {:env {:production true}}})