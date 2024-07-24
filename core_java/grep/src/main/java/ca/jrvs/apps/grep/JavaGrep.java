package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {

  /**
   * processing the grep algorithm
   * @throws IOException
   */
  void process() throws IOException;

  /**
   * traverses directory given and returns all files
   * @param rootDir
   * @return
   */
  List<File> listFiles(String rootDir);

  /**
   * reads the input file and returns all the lines
   * @param inputFile
   * @return
   */
  List<String> readLines(File inputFile);

  /**
   * checks if a line matches the regex pattern
   * @param line
   * @return
   */
  boolean containsPattern(String line);

  /**
   * writes lines to a file
   * @param lines
   * @throws IOException
   */
  void writeToFile(List<String> lines) throws IOException;

  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outFile);


}
