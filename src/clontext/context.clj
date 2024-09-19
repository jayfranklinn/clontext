(ns clontext.context
  (:require [clojure.string :as str]
            [clontext.file :as file]
            [clontext.gitignore :as gitignore]))

(defn generate-context
  "Generate context from the given project directory"
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