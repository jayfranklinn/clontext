(ns clontext.file
  "Namespace for file-related operations in the Clontext application."
  (:require [clojure.java.io :as io])
  (:import (java.nio.file Paths)))

(defn list-files
  "Recursively list all files in the given directory."
  [dir]
  (let [dir-file (io/file dir)]
    (when-not (.exists dir-file)
      (throw (java.io.FileNotFoundException. (str "Directory not found: " dir))))
    (when-not (.isDirectory dir-file)
      (throw (IllegalArgumentException. (str "Not a directory: " dir))))
    
    (->> (file-seq dir-file)
         (filter #(.isFile %)))))

(defn read-file-content
  "Read the content of a file."
  [file]
  (try
    (slurp file)
    (catch Exception e
      (throw (java.io.IOException. (str "Error reading file " (.getName file) ": " (.getMessage e)))))))

(defn get-relative-path
  "Get the relative path of a file from the project root."
  [project-root file]
  (let [root-path (Paths/get (.getAbsolutePath (io/file project-root)) (make-array String 0))
        file-path (Paths/get (.getAbsolutePath file) (make-array String 0))]
    (.toString (.relativize root-path file-path))))

(defn file-content
  "Generate the content for a file in the desired XML-like format."
  [project-root file index]
  (let [relative-path (get-relative-path project-root file)
        content (read-file-content file)]
    (format "<document index=\"%d\">\n<source>%s</source>\n<document_content>%s</document_content>\n</document>"
            index
            relative-path
            content)))