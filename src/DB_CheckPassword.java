import java.sql.*;
import java.util.Arrays;

public  class DB_CheckPassword {
    private String username = null;
    private String password  = null;
    private String dbPassword = null;

    public DB_CheckPassword(String usernameInput, String passwordInput ){
        username = usernameInput;
        password = passwordInput;
    }
    public synchronized String checkPW() {
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
            stmt = c.prepareStatement("SELECT password FROM users WHERE username='"+username+"'");
            ResultSet rs = stmt.executeQuery();
            boolean empty = true;
            while (rs.next()){
                dbPassword = rs.getString("password");
                empty= false;
            }
            if(empty){
                System.out.println("This user doesnt exist");
                return "no_user";
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();
            System.out.println("Checking password");
            System.out.println(password);
            System.out.println(dbPassword);

            if (password.equals(dbPassword)){
                System.out.println("Password exists");
                return "password_ok";
            }
            else{
                System.out.println("Password didn't match");
                return "wrong_password";
            }

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

