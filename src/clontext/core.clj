(ns clontext.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clontext.file-utils :as fu])
  (:gen-class))

(def cli-options
  [["-d" "--directory DIR" "Directory to process"
    :default "."
    :validate [#(.isDirectory (java.io.File. %)) "Must be a valid directory"]]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (println summary)
      errors (do (println errors)
                 (System/exit 1))
      :else (fu/process-directory (:directory options)))))

(defn process-project [dir]
  (fu/process-directory dir))

(process-project "./")