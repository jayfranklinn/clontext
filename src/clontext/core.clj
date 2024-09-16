(ns clontext.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:gen-class))

(def cli-options
  [["-h" "--help" "Show help"]
   ["-o" "--output FILE" "Output file name"
    :default "context.txt"]])

(defn list-files
  "Recursively list all files in the given directory"
  [dir]
  (->> (file-seq (io/file dir))
       (filter #(.isFile %))))

(defn read-file-content
  "Read the content of a file"
  [file]
  (try
    (slurp file)
    (catch Exception e
      (str "Error reading file: " (.getMessage e)))))

(defn generate-context
  "Generate context from the given project directory"
  [project-dir output-file]
  (let [files (list-files project-dir)
        context (str/join "\n\n"
                  (for [file files]
                    (str "File: " (.getPath file) "\n"
                         "Content:\n"
                         (read-file-content file))))]
    (spit output-file context)
    (println "Context file generated:" output-file)))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (println summary)
      errors (do (println errors)
                 (System/exit 1))
      (empty? arguments) (println "Please provide a project directory")
      :else (let [project-dir (first arguments)
                  output-file (:output options)]
              (generate-context project-dir output-file)))))