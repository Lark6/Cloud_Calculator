import java.io.*;
import java.net.*;

public class Client 
{
    private static final int DEFAULT_PORT = 9999;
    private static final String DEFAULT_IP = "localhost";

    public static void main(String[] args) 
    {
        int port = DEFAULT_PORT;
        String ipAddress = DEFAULT_IP;

        // read IP & port number from config.txt
        try (BufferedReader configFileReader = new BufferedReader(new FileReader("config.txt"))) 
        {
            String configIpAddress = configFileReader.readLine();
            String portStr = configFileReader.readLine();

            if (configIpAddress != null) // IP address is null
            {
                ipAddress = configIpAddress;
            }

            if (portStr != null) //port number is null
            {
                port = Integer.parseInt(portStr);
            }

            System.out.println("Server IP : " + ipAddress + ", Server port : " + port);
        } catch (IOException e) // Using Default
        {
            System.err.println("Using Default");
        }

        try {
            // Connect to Server
            Socket socket = new Socket(InetAddress.getByName(ipAddress), port);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)); // ready to input Sting for Calculation
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true); // ready to send Server
            
            while (true) //Section for Sending Formula
            {
                System.out.print("input formula: ");
                String message = userInput.readLine();

                toServer.println(message); //send to Server

                BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream())); //receive to Server 
                String serverResponse = fromServer.readLine();
                String[] Received_Msg = serverResponse.split(" "); // 

                switch (Integer.parseInt(Received_Msg[0])) 
                {
                    case 0:
                        System.out.println(Received_Msg[1]);
                        break;
                    case 1:
                        System.out.println("ErrCode " + Integer.parseInt(Received_Msg[0])+" : Too many Arguments");
                        break;
                    case 2:
                        System.out.println("ErrCode " + Integer.parseInt(Received_Msg[0])+" : Need more argument");
                        break;
                    case 3:
                        System.out.println("ErrCode " + Integer.parseInt(Received_Msg[0])+" : Devided by Zero");
                        break;
                }

                if ("CLOSE".equals(message)) //if input 'CLOSE' close Client
                {
                    break;
                }
            }

            //Close Socket Stream 
            userInput.close();
            toServer.close();
            socket.close();

        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
