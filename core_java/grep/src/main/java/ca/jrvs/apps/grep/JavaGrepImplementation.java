package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JavaGrepImplementation implements JavaGrep{

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    if (args.length !=3){
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    JavaGrepImplementation javaGrepImp = new JavaGrepImplementation();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception e){
      javaGrepImp.logger.error("Error: Unable to process because ", e);
    }


  }

  @Override
  public void process() throws IOException {
    List<String> linesToWrite = new ArrayList<>();
    for (File myfile: listFiles(getRootPath())){
      for (String line: readLines(myfile)){
        if (containsPattern(line)){
          linesToWrite.add(line);
        }
      }
    }
    writeToFile(linesToWrite);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    File dir = new File(rootDir);
    File[] files = dir.listFiles();
    List<File> mylist = Arrays.asList(files);
    return mylist;
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> result = new ArrayList<>();
    try{
      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      while (reader.ready()) {
        result.add(reader.readLine());
      }
      reader.close();
    } catch (Exception e){
      e.getStackTrace();
    }
    return result;
  }

  @Override
  public boolean containsPattern(String line) {
    return line.matches(getRegex());
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    File fileToWrite = new File(getOutFile());
    if (!fileToWrite.exists()){
      fileToWrite.createNewFile();
    }
    BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite));
    for (String line: lines){
      writer.write(line+"\n");
    }
    writer.close();
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}
