import java.io.*;
import java.net.*;

public class Client {
    // Default Info
    private static String ipAddress = "localhost";
    private static int port = 9999;

    public static void main(String argv[]) throws Exception 
    {
        // read IP & port number from server_info.dat
        try (BufferedReader configFileReader = new BufferedReader(new FileReader("src/server_info.dat"))) 
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
        }catch (IOException e) // Using Default
        {
            System.err.println("Using Default");
        }

        Socket clientSocket = null;
        BufferedReader userInput = null;
        DataOutputStream toServer = null;

        try 
        {
            // Socket connection to Server
            clientSocket = new Socket(ipAddress, port);
            userInput = new BufferedReader(new InputStreamReader(System.in));
            toServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) 
            {
                // Input msg 
                System.out.print("input formula (EX:20 + 10): ");
                String message = userInput.readLine();

                //if input 'CLOSE' close Client
                if ("CLOSE".equals(message)) 
                {
                    break;
                }

                // Send msg to Server
                toServer.writeBytes(message + '\n');

                // receive msg from server
                String serverResponse = inFromServer.readLine();

                // Decoding msg from server
                decodingMsg(serverResponse);
            }
        } catch (IOException e) 
        {
            e.printStackTrace();        
        }finally 
        {
            try 
            {
                if (clientSocket != null)
                {
                    clientSocket.close();
                }         
            } catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }

    private static void decodingMsg(String serverResponse) 
    {
        //deconding msg from Server (Errcode, result
        String[] result = serverResponse.split(" ");

            String errcode = result[0];
            String Answer = result[1];

            // Print Answer if Errcode = 0 else Print Error case
            switch (errcode) 
            {
                case "0":
                    System.out.println("result: " + Answer);
                    break;
                case "100":
                    System.out.println("Div by Zero");
                    break;
                case "101":
                    System.out.println("InValid Operator");
                    break;
                case "102":
                    System.out.println("Invalid Num");
                    break;
                case "103":
                    System.out.println("Too many Argument");
                    break;
                case "104":
                    System.out.println("Less Argument");
                    break;   
            }
        
    }
}