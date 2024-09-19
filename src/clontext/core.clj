(ns clontext.core
  "Core namespace for the Clontext application.
   This namespace contains the main entry point and CLI option parsing."
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clontext.context :as context])
  (:gen-class))

(def cli-options
  "CLI options for the Clontext application."
  [["-h" "--help" "Show help"]
   ["-o" "--output FILE" "Output file name"
    :default "context.txt"]])

(defn- exit
  "Exit the program with the given status code and message."
  [status msg]
  (println msg)
  (System/exit status))

(defn -main
  "The main entry point for the Clontext application.
   Parses command-line arguments and generates the context file.

   Usage: clontext [OPTIONS] PROJECT_DIRECTORY

   Options:
     -h, --help             Show help
     -o, --output FILE      Output file name (default: context.txt)"
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 summary)
      errors (exit 1 (str "Error: " (clojure.string/join "\n" errors)))
      (empty? arguments) (exit 1 "Error: Please provide a project directory")
      :else 
      (let [project-dir (first arguments)
            output-file (:output options)]
        (try
          (context/generate-context project-dir output-file)
          (exit 0 (str "Context file generated successfully: " output-file))
          (catch Exception e
            (exit 1 (str "Error generating context: " (.getMessage e)))))))))