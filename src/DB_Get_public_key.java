import java.sql.*;

public  class DB_Get_public_key {
    private String username = null;
    private String pk = null;


    public DB_Get_public_key(String usernameInput){
        username = usernameInput;

    }
    public synchronized String getkey() {
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
            stmt = c.prepareStatement("SELECT public_key FROM users WHERE username='"+username+"'");
            ResultSet rs = stmt.executeQuery();
            boolean empty = true;
            while (rs.next()){
                pk = rs.getString("public_key");

            }

            rs.close();
            stmt.close();
            c.commit();
            c.close();
           return pk;

        } catch (SQLException se) {
            //Handle errors for JDBC
            String error = se.getMessage();
            System.out.println(error);
            System.out.println("Records created unsuccessfully");
            return "error";

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
            System.out.println("Records created unsuccessfully");
            return "error";
        }

    }


}

