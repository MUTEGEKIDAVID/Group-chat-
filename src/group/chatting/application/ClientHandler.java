
package group.chatting.application;

import java.io.*;
import java.net.*;
import java.util.*;


public class ClientHandler implements Runnable {
    
    // this array contains all our clienthandlers hence keeps track of all our clients.can be used to implement broadcast
    public static ArrayList<ClientHandler> clienthandlers = new ArrayList<>();
    
    private Socket socket;
    private BufferedWriter bufferedwriter;  //read data from a client
    private BufferedReader bufferedreader;  // for sending data to a client
    private String Clientusername;
    
    
    public ClientHandler(Socket socket){
        try{
          this.socket=socket;  
          this.bufferedwriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//we are wrapping our btye stream with a character stream because we want to send characters
          this.bufferedreader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
          
          this.Clientusername=bufferedreader.readLine();
        
         
          //add client to the array list so that they can recieve messages
          clienthandlers.add(this);
          
          broadcastMessage("SERVER: " + Clientusername +" "+ "has entered the chat");
        }catch(IOException e){
            closeEverything(socket,bufferedwriter,bufferedreader);
        }
        
    }
    
    
    
    
    @Override
    public void run(){
        String messageFromClient;
        
        while(socket.isConnected()){
            try{
              messageFromClient=bufferedreader.readLine();// server thread will hault here till we recieve a message from the client
              broadcastMessage(messageFromClient);
                      
                      
            }catch(IOException e){
                closeEverything(socket,bufferedwriter,bufferedreader);
                break;
            }
        }
    
    }
    public void broadcastMessage(String messageToSend){
        
        for(ClientHandler clienthandler: clienthandlers){
        try{
            if(!clienthandler.Clientusername.equals(Clientusername)){
                clienthandler.bufferedwriter.write(messageToSend);
                clienthandler.bufferedwriter.newLine();
                clienthandler.bufferedwriter.flush();
            }
        }catch(IOException e){
        closeEverything(socket,bufferedwriter,bufferedreader);
        }
        }
        
    }
    
    //method to signal that a user has left a chat
    
    public void  removeClientHandler(){
        clienthandlers.remove(this);
        broadcastMessage("SERVER:" +Clientusername + "has left the chat");
    }
    
    public void closeEverything( Socket socket, BufferedWriter bufferedwriter, BufferedReader bufferedreader){
    removeClientHandler();
    
    try{
        if(bufferedreader != null){
            bufferedreader.close();
        }
         if(bufferedwriter != null){
            bufferedwriter.close();
        }
         if(socket!=null){
             socket.close();
         }
        
    }catch(IOException e){
        e.printStackTrace();
    }
    }

    
}
