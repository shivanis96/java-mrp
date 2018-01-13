import java.sql.*;
import java.util.*;
import java.lang.*;

public  class DB_get_all_messages {

    private List<HashMap<String,Object>> temp = new ArrayList<HashMap<String,Object>>();
    private String username;

    public DB_get_all_messages(String usernameInput){
        username = usernameInput;

    }
    public synchronized List<HashMap<String,Object>> getUsers() {
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
                //temp.addMessage(rs.getString("username"));
                HashMap<String,Object> hMap = new HashMap<String,Object>();
               // hMap.put("id", Integer.toString(rs.getInt("id")));
                hMap.put("id",rs.getString("id"));
                hMap.put("Sender", rs.getString("mailfrom"));
                hMap.put("Date", rs.getString("Date"));
                hMap.put("Subject", rs.getString("subject"));
                hMap.put("Body", rs.getString("body"));
                hMap.put("Sign", rs.getString("sign"));
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

