import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class testServer 
{
    public static void main(String[] args) throws Exception 
    {
        int portNum = 9999;
        try 
        {
            ServerSocket serverSocket = new ServerSocket(portNum);
            System.out.println("Wating Connection");
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) 
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected!!");
                pool.execute(new Client(clientSocket));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static class Client implements Runnable
    {
        private Socket socket;

        Client(Socket socket)
        {
            this.socket = socket;
        }
        
        @Override
        public void run()
        {
            BufferedReader fromClient = null;
            DataOutputStream toClient = null;
            try 
            {
                fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                toClient = new DataOutputStream(socket.getOutputStream());

                while (true) 
                {
                    String clientMessage = fromClient.readLine();
                    if (clientMessage == null) 
                    {
                        break;
                    }
                    System.out.println(clientMessage);

                    String response = calculate(clientMessage);

                    // Send msg to Client
                    toClient.writeBytes(response + '\n');
                }
            }            
            catch (IOException e) 
            {
                e.printStackTrace();
            } 
            finally 
            {
                try 
                {
                    socket.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }
    private static String calculate(String clientMessage) 
    {
        String[] argument = clientMessage.split(" ");
        int ErrCode=0;

        if (argument.length == 3) 
        {
            try 
            {
                double num1 = Double.parseDouble(argument[0]);
                double num2 = Double.parseDouble(argument[2]);

                switch (argument[1]) 
                {
                    case "+":
                        return Integer.toString(ErrCode)+" " + (num1 + num2);
                    case "-":
                        return Integer.toString(ErrCode)+" "+ (num1 - num2);
                    case "*":
                        return Integer.toString(ErrCode)+" " + (num1 * num2);
                    case "/":
                        // 0으로 나누는 경우 처리
                        if (num2 != 0) 
                        {
                            return Integer.toString(ErrCode)+" " + (num1 / num2);
                        }
                        else 
                        {
                            ErrCode=100;
                            return Integer.toString(ErrCode) + " No_result";
                        }
                    default:
                        ErrCode=101;
                        return Integer.toString(ErrCode) + " No_result";
                }
            } 
            catch (NumberFormatException e) 
            {
                ErrCode=102;
                return Integer.toString(ErrCode) + " No_result";
            }
        }
        else if (argument.length>3) 
        {
            ErrCode=103;
            return Integer.toString(ErrCode) + " No_result";
        }
        else
        {
            ErrCode=104;
            return Integer.toString(ErrCode) + " No_result";
        }
    }
}