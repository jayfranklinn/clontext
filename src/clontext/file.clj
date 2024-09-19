(ns clontext.file
  (:require [clojure.java.io :as io]))

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

(defn file-content
  "Generate the content for a file in the desired format"
  [file index]
  (let [source (.getName file)
        content (read-file-content file)]
    (format "<document index=\"%d\">\n<source>%s</source>\n<document_content>%s</document_content>\n</document>"
            index
            source
            content)))