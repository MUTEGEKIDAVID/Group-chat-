
package group.chatting.application;
import java.net.*;
import java.io.*;

public class Server {
    
    //this server socket listens in for incoming connections and creatins a socket object for communicating with them
    private ServerSocket serversocket;
    
    //constructor to set up our server socket
    public Server(ServerSocket serversocket){
        this.serversocket=serversocket;
    }
    
    //method start server that will be responsible for keeping the server running
    public void StartServer(){
        try{
            //while the serverSocket is not closed we wait for a client to connect
            while(!serversocket.isClosed()){
               Socket socket = serversocket.accept();//this is a blocking method do our server will be kept here till a connection is established
            //socket object is returned which can be used to connect to a client
            System.out.println("A new client has connected");
            
            //object to handle the affairs of the connected client
            ClientHandler clienthandler;
                clienthandler = new ClientHandler(socket);
            
             //when a class implements runnable. the instances of the class are each implemented by a different thread
                    Thread thread= new Thread(clienthandler);
                    thread.start();
            }
            
        }catch(IOException e){
           
            
        }
    }
    //this method is to close our server socket. used if we wantto close our server spcket when an error occurs
    public void CloseServerSocket(){
        try{
            if(serversocket!=null){
                serversocket.close();
            }
        
        }catch(IOException e){
             e.printStackTrace();
        }
        
    }
    
    public static void main(String args[]) throws IOException{
        ServerSocket serverSocket= new ServerSocket(1234);// server listens to all clients trying to connecr to that port
        
        Server server= new Server(serverSocket);
        server.StartServer();
    }
    
}
