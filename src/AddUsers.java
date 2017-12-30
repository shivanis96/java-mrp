import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AddUsers {
    public static void main(String args[]){
        Connection c = null;
        Statement stmt = null;
        Scanner sc = new Scanner(System.in);
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
            System.out.println("Username: ");
            String userName= sc.nextLine();
            System.out.println("Email: ");
            String userEmail= sc.nextLine();
            System.out.println("Password: ");
            String userPassword = sc.nextLine();
            String sql = "INSERT INTO USERS (NAME, EMAIL, PASSWORD)" +"VALUES ('" + userName + "','" + userEmail+ "','" + userPassword+"');";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
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
