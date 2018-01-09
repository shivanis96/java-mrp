import java.sql.*;

class Search {

    public static void main (String[] args) {
        try {
            String url = "org.postgresql.Driver";
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mrdb", "shivani", "password");
            Statement stmt = conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT id FROM Messages WHERE id = 1");
            while ( rs.next() ) {
                String lastName = rs.getString("id");
                System.out.println("need to fix this");
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception ");
            System.err.println(e.getMessage());
        }
    }
}