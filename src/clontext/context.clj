(ns clontext.context
  (:require [clojure.string :as str]
            [clontext.file :as file]
            [clontext.gitignore :as gitignore]))

(defn generate-context
  "Generate context from the given project directory"
  [project-dir output-file]
  (let [ignore-patterns (gitignore/read-gitignore project-dir)
        files (file/list-files project-dir)
        context (str/join "\n\n"
                  (for [file files
                        :when (not (gitignore/should-ignore? file project-dir ignore-patterns))]
                    (file/file-content file)))]
    (spit output-file context)
    (println "Context file generated:" output-file)))