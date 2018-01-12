import java.sql.*;
import java.util.*;

public  class DB_AddMessages {
    private String rcptto = null;
    private String mailfrom = null;
    private String subject = null;
    private String body = null;
    private String sign = null;


    public DB_AddMessages(String rcptto, String mailfrom,String subject, String body, String sign){
          rcptto = rcptto;
          mailfrom = mailfrom;
          subject = subject;
          body = body;
          sign = sign;
    }
    public synchronized boolean addMessage() {
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
            Calendar calendar = Calendar.getInstance();
            java.sql.Date ourJavaDateObject = new java.sql.Date(calendar.getTime().getTime());
            java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

            stmt= c.prepareStatement("INSERT INTO MESSAGES (RCPTTO, MAILFROM,DATE, TIME ,SUBJECT, BODY, SIGN)" + "VALUES ('" + rcptto + "','" + mailfrom + "',?,?,'" +subject +  "','"+body+ "','" + sign + "');");
            stmt.setDate(1,ourJavaDateObject);
            stmt.setTimestamp(2,ourJavaTimestampObject);
            stmt.executeUpdate();

            stmt.close();
            c.commit();
            c.close();
            System.out.println("Records created successfully");
            return true;
        } catch (SQLException se) {
            //Handle errors for JDBC
            String error = se.getMessage();
            System.out.println(error);
            se.printStackTrace();
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
            e.printStackTrace();
            return false;
        }

    }


}
