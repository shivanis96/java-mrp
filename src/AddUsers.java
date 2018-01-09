import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AddUsers {
    private String name = null;
    private String username = null;
    private String password = null;

    public AddUsers (String nameInput, String usernameInput, String passwordInput){
        name = nameInput;
        username = usernameInput;
        password = passwordInput;
    }
    public void add() {
        Connection c = null;
        Statement stmt = null;
        try {
            //Register JDBC Driver
            Class.forName("org.postgresql.Driver");
            //Open a connection
            System.out.println("Connecting to a database...");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mrdb", "shivani", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            //Execute a query
            stmt = c.createStatement();
            String sql = "INSERT INTO USERS (NAME, USERNAME, PASSWORD)" + "VALUES ('" + name + "','" + username + "','" + password + "');";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
            System.out.println("Records created successfully");
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
