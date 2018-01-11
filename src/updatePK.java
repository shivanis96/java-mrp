import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public  class updatePK {
    private String username = null;
    private byte[]  pk = null;

    public updatePK (String usernameInput, byte[] pubKey ){
        username = usernameInput;
        pk = pubKey;
    }
    public synchronized boolean updatepk() {
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
            stmt = c.prepareStatement("UPDATE users SET public_key=? WHERE username='"+username+"'");
            stmt.setBytes(1,pk);
            stmt.executeUpdate();
            stmt = c.prepareStatement("SELECT * FROM users WHERE username='"+username+"'");
            ResultSet rs = stmt.executeQuery(  );
            while (rs.next()){
                byte[] pk = rs.getBytes("public_key");
                String  name = rs.getString("name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                System.out.println( Arrays.toString(pk) );
                System.out.println( "NAME = " + name );
                System.out.println( "USERNAME = " + username );
                System.out.println( "PASSWORD = " + password );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
            System.out.println("Records created successfully");
            return true;
        } catch (SQLException se) {
            //Handle errors for JDBC
            String error = se.getMessage();
            System.out.println(error);
            System.out.println("Records created unsuccessfully");
            return false;


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
            System.out.println("Records created unsuccessfully");
            return false;
        }

    }


}

