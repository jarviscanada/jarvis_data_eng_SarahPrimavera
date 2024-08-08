package ca.jrvs.apps.jdbc;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {

  private QuoteService quoteService;
  private PositionService positionService;
  public Scanner scanner;

  public void initClient() throws SQLException {
    scanner = new Scanner(System.in);
    System.out.println("Welcome to the stock quote app!");
    mainMenuOptions();


  }

  public void mainMenuOptions() throws SQLException {
    int option = 5;
    while (option > 4){
      System.out.println("What would you like to do?");
      System.out.println("1: Check out a stock \n2: Sell my stocks \n3: Buy some stocks \n4: Quit");
      option = scanner.nextInt();
    }
    switch(option){
      case 1:
        checkStockPrice();
        break;
      case 2:
        sellStock();
        break;
      case 3:
        buyStock();
        break;
      case 4:
        System.out.println("See you next time!");
        break;
    }
  }

  public Quote ValidateTickerAndReturnQuote() throws SQLException {
    boolean validTicker = false;
    Optional<Quote> quoteData = Optional.empty();
    while(!validTicker){
      System.out.println("Which stock are you interested in? Make sure you enter a valid ticker ex: IBM");
      scanner.nextLine();
      String ticker = scanner.nextLine();
      quoteData = quoteService.fetchQuoteDataFromAPI(ticker);
      if (quoteData.isPresent()){
        validTicker = true;
      }
    }
    return quoteData.get();
  }

  public void checkStockPrice() throws SQLException {
    Quote quote = ValidateTickerAndReturnQuote();
    System.out.println(quote.getTicker()+" stock:");
    System.out.println("Price"+": "+quote.getPrice());
    System.out.println("High"+": "+quote.getHigh());
    System.out.println("Low"+": "+quote.getLow());
    System.out.println("Volume"+": "+quote.getVolume());
    System.out.println("Previous close"+": "+quote.getPreviousClose());
    System.out.println("Change"+": "+quote.getChange());
    System.out.println("Change Percent"+": "+quote.getChangePercent());
    mainMenuOptions();
  }

  public void sellStock() throws SQLException {
    Quote quote = ValidateTickerAndReturnQuote();
    System.out.println(quote.getTicker()+" stock:");
    System.out.println("Price"+": "+quote.getPrice());
    System.out.println("High"+": "+quote.getHigh());
    System.out.println("Low"+": "+quote.getLow());
    System.out.println("Volume"+": "+quote.getVolume());
    System.out.println("Previous close"+": "+quote.getPreviousClose());
    System.out.println("Change"+": "+quote.getChange());
    System.out.println("Change Percent"+": "+quote.getChangePercent());
    System.out.println("Are you sure you want to sell? yes/no");
    String answer = scanner.nextLine();
    if(answer.equals("yes")){
      positionService.sell(quote.getTicker());
      mainMenuOptions();
    }else{
      mainMenuOptions();
    }
  }

  public void buyStock() throws SQLException {
    Quote quote = ValidateTickerAndReturnQuote();
    System.out.println(quote.getTicker()+" stock:");
    System.out.println("Price"+": "+quote.getPrice());
    System.out.println("High"+": "+quote.getHigh());
    System.out.println("Low"+": "+quote.getLow());
    System.out.println("Volume"+": "+quote.getVolume());
    System.out.println("Previous close"+": "+quote.getPreviousClose());
    System.out.println("Change"+": "+quote.getChange());
    System.out.println("Change Percent"+": "+quote.getChangePercent());
    System.out.println("Are you sure you want to buy? yes/no");
    String answer = scanner.nextLine();
    if(answer.equals("yes")){
      boolean validNumOfShares = false;
      int numOfShares = 0;
      while(!validNumOfShares){
        System.out.println("How many shares of "+quote.getTicker()+" would you like to buy? Make sure you enter a whole number ex: 1");
        numOfShares = scanner.nextInt();
        if (numOfShares > 0){
          validNumOfShares = true;
        }
      }
      positionService.buy(quote.getTicker(), numOfShares, (quote.getPrice()*numOfShares));
      mainMenuOptions();
    }else{
      mainMenuOptions();
    }
  }


  public QuoteService getQuoteService() {
    return quoteService;
  }

  public void setQuoteService(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  public PositionService getPositionService() {
    return positionService;
  }

  public void setPositionService(PositionService positionService) {
    this.positionService = positionService;
  }
}
