package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Timestamp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONObject;
import okhttp3.ResponseBody;

public class QuoteHttpHelper {

  private String apiKey;
//  "81d6d60d8fmsh196633f8e7c4c1cp1d6eccjsn074b80ad1373"
  private OkHttpClient client;

  /**
   * Fetch latest quote data from Alpha Vantage endpoint
   * @param symbol
   * @return Quote with latest data
   * @throws IllegalArgumentException - if no data was found for the given symbol
   */
  public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException{
    Request request = new Request.Builder()
        .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+symbol+"&datatype=json")
        .header("X-RapidAPI-Key", apiKey)
        .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
        .get()
        .build();
    try(ResponseBody responseBody = client.newCall(request).execute().body()) {
      JSONObject jsonObject = new JSONObject(responseBody.string());
      JSONObject globalQuote = jsonObject.getJSONObject("Global Quote");
      ObjectMapper objectMapper = new ObjectMapper();
      Quote quote = objectMapper.readValue(globalQuote.toString(), Quote.class);
      quote.setTimestamp(new Timestamp(System.currentTimeMillis()));
      return quote;
    }catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public OkHttpClient getClient() {
    return client;
  }

  public void setClient(OkHttpClient client) {
    this.client = client;
  }
}
