(ns clontext.file
  "Namespace for file-related operations in the Clontext application."
  (:require [clojure.java.io :as io]))

(defn list-files
  "Recursively list all files in the given directory.
   
   Parameters:
   - dir: String path to the directory to be scanned

   Returns:
   A sequence of java.io.File objects representing all files (not directories) 
   found in the given directory and its subdirectories."
  [dir]
  (->> (file-seq (io/file dir))
       (filter #(.isFile %))))

(defn read-file-content
  "Read the content of a file.
   
   Parameters:
   - file: java.io.File object representing the file to be read

   Returns:
   A string containing the file's content, or an error message if the file
   cannot be read."
  [file]
  (try
    (slurp file)
    (catch Exception e
      (str "Error reading file: " (.getMessage e)))))

(defn file-content
  "Generate the content for a file in the desired XML-like format.
   
   Parameters:
   - file: java.io.File object representing the file
   - index: Integer representing the file's index in the context

   Returns:
   A string containing the file's content wrapped in XML-like tags, including
   the file's name as the source and its content."
  [file index]
  (let [source (.getName file)
        content (read-file-content file)]
    (format "<document index=\"%d\">\n<source>%s</source>\n<document_content>%s</document_content>\n</document>"
            index
            source
            content)))