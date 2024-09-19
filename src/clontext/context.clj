(ns clontext.context
  "Namespace for generating the context file from a Clojure project."
  (:require [clojure.string :as str]
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

   Returns:
   Nil, but prints a message indicating where the context file was generated."
  [project-dir output-file]
  (let [ignore-patterns (gitignore/read-gitignore project-dir)
        files (file/list-files project-dir)
        filtered-files (remove #(gitignore/should-ignore? % project-dir ignore-patterns) files)
        context (str "<documents>\n"
                     (str/join "\n"
                       (map-indexed
                         (fn [idx file]
                           (file/file-content file (inc idx)))
                         filtered-files))
                     "\n</documents>")]
    (spit output-file context)
    (println "Context file generated:" output-file)))