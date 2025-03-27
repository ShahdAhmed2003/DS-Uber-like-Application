package ServerSide;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server
{
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }
    public void startServer()
    {
         try{
            while(!serverSocket.isClosed()){
               Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
                System.out.println("Thread #" + thread.getId());
            }

         }catch(IOException e){
             e.printStackTrace();
         }
    }
    public void closeServerSocket()
    {
        try{
            serverSocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        try{
            ServerSocket serverSocket = new ServerSocket(8080);
            Server server = new Server(serverSocket);
            server.startServer();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}