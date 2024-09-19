(ns clontext.context
  "Namespace for generating the context file from a Clojure project."
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clontext.file :as file]
            [clontext.gitignore :as gitignore]))

(defn generate-context
  "Generate context from the given project directory and save it to the output file.
   
   This function performs the following steps:
   1. Read .gitignore patterns from the project directory
   2. List all files in the project directory
   3. Filter out files that should be ignored based on .gitignore patterns
   4. Generate XML-like content for each file
   5. Combine all file contents into a single context string
   6. Write the context string to the output file

   Parameters:
   - project-dir: String path to the Clojure project directory
   - output-file: String path where the context file should be saved

   Throws:
   - java.io.FileNotFoundException if the project directory doesn't exist
   - java.io.IOException for other I/O related errors"
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
                               (file/file-content file (inc idx))
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