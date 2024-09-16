# Clontext

Clontext is a command-line tool written in Clojure that condenses the structure of a Clojure project into a single file. This file can then be used as context for prompting in various applications.

## Features

- Recursively scans a Clojure project directory
- Generates a single context file containing the content of all project files
- Respects `.gitignore` patterns to exclude unnecessary files
- Configurable output file name

## Installation

Clontext is a Clojure deps.edn project. To use it, you'll need to have Clojure and the Clojure CLI tools installed on your system.

1. Clone the repository:
   ```
   git clone https://github.com/your-username/clontext.git
   cd clontext
   ```

2. Build the project:
   ```
   clojure -T:build uber
   ```

   This will create a standalone JAR file in the `target` directory.

## Usage

To use Clontext, run the following command:

```
java -jar target/clontext.jar [OPTIONS] PROJECT_DIRECTORY
```

Options:
- `-h, --help`: Show help
- `-o, --output FILE`: Specify the output file name (default: "context.txt")

Example:
```
java -jar target/clontext.jar -o my-context.txt /path/to/your/clojure/project
```

This will generate a file named `my-context.txt` containing the context of the specified Clojure project.

## Project Structure

- `src/clontext/core.clj`: Main entry point of the application
- `src/clontext/context.clj`: Logic for generating the context
- `src/clontext/file.clj`: File operations (listing files, reading content)
- `src/clontext/gitignore.clj`: Handling of `.gitignore` patterns

## Development

To run the project during development:

```
clojure -M:run [OPTIONS] PROJECT_DIRECTORY
```

To run tests:

```
clojure -M:test
```

## Building

To build a standalone JAR:

```
clojure -T:build uber
```

This will create a JAR file in the `target` directory.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

[Add your chosen license here]
