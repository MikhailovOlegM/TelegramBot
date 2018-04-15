package service;

import app.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DatabaseConnector {

  private static final String url = "jdbc:mysql://localhost:3306/botTelegram?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false";
  private static final String user = "root";
  private static final String password = "76222377";

  private static Connection con;
  private static Statement stmt;
  private static ResultSet rs;

  public void addUser(User user){
    String query = "INSERT INTO user (id, userName, status) \n" +
        " VALUES (\'"+user.getId()+"\', \'"+user.getUserName()+"\', 'false');";

    try {
      con = DriverManager.getConnection(url, this.user, password);
      stmt = con.createStatement();

      stmt.executeUpdate(query);
    } catch (SQLException ignored) {
    }
  }

  public void updateUser(Boolean status, String id){
    String query = "UPDATE user SET status = \'"+status+"\' WHERE id =\'"+id+"\';";

    try {
      con = DriverManager.getConnection(url, this.user, password);
      stmt = con.createStatement();

      stmt.executeUpdate(query);
    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    }
  }

  public List<User> getUsers() {
    List<User> userList = new ArrayList<>();
    String query = "SELECT id, userName, status FROM user";

    try {
      con = DriverManager.getConnection(url, user, password);
      stmt = con.createStatement();

      rs = stmt.executeQuery(query);

      while (rs.next()) {
        userList.add(new User(rs.getString(1), rs.getString(2), Boolean.valueOf(rs.getString(3))));
      }

    } catch (SQLException sqlEx) {
      sqlEx.printStackTrace();
    }

    return userList;
  }

}
