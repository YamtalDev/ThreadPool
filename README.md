# ThreadPool
A Java thread pool library for concurrent task execution.

**Project Overview:** 
This repository contains a Java thread pool library that simplifies concurrent task execution. It provides a fixed-size thread pool that efficiently manages worker threads and executes submitted tasks concurrently.

**Features:**
- Fixed-size thread pool with adjustable thread number.
- Graceful termination of worker threads.
- Thread-safe task submission and execution (lambda expressions).
- tasks priority configuration.

## Usage

To utilize this thread pool library in your Java project, follow these steps:

### Prerequisites

Before you begin, ensure you have the following prerequisites installed on your system:

- **Java (JDK):** If you don't have Java installed, you can download and install it from the official website:
  - [Download Java for Windows](https://www.oracle.com/java/technologies/javase-downloads.html) (Windows)
  - [Download Java for macOS](https://www.oracle.com/java/technologies/javase-downloads.html) (macOS)
  - [Download Java for Linux](https://openjdk.java.net/install/) (Linux)

- **Maven:** If you don't have Maven installed, you can download and install it from the official website:
  - [Download Maven](https://maven.apache.org/download.cgi)

### Installation

1. **Clone or Download the Repository:**
   You can clone this Git repository or download it as a ZIP file to your local machine.

``` shell
git clone https://github.com/YamtalDev/ThreadPool.git

```

2. **Configuring Java Version (Optional):** If you need to configure a specific 
Java version different from Java 17 used in this project, you can do so by modifying the pom.xml file. 


3. **Build the Project:**
Navigate to the project directory and build it using Maven.
* This command will download project dependencies, compile the code, and create a JAR file.

``` shell
cd ThreadPool
mvn clean install

```

4. **Run the Tests:**
You can run the project's tests to ensure everything is working as expected.
* This command will execute the test suite, and you should see the test results in your console.


``` shell
mvn test

```

**License:**
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

**Contact:**
For questions or issues, feel free to [create an issue](https://github.com/YamtalDev/ThreadPool/issues) or contact the project maintainer.

