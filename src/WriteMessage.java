import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
public class WriteMessage {
    public static void main(String args[]){
        Connection c = null;
        Statement stmt = null;
        Scanner scan = new Scanner(System.in);
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
            System.out.println("Send To: ");
            String rcpt= scan.nextLine();
            System.out.println("Mail From: ");
            String mailFrom= scan.nextLine();
            System.out.println("Subject: ");
            String subject= scan.nextLine();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43

            System.out.println("Write Message:");
            String message= scan.nextLine();



            String sql = "INSERT INTO MESSAGES (RCPTTO, MAILFROM,DATE,SUBJECT, MESSAGE )" +"VALUES ('" + rcpt + "','" + mailFrom+ "','" + date+ "','" +subject+ "','" +message+ "');";
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

