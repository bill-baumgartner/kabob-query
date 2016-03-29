
(ns kabob-query.cli
  (:require [clojure.java.io :refer [file reader writer]]
            [clojure.tools.cli :refer [parse-opts]]
            [mantle.collection :refer [select-values]]
            [kabob-query.core :refer [query]])
  (:import [java.io PushbackReader StringReader])
  (:gen-class))

(def cli-opts [["-q" "--query-name NAME" "Query to run."]
               ["-a" "--query-args ARGS" "Argument map for query."
                :parse-fn read-string]
               ["-p" "--backend-params PARAMS" "Parameter map for KB backend."
                :parse-fn read-string]])

(defn suffix-qname
  [m]
  (assoc m :query-name (str (:query-name m) ".mustache")))

(defn -main
  [& args]
  (let [opts (:options (parse-opts args cli-opts))]
    (clojure.pprint/pprint
     (apply query
            (select-values (suffix-qname opts)
                           [:query-name :query-args :backend-params])))))

;; Example invocation:
;; java -jar target/kabob-query-0.1.0-SNAPSHOT-standalone.jar \
;;   kabob-query/cli \
;;   -q sparql/test/connection \
;;   -a "{:value 3}" \
;;   -p "{:db-url \"http://hostname:10035\" :repository-name \"repo\" :username \"user\" :password \"pass\"}"