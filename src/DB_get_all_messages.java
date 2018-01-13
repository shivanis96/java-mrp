import java.sql.*;
import java.util.*;
import java.lang.*;

public  class DB_get_all_messages {

    private List<HashMap<Integer,MailTemplate>> temp = new ArrayList<HashMap<Integer,MailTemplate>>();
    private String username;

    public DB_get_all_messages(String usernameInput){
        username = usernameInput;

    }
    public synchronized List<HashMap<Integer,MailTemplate>> getUsers() {
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
            stmt = c.prepareStatement("SELECT * FROM messages WHERE rcptto='"+username+"'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){

                HashMap<Integer,MailTemplate> hMap = new HashMap<Integer, MailTemplate>();
                Integer id = rs.getInt("id");
                String mailfrom =  rs.getString("mailfrom");
                String rcptto = rs.getString("rcptto");
                String datestring = rs.getString("Date");
                String subject = rs.getString("subject");
                String body = rs.getString("body");
                String ds = rs.getString("sign");

                MailTemplate newMail = new MailTemplate(mailfrom,rcptto,subject,body,ds,datestring);
                hMap.put(id,newMail);
                temp.add(hMap);
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();

            return temp;

        } catch (SQLException se) {
            //Handle errors for JDBC
            String error = se.getMessage();
            System.out.println(error);
            System.out.println("Records created unsuccessfully");
            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
            System.out.println("Records created unsuccessfully");
            return temp;
        }

    }


}

