import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class Client {

    //Main Method:- called when running the class file.
    public static void main(String[] args){
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

        try{
            //Create a new socket for communication
            Socket soc = new Socket(serverIP,portNumber);

            // create new instance of the client writer thread, intialise it and start it running
//            ClientReader clientRead = new ClientReader(soc);
//            Thread clientReadThread = new Thread(clientRead);
//
//            clientReadThread.start();

            // create new instance of the client writer thread, intialise it and start it running
            ClientWriter clientWrite = new ClientWriter(soc);
            Thread clientWriteThread = new Thread(clientWrite);
            clientWriteThread.start();



        }
        catch (Exception except){
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.out.println("Error --> " + except.getMessage());
        }
    }
}

//This thread is responcible for writing messages
class ClientWriter implements Runnable
{
    Socket cwSocket = null;


    public ClientWriter (Socket outputSoc){
        cwSocket = outputSoc;

    }
    public String serverResponse(DataOutputStream o, 	DataInputStream i, DataInputStream console, String command,String stmp) {
        //error messages response codes
        try {
            String quit = "";
            String reset = "";
            String error = "";
            String message = i.readUTF();
            System.out.println(message);
            String [] response =	message.split(" ");
            if(response[0].equals("250")||response[0].equals("354")||response[0].equals("start")) {
                System.out.println(command);
                String reply = console.readLine();
                System.out.println(reply);

                switch(reply) {
                    case "RSET":reset = "RESET";
                        break;
                    case "QUIT":quit = "QUIT";
                        break;
                    default:		System.out.println(stmp +" " + reply);
                        o.writeUTF(stmp +" " +  reply);
                        break;
                }

            }else {
                System.out.println( "error" + message);
                error = "error";
            }

            if(reset == "RESET") {
                return reset;
            }else if(quit == "QUIT") {
                return quit;
            } else if( error=="error") {
                System.out.println("error");
                System.out.println(message);
                return error;
            } else {
                return null;
            }
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }
    public void run() {
        Random rand = new Random();
//    	int start = rand.nextInt(cwSocket.getLocalPort());


        try {
            //Create the outputstream to send data through
            DataOutputStream dataOut = new DataOutputStream(cwSocket.getOutputStream());
            //
            DataInputStream console = new DataInputStream(System.in);
            DataInputStream serverStream = new DataInputStream(cwSocket.getInputStream());
            System.out.println("Client writer running");
            //Read hat comes in from server and splits all the words with a space
            String[] successMessage = serverStream.readUTF().split(" ");
            //Checks first postion equals 220
            if (successMessage[0].equals("220")) {
                System.out.println("Sending HELO message");
                dataOut.writeUTF("HELO " + cwSocket.getInetAddress().getHostAddress() + ":" + cwSocket.getPort());
            }


            if (entry == "RESET") {
                System.out.println("Resetting..");
                dataOut.writeUTF("RESET");
                continue;
            } else if (entry == "QUIT") {
                System.out.println("quitting...");
                dataOut.writeUTF("QUIT");
                break;
            } else if (entry == "error") {
                System.out.println("Error occured... restarting");
                dataOut.writeUTF(entry);
                continue;
            }

            System.out.println("Enter QUIT to close the connection");
            dataOut.writeUTF("QUIT");
            break;




        }
        catch (Exception except){
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.out.println("Error in Writer--> " + except.getMessage());
        }


    }
}

