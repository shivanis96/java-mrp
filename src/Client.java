import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Client {

    //Main Method:- called when running the class file.
    public static void main(String[] args) {
        System.out.println("Add a port number:");
        Scanner scan = new Scanner(System.in);
        int port = scan.nextInt();

        //Portnumber:- number of the port we wish to connect on.
        int portNumber = 15882;
        //ServerIP:- IP address of the server.
        String serverIP = "localhost";

        if (port > 2048) {
            portNumber = port;
        } else {
            System.err.println("Port number too low, defaulting to 15882");
        }

        try {
            //Create a new socket for communication
            Socket soc = new Socket(serverIP, portNumber);

            // create new instance of the client writer thread, intialise it and start it running
//            ClientReader clientRead = new ClientReader(soc);
//            Thread clientReadThread = new Thread(clientRead);
//
//            clientReadThread.start();

            // create new instance of the client writer thread, intialise it and start it running
            ClientWriter clientWrite = new ClientWriter(soc);
            Thread clientWriteThread = new Thread(clientWrite);
            clientWriteThread.start();


        } catch (Exception except) {
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.out.println("Error --> " + except.getMessage());
        }
    }
}

//This thread is responcible for writing messages
class ClientWriter implements Runnable {
    Socket cwSocket = null;
    DataOutputStream dataOut = null;
    DataInputStream serverStream = null;



    public ClientWriter(Socket outputSoc) {
        cwSocket = outputSoc;

    }

    public void run() {
        Random rand = new Random();
//    	int start = rand.nextInt(cwSocket.getLocalPort());


        try {
            //Create the outputstream to send data through
            DataOutputStream dataOut = new DataOutputStream(cwSocket.getOutputStream());
            //
            Scanner console = new Scanner(System.in);
            DataInputStream serverStream = new DataInputStream(cwSocket.getInputStream());
            System.out.println("Client writer running");
            //Read hat comes in from server and splits all the words with a space
            String[] successMessage = serverStream.readUTF().split(" ");
            //Checks first postion equals 220
            if (successMessage[0].equals("220")) {
                System.out.println("Do you want to 1. Sign up and login or 2. Login? 3.QUIT? Please choose a number");
                int input = console.nextInt();
                boolean check = false;

                while (!check) {
                    switch (input) {
                        case 1:
                            dataOut.writeUTF("signup");
                            // Sign up
                            String message = serverStream.readUTF();
                            System.out.println(message);
                            Scanner sc = new Scanner(System.in);
                            String name = sc.nextLine();
                            dataOut.writeUTF(name);
                            message = serverStream.readUTF();
                            System.out.println(message);
                            String username = sc.nextLine();
                            dataOut.writeUTF(username);
                            message = serverStream.readUTF();
                            System.out.println(message);
                            String password = sc.nextLine();
                            Hashing hash = new Hashing(password);
                            String hashedPW = hash.getHashed();
                            dataOut.writeUTF(hashedPW);
                            message = serverStream.readUTF();
                            System.out.println(message);
                            if (message.contains("Success in signing up")){
                               System.out.println("Now creating your keys");
                               RSAwithDigitalMessage RSAE = new RSAwithDigitalMessage();
                               KeyPair pair = RSAE.generateKeyPair();
                               RSAE.SaveKeyPair(pair,username);
                               PublicKey pubKey = pair.getPublic();
                               byte[] bytes = pubKey.getEncoded();
                               System.out.println(Arrays.toString(bytes));
                               dataOut.writeInt(bytes.length);
                               dataOut.write(bytes);



                                break;
                            }
                            break;
                        case 2:
//                            SignIn;
                                message = serverStream.readUTF();
                                System.out.println(message);
                            break;
                        case 3:
//                            quit;
                            break;
                        default:
                            System.out.println("That is not a vaid number.Do you want to 1. Sign up or 2. Sign in? 3. quit;Please choose a number");
                            input = console.nextInt();
                            break;
                    }

                }


            }

            System.out.println("Sending HELO message");
            dataOut.writeUTF("HELO " + cwSocket.getInetAddress().getHostAddress() + ":" + cwSocket.getPort());

            System.out.println("Enter QUIT to close the connection");
            dataOut.writeUTF("QUIT");


        } catch (Exception except) {
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.out.println("Error in Writer--> " + except.getMessage());
        }


    }
}

