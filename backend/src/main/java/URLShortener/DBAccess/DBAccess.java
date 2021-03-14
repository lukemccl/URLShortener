package URLShortener.DBAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAccess {
    //Use of prepared statements removes risk of SQL injection

    private static Connection makeConnection(){
        try {
            // Set connection
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/urls", "root", "URLShortener");

        } catch (SQLException e) {
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }

    //retrieve redirectURL
    public static String getURL(String hostURL){

        try {
            Connection connection = makeConnection();

            String getQueryStatement = "SELECT redirectURL FROM globalurls WHERE hostedURL = (?);";

            PreparedStatement prepareStat = connection.prepareStatement(getQueryStatement);
            prepareStat.setString(1, hostURL);

            ResultSet rs = prepareStat.executeQuery();

            //get result from query
            String URL = "";
            while (rs.next()) {
                URL = rs.getString("redirectURL");
            }
            return URL;

        } catch (SQLException e) {
            return "";
        }
    }

    //make new record in database with hostedURL and redirectURL
    public static Boolean setNewURL(String hostedURL, String redirectURL){
        try {
            Connection connection = makeConnection();

            String insertQueryStatement = "INSERT  INTO  globalurls  VALUES  (?,?)";

            PreparedStatement prepareStat = connection.prepareStatement(insertQueryStatement);
            prepareStat.setString(1, hostedURL);
            prepareStat.setString(2, redirectURL);

            prepareStat.executeUpdate();
        } catch (SQLException e) {
            //return failure (if hostedURL is already used)
            return false;
        }
        //return success
        return true;
    }
}
