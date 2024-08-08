package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseConnectionManager {

  private final String url;
  private final Properties properties;
  private static final Logger loggerInfo = LogManager.getLogger("StockLogInfo");

  public DatabaseConnectionManager(String host, String databaseName,
      String username, String password){
    this.url = "jdbc:postgresql://"+host+"/"+databaseName;
    this.properties = new Properties();
    this.properties.setProperty("user", username);
    this.properties.setProperty("password", password);
  }

  public Connection getConnection() throws SQLException {
    loggerInfo.info("getting connection from driver manager");
    return DriverManager.getConnection(this.url, this.properties);
  }

}
