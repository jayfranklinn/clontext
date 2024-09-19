(ns clontext.context
  "Namespace for generating the context file from a Clojure project."
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clontext.file :as file]
            [clontext.gitignore :as gitignore]))

(defn generate-context
  "Generate context from the given project directory and save it to the output file."
  [project-dir output-file]
  (let [project-dir-file (io/file project-dir)]
    (when-not (.exists project-dir-file)
      (throw (java.io.FileNotFoundException. (str "Project directory not found: " project-dir))))
    (when-not (.isDirectory project-dir-file)
      (throw (IllegalArgumentException. (str "Not a directory: " project-dir))))
    
    (let [ignore-patterns (gitignore/read-gitignore project-dir)
          files (file/list-files project-dir)
          filtered-files (remove #(gitignore/should-ignore? % project-dir ignore-patterns) files)
          context (str "<documents>\n"
                       (str/join "\n"
                         (map-indexed
                           (fn [idx file]
                             (try
                               (file/file-content project-dir file (inc idx))
                               (catch Exception e
                                 (println (str "Warning: Error processing file " (.getName file) ": " (.getMessage e)))
                                 "")))  ; Skip files that can't be processed
                           filtered-files))
                       "\n</documents>")]
      (try
        (spit output-file context)
        (println "Context file generated:" output-file)
        (catch Exception e
          (throw (java.io.IOException. (str "Error writing to output file: " (.getMessage e)))))))))