package skincare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnecDB {
    public Connection getConnect() {
        String host = "localhost"; // Ganti dengan host Anda
        String port = "3306"; // Port default MySQL
        String database = "skincare"; // Ganti dengan nama database Anda
        String username = "root"; // Ganti dengan username MySQL Anda
        String password = ""; // Ganti dengan password MySQL Anda
        
        Connection conn = null;
        
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Open a connection
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        
        return conn;
    }
}