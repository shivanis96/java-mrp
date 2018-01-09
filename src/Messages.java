import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class Messages {
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
            String sql = "CREATE TABLE MESSAGES" +
                    "(id SERIAL PRIMARY KEY  NOT NULL," +
                    "RCPTTO VARCHAR(255) NOT NULL," +
                    "MAILFROM VARCHAR(255) UNIQUE NOT NULL," +
                    "DATE DATE NOT NULL," +
                    "TIME TIME NOT NULL," +
                    "SUBJECT VARCHAR(255) NOT NULL," +
                    "MESSAGE VARCHAR(255) NOT NULL)";

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
