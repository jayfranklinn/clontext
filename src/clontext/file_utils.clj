(ns clontext.file-utils
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn read-gitignore [dir]
  (let [gitignore-file (io/file dir ".gitignore")]
    (if (.exists gitignore-file)
      (str/split-lines (slurp gitignore-file))
      [])))

(defn should-ignore? [file ignore-patterns]
  (let [path (.getPath file)]
    (some #(str/includes? path %) ignore-patterns)))

(defn file-content [file]
  (str "File: " (.getPath file) "\n"
       "Content:\n"
       (slurp file)
       "\n\n"))

(defn process-directory [dir]
  (let [ignore-patterns (read-gitignore dir)]
    (->> (file-seq (io/file dir))
         (remove #(should-ignore? % ignore-patterns))
         (filter #(.isFile %))
         (map file-content)
         (str/join)
         (spit "project_context.txt")))
  (println "Project context has been written to project_context.txt"))