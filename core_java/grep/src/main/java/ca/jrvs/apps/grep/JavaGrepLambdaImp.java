package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImplementation implements
    LambdaStreamExec {

  public static void main(String[] args) {
    if (args.length !=3){
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      javaGrepLambdaImp.process();
    } catch (Exception e){
      javaGrepLambdaImp.logger.error("Error: Unable to process because ", e);
    }
  }

  @Override
  public void process() throws IOException {
    List<String> linesToWrite = listFiles(getRootPath()).stream()
        .flatMap(file -> {
          return readLines(file).stream();
        })
        .filter(this::containsPattern)
        .collect(Collectors.toList());
    writeToFile(linesToWrite);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    File dir = new File(rootDir);
    return Arrays.stream(dir.listFiles()).collect(Collectors.toList());
  }

  @Override
  public List<String> readLines(File inputFile) {
    try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
      return reader.lines().collect(Collectors.toList());
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean containsPattern(String line) {
    return line.matches(getRegex());
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    String outFile = getOutFile();
    Files.write(Paths.get(outFile), lines);
  }

  @Override
  public Stream<String> createStrStream(String... strings) {
    return Arrays.stream(strings);
  }

  @Override
  public Stream<String> toUpperCase(String... strings) {
    return createStrStream(strings).map(String :: toUpperCase);
  }

  @Override
  public Stream<String> filter(Stream<String> stringStream, String pattern) {
    return stringStream.filter(s -> s.contains(pattern));
  }

  @Override
  public IntStream createIntStream(int[] arr) {
    return IntStream.of(arr);
  }

  @Override
  public <E> List<E> toList(Stream<E> stream) {
    return stream.collect(Collectors.toList());
  }

  @Override
  public List<Integer> toList(IntStream intStream) {
    return intStream.boxed().collect(Collectors.toList());
  }

  @Override
  public IntStream createIntStream(int start, int end) {
    return IntStream.rangeClosed(start, end);
  }

  @Override
  public DoubleStream squareRootIntStream(IntStream intStream) {
    return intStream.mapToDouble(Math::sqrt);
  }

  @Override
  public IntStream getOdd(IntStream intStream) {
    return intStream.filter(i -> i % 2 !=0);
  }

  @Override
  public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
    return message -> {
      String formattedMessage = prefix + " " + message + " " + suffix;
      System.out.println(formattedMessage);
    };
  }

  @Override
  public void printMessages(String[] messages, Consumer<String> printer) {
    for (String message : messages){
      printer.accept(message);
    }
  }

  @Override
  public void printOdd(IntStream intStream, Consumer<String> printer) {
    intStream.filter(i -> i % 2 !=0).forEach(i -> printer.accept(String.valueOf(i)));
  }

  @Override
  public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
    return ints.flatMap(List::stream).map(n -> n * n);
  }
}
