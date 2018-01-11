import java.sql.*;
import java.util.*;

public  class DB_get_all_users {


    private List<HashMap<String,byte[]>> temp = new ArrayList<HashMap<String,byte[]>>();

    public DB_get_all_users(){

    }
    public synchronized List<HashMap<String,byte[]>> getUsers() {
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
            stmt = c.prepareStatement("SELECT * FROM USERS");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                //temp.add(rs.getString("username"));
                HashMap<String,byte[]> hMap = new HashMap<String,byte[]>();
                byte[] pk = rs.getBytes("public_key");
                String name = rs.getString("username");
                hMap.put(name, pk);
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

