package URLShortener.DBAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAccess {

    private static Connection makeConnection(){
        Connection DBConnect = null;
        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            DBConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/urls", "root", "URLShortener");
        } catch (SQLException e) {
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
            return DBConnect;
        }
        return DBConnect;
    }

    public static String getURL(String hostURL){
        //access database and retrieve redirectURL
        String URL = "";
        try {
            Connection connection = makeConnection();

            // MySQL Select Query Tutorial
            String getQueryStatement = "SELECT redirectURL FROM globalurls WHERE hostedURL = '"+ hostURL+"';";

            PreparedStatement prepareStat = connection.prepareStatement(getQueryStatement);

            // Execute the Query, and get a java ResultSet
            ResultSet rs = prepareStat.executeQuery();

            // Let's iterate through the java ResultSet
            while (rs.next()) {
                URL = rs.getString("redirectURL");
            }

        } catch (SQLException e) {
            return "";
        }
        return URL;
    }

    public static String setNewURL(String hostedURL, String redirectURL){
        //make new record in database with hostedURL and redirectURL
        try {
            Connection connection = makeConnection();

            String insertQueryStatement = "INSERT  INTO  globalurls  VALUES  (?,?)";

            PreparedStatement prepareStat = connection.prepareStatement(insertQueryStatement);
            prepareStat.setString(1, hostedURL);
            prepareStat.setString(2, redirectURL);

            // execute insert SQL statement
            prepareStat.executeUpdate();
        } catch (SQLException e) {
            return "false";
        }
        //return success
        return "true";
    }

    public static boolean checkConflicts(String hostedURL){
        //check if hosted URL already exists in database

        //return true if conflict
        return false;
    }
}
