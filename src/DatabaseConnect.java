import java.sql.Connection;
import java.sql.DriverManager;
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
            c= DriverManager.getConnection("jdbc:postgresql://localhost:5432/mrdb", "shivani","password");
            System.out.println("Opened database successfully");
            //Execute a query
            stmt = c.createStatement();
            String sql = "CREATE TABLE USERS"+
                    "(ID INT PRIMARY KEY  NOT NULL,"+
                    "NAME TEXT NOT NULL,"+
                    "EMAIL VARCHAR(255) NOT NULL,"+
                    "PASSWORD VARCHAR(20))";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
}
