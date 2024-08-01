package ca.jrvs.apps.jdbc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class QuoteHttpHelperTest {

  private QuoteHttpHelper quoteHttpHelper;

  private OkHttpClient mockClient;

  private Call mockCall;

  private Response mockResponse;

  private ResponseBody mockResponseBody;

  @org.junit.Before
  public void setUp() throws Exception {
      quoteHttpHelper = new QuoteHttpHelper();
      mockClient = mock(OkHttpClient.class);
      mockCall = mock(Call.class);
      mockResponse = mock(Response.class);
      mockResponseBody = mock(ResponseBody.class);

  }

  @org.junit.Test
  public void fetchQuoteInfo() throws IOException, ParseException {
    String symbol = "AAPL";
    String jsonResponse = "{\"Global Quote\":{\"01. symbol\":\"AAPL\",\"02. open\":\"216.9600\",\"03. high\":\"219.3000\",\"04. low\":\"215.7500\",\"05. price\":\"218.2400\",\"06. volume\":\"36311778\",\"07. latest trading day\":\"2024-07-29\",\"08. previous close\":\"217.9600\",\"09. change\":\"0.2800\",\"10. change percent\":\"0.1285%\"}}";

    when(mockResponse.isSuccessful()).thenReturn(true);
    when(mockResponse.body()).thenReturn(mockResponseBody);
    when(mockResponseBody.string()).thenReturn(jsonResponse);
    when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
    when(mockCall.execute()).thenReturn(mockResponse);

    quoteHttpHelper.setApiKey("81d6d60d8fmsh196633f8e7c4c1cp1d6eccjsn074b80ad1373");
    quoteHttpHelper.setClient(mockClient);
    Quote quote = quoteHttpHelper.fetchQuoteInfo(symbol);

    // setting up the date for assert
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//    Date expectedDate = dateFormat.parse("2024-07-29");
//    System.out.println("expected date: "+expectedDate);


    assertEquals("AAPL", quote.getTicker());
    assertEquals(216.96, quote.getOpen(), 0.01);
    assertEquals(219.30, quote.getHigh(), 0.01);
    assertEquals(215.7500, quote.getLow(), 0.01);
    assertEquals(218.24, quote.getPrice(), 0.01);
    assertEquals(36311778, quote.getVolume());
//    assertEquals(expectedDate, quote.getLatestTradingDay());
    assertEquals(217.96, quote.getPreviousClose(), 0.01);
    assertEquals(0.28, quote.getChange(), 0.01);
    assertEquals("0.1285%", quote.getChangePercent());
    assertNotNull(quote.getTimestamp());
  }


  //    {
//      "Global Quote": {
//      "01. symbol": "AAPL",
//          "02. open": "216.9600",
//          "03. high": "219.3000",
//          "04. low": "215.7500",
//          "05. price": "218.2400",
//          "06. volume": "36311778",
//          "07. latest trading day": "2024-07-29",
//          "08. previous close": "217.9600",
//          "09. change": "0.2800",
//          "10. change percent": "0.1285%"
//    }
//    }
}