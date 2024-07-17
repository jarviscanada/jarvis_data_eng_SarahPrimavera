# Introduction
This Java Grep application replicates the functionality of the Linux `grep` command. Built using Java, IntelliJ, Maven, and Docker, it also leverages lambda expressions to provide efficient text searching capabilities. This project offers a hands-on demonstration of integrating these technologies, showcasing how they can work together to create powerful and flexible apps.

# Quick Start
    pathtoJavaGrepImpfile "regexpattern" "inputfile" "outputfile"
Example:

    JavaGrepImp.java ".*Romeo.*Juliet.*" "data\txt" "out\outfile.txt"

# Implemenation
To implement this Java Grep application, I used IntelliJ IDEA as my development environment and managed dependencies with Maven. I wrote the core functionality in Java, utilizing lambda expressions for efficient and readable code. Docker was used to containerize the application, ensuring it runs consistently across different environments. The app takes input from files, searches for specified patterns, and outputs matching lines, effectively mimicking the Linux grep command. This combination of tools and techniques showcases modern software development practices and containerization for easy deployment.

# Pseudocode
Pseucode for `process` method

    variable linesToWrite
    for singlefile in listedfiles(rootDir)
        for singleline in listedlines(singlefile)
            if containsPattern(singleline)
                linesToWrite.add(singleline)
    writeToFile(linesToWrite)

# Performance Issue
One issue that arises from this program is accumulating all the matching lines in memory before writing them to the output file. If large enough, this can exhaust the memory. A solution to this problem would be to immediately write each matched line to the output file, reducing memory usage and preventing overflow.

# Test
To test this application, I used IntelliJ's run configurations to add my command-line arguments. This allowed me to specify the regex, root path, and output file directly within the IDE. Additionally, I utilized the built-in debugger to identify and resolve issues, ensuring smooth functionality and efficient troubleshooting throughout the development process.

# Deployment
I dockerized my Java Grep application to simplify distribution and ensure consistent runtime environments. By building and running the Docker image, the app can be easily deployed and executed across different systems without compatibility issues.

# Improvements
- Use of configuration file (YAML or JSON) to manage input parameters
- Fix the memory issue by immediately writing to the output file
- Add unit tests to validate functionality