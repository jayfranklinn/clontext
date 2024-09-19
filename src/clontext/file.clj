(ns clontext.file
  "Namespace for file-related operations in the Clontext application."
  (:require [clojure.java.io :as io]))

(defn list-files
  "Recursively list all files in the given directory.
   
   Parameters:
   - dir: String path to the directory to be scanned

   Returns:
   A sequence of java.io.File objects representing all files (not directories) 
   found in the given directory and its subdirectories.

   Throws:
   - java.io.FileNotFoundException if the directory doesn't exist
   - java.lang.IllegalArgumentException if the path is not a directory"
  [dir]
  (let [dir-file (io/file dir)]
    (when-not (.exists dir-file)
      (throw (java.io.FileNotFoundException. (str "Directory not found: " dir))))
    (when-not (.isDirectory dir-file)
      (throw (IllegalArgumentException. (str "Not a directory: " dir))))
    
    (->> (file-seq dir-file)
         (filter #(.isFile %)))))

(defn read-file-content
  "Read the content of a file.
   
   Parameters:
   - file: java.io.File object representing the file to be read

   Returns:
   A string containing the file's content.

   Throws:
   - java.io.IOException if there's an error reading the file"
  [file]
  (try
    (slurp file)
    (catch Exception e
      (throw (java.io.IOException. (str "Error reading file " (.getName file) ": " (.getMessage e)))))))

(defn file-content
  "Generate the content for a file in the desired XML-like format.
   
   Parameters:
   - file: java.io.File object representing the file
   - index: Integer representing the file's index in the context

   Returns:
   A string containing the file's content wrapped in XML-like tags, including
   the file's name as the source and its content.

   Throws:
   - Any exception that might occur during file reading"
  [file index]
  (let [source (.getName file)
        content (read-file-content file)]
    (format "<document index=\"%d\">\n<source>%s</source>\n<document_content>%s</document_content>\n</document>"
            index
            source
            content)))