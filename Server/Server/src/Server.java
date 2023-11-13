import java.io.*;
import java.net.*;

public class Server 
{


    public static void main(String[] args) 
    {
        int nPort;
        nPort = 9999;
        int MAX_CONNECTIONS=5; //Max connection

 
        try 
        {
            //Create Server Socket & Binding
            ServerSocket serverSocket = new ServerSocket(nPort);

            // Constraint Max Connection
            int connectedClients = 0;
            
            while (true) 
            {
                // Waiting Connection
                Socket clientSocket = serverSocket.accept();

                // Check Max connection
                if (connectedClients < MAX_CONNECTIONS) 
                {
                    System.out.println("Client Connected");

                    // Create & Start Thread for Socket Connection
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    new Thread(clientHandler).start();

                    // increasing Max connection
                    connectedClients++;
                } else 
                {
                    // if over Max connection, close socket
                    System.out.println("Over Max connection");
                    clientSocket.close();
                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable //Thread to Socket Connection
{
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        try {
            // Create Stream for read date from Client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // read formula from Client
            String clientMessage = reader.readLine();
            int Errcode=0; //0: No Err 1: Too many Arguments, 2: Need more argument, 3: Divided by Zero
            int result =0;
            String[] Str = clientMessage.split(" ");

            System.out.println(Str.length);

            if (Str.length ==3) 
            {
                for (int i = 0; i < Str.length; i++) 
                {
                    System.out.println(Str[i]);
                }
                //Complite
                switch (Str[1]) {
                    case "-":
                        result = Integer.parseInt(Str[0])-Integer.parseInt(Str[2]);
                        break;
                    case "+":
                        result = Integer.parseInt(Str[0])+Integer.parseInt(Str[2]);
                        break;
                    case "*":
                        result = Integer.parseInt(Str[0])*Integer.parseInt(Str[2]);
                        break;
                    case "/":
                        if (Integer.parseInt(Str[2])!=0) {
                            result = Integer.parseInt(Str[0])/Integer.parseInt(Str[2]);
                        }else if (Integer.parseInt(Str[2])==0){
                            Errcode=3;
                        }
                        break;
                }


            }else if(Str.length>3)
            {
                Errcode=1;
            }else if(Str.length<3)
            {
                Errcode = 2;
            }
            //문자열로 전환후 클라이언트로 전송
            StringBuffer sb = new StringBuffer();

            // For Debug
            System.out.println(Errcode);
            System.out.println(result);



            sb.append(Integer.toString(Errcode));
            sb.append(" ");
            sb.append(Integer.toString(result));

            writer.println(sb);

            // Close Socket, Stream
            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println(" Protocol Error");
        }
    }
}
