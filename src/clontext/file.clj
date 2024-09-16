(ns clontext.file
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

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

;; Change to adhere to llm preferred context
(defn file-content [file]
  (str "File: " (.getPath file) "\n"
       "Content:\n"
       (read-file-content file)
       "\n\n"))