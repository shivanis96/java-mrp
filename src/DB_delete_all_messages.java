import java.sql.*;
import java.util.*;
import java.lang.*;

public  class DB_delete_all_messages {

    private String username;

    public DB_delete_all_messages(String usernameInput){
        username = usernameInput;

    }
    public synchronized String deleteUsers() {
        Connection c = null;
        PreparedStatement stmt = null;
        try {
            //Register JDBC Driver
            Class.forName("org.postgresql.Driver");
            //Open a connection
            System.out.println("Connecting to a database...");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mrdb", "shivani", "password");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            //Execute a query
            stmt = c.prepareStatement("DELETE FROM messages WHERE rcptto='"+username+"'");
            stmt.executeUpdate();

            stmt.close();
            c.commit();
            c.close();

            return "All messages have successfully been deleted.";

        } catch (SQLException se) {
            //Handle errors for JDBC
            String error = se.getMessage();
            System.out.println(error);
            System.out.println("Records deleted unsuccessfully");
            return "Database Error: Messages have not been deleted.";

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
            System.out.println("Records deleted unsuccessfully");
            return "Error:  ";
        }

    }


}

