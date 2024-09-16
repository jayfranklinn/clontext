(ns clontext.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clontext.context :as context])
  (:gen-class))

(def cli-options
  [["-h" "--help" "Show help"]
   ["-o" "--output FILE" "Output file name"
    :default "context.txt"]])

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (println summary)
      errors (do (println errors)
                 (System/exit 1))
      (empty? arguments) (println "Please provide a project directory")
      :else (let [project-dir (first arguments)
                  output-file (:output options)]
              (context/generate-context project-dir output-file)))))