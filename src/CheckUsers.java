import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
public class CheckUsers {
    public static void main(String args[]){
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
            ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS WHERE EMAIL = 'shivaniisamazing@hotmail.com';" );
            while (rs.next()){
                int id = rs.getInt("id");
                String  name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                System.out.println( "id = " + id );
                System.out.println( "NAME = " + name );
                System.out.println( "EMAIL = " + email );
                System.out.println( "PASSWORD = " + password );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
            System.out.println("Records created successfully");
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
