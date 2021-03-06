import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseConnect {
    public static void main(String args[]){
        Connection c = null;
        Statement stmt = null;
        try {
            //Register JDBC Driver
            Class.forName("org.postgresql.Driver");
            //Open a connection
            System.out.println("Connecting to a database...");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mrdb", "shivani", "password");
            System.out.println("Opened database successfully");
            //Execute a query
            stmt = c.createStatement();
            String sql = "CREATE TABLE USERS" +
                    "(id SERIAL PRIMARY KEY  NOT NULL," +
                    "NAME TEXT NOT NULL," +
                    "USERNAME VARCHAR(255) UNIQUE NOT NULL," +
                    "PASSWORD VARCHAR(255)," +
                    "public_key BYTEA )";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }catch (SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
}
