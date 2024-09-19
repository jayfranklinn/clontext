(ns clontext.gitignore
  "Namespace for handling .gitignore patterns in the Clontext application."
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import (java.nio.file FileSystems Path)))

(defn- glob->regex
  "Convert a glob pattern to a regex pattern.
   
   Parameters:
   - glob-pattern: String representing a glob pattern

   Returns:
   A string representing the equivalent regex pattern."
  [glob-pattern]
  (-> glob-pattern
      (str/replace #"\." "\\.")
      (str/replace #"\*" ".*")
      (str/replace #"\?" ".")
      (str/replace #"\[" "\\[")
      (str/replace #"\]" "\\]")
      (as-> pattern (str "^" pattern "$"))))

(defn read-gitignore
  "Read and process the .gitignore file in the given directory.
   
   This function reads the .gitignore file, processes its contents, and returns
   a sequence of regex patterns. It also includes some built-in patterns.

   Parameters:
   - dir: String path to the directory containing the .gitignore file

   Returns:
   A sequence of regex patterns (as Pattern objects) representing ignore rules."
  [dir]
  (let [gitignore-file (io/file dir ".gitignore")
        git-dir-pattern (re-pattern "^.git(/.*)?$")
        builtin-patterns [git-dir-pattern]]
    (if (.exists gitignore-file)
      (into builtin-patterns
            (->> (slurp gitignore-file)
                 str/split-lines
                 (remove str/blank?)
                 (remove #(str/starts-with? % "#"))
                 (map glob->regex)
                 (map re-pattern)))
      builtin-patterns)))

(defn should-ignore?
  "Check if a file should be ignored based on gitignore patterns.
   
   Parameters:
   - file: java.io.File object representing the file to check
   - base-dir: String path to the base directory of the project
   - ignore-patterns: Sequence of regex patterns from read-gitignore

   Returns:
   Boolean indicating whether the file should be ignored (true) or not (false)."
  [file base-dir ignore-patterns]
  (let [relative-path (-> (FileSystems/getDefault)
                          (.getPath base-dir (into-array String []))
                          (.relativize (.toPath (io/file file)))
                          str)]
    (some #(re-matches % relative-path) ignore-patterns)))