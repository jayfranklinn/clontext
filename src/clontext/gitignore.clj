(ns clontext.gitignore
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import (java.nio.file FileSystems Path)))

(defn- glob->regex
  "Convert a glob pattern to a regex pattern"
  [glob-pattern]
  (-> glob-pattern
      (str/replace #"\." "\\.")
      (str/replace #"\*" ".*")
      (str/replace #"\?" ".")
      (str/replace #"\[" "\\[")
      (str/replace #"\]" "\\]")
      (as-> pattern (str "^" pattern "$"))))

(defn read-gitignore [dir]
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
  "Check if a file should be ignored based on gitignore patterns"
  [file base-dir ignore-patterns]
  (let [relative-path (-> (FileSystems/getDefault)
                          (.getPath base-dir (into-array String []))
                          (.relativize (.toPath (io/file file)))
                          str)]
    (some #(re-matches % relative-path) ignore-patterns)))