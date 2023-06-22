
package group.chatting.application;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {
    
    private Socket socket;
    private BufferedWriter bufferedwriter;  //read data from a client
    private BufferedReader bufferedreader;  // for sending data to a client
    private String username;
    
    public Client(Socket socket,String username ){
        try{
          this.socket=socket;  
          this.bufferedwriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//we are wrapping our btye stream with a character stream because we want to send characters
          this.bufferedreader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
          
          this.username=username;
        
        }catch(IOException e){
            closeEverything(socket,bufferedwriter,bufferedreader);
        }
    }
    
    public void sendMessage() throws IOException{
        try{
            bufferedwriter.write(username);
            bufferedwriter.newLine();
            bufferedwriter.flush();
            
            Scanner s= new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend= s.nextLine();
                bufferedwriter.write(username+ ":"+" "+ messageToSend);
                bufferedwriter.newLine();
                bufferedwriter.flush();
            }
            
        }catch(IOException e){
            closeEverything(socket,bufferedwriter,bufferedreader);
        }
    }
    
    //method to listeen to broadcasted messages
    public void listenForMessages(){
        new Thread(new Runnable(){
            
            @Override
            public void run(){
                String messageFromGroupChat;
                
                while(socket.isConnected()){
                try{
                    messageFromGroupChat= bufferedreader.readLine();
                    System.out.println(messageFromGroupChat);
                }catch(IOException e){
                    
            closeEverything(socket,bufferedwriter,bufferedreader);
            
           }
                
                }
            }
        
        }).start();
        
    }
      public void closeEverything( Socket socket, BufferedWriter bufferedwriter, BufferedReader bufferedreader){
    
    
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
      
     public static void main(String args[]) throws IOException{
         Scanner s= new Scanner(System.in);
         
         System.out.println("Enter your username for the group chat: ");
         
         String username= s.nextLine();
         
         Socket socket = new Socket("localhost",1234);
         Client client = new Client(socket,username);
         client.listenForMessages();
         client.sendMessage();
         
         
     } 
   
    
}
