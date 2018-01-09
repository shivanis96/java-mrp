import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class signUp {

    String name;
    String surname;
    String username;
    String password;
    Scanner sc = new Scanner(System.in );

    public signUp (String nameInput, String surnameInput, String usernameInput, String passwordInput){
        name = nameInput;
        surname = surnameInput;
        username = usernameInput;
        password = passwordInput;

    }

}

public void signup() {
            System.out.println("Name: ");
            String name = sc.nextLine();
            System.out.println("Surname: ");
            String surname = sc.nextLine();
            System.out.println("Username: ");
            String username = sc.nextLine();
            System.out.println("Create a password: ");
            String password = sc.nextLine();

            String hashedPassword;
            Hashing hash =new Hashing(password);
            hashedPassword= Hashing. getHashed();

            AddUsers AU = new AddUsers(name, username, password);
            AU.add();

            "Welcome you have been added"

        }