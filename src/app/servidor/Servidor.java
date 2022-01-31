/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.servidor;

import app.bean.FileMesage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jocelio
 */
public class Servidor {
    private ServerSocket serverSocket;
    private Socket socket;
    private static Map<String, ObjectOutputStream> streamMap = new HashMap<String, ObjectOutputStream>();

    public Servidor() {
        try {
            serverSocket = new ServerSocket(5555);
            System.out.println("Server online - Aguardando arquivos...");
            
            while(true){
                socket = serverSocket.accept();
                
                new Thread (new ListenerSocket(socket)).start();
            } 
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    private static class ListenerSocket implements Runnable {
          private ObjectOutputStream outputStream;
          private ObjectInputStream inputStream;
        
          public ListenerSocket(Socket socket) throws IOException {
              this.outputStream = new ObjectOutputStream(socket.getOutputStream());
              this.inputStream = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            FileMesage mesage = null;
            
              try {
                  while((mesage= (FileMesage) inputStream.readObject())!= null){
                     streamMap.put(mesage.getCliente(), outputStream);
                     
                     if(mesage.getFile()!= null){
                       for(Map.Entry<String, ObjectOutputStream> kv : streamMap.entrySet()){
                           if(!mesage.getCliente().equals(kv.getKey())){
                                kv.getValue().writeObject(mesage);
                            }                    
                        }
                     }
                  } 
              
              } catch (IOException ex) {
                streamMap.remove(mesage.getCliente());
                  System.out.println(mesage.getCliente() + " Desconectou");
              } catch (ClassNotFoundException ex) {
                  Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
              }
            
        }
        
        public static void main(String[] args) {
            new Servidor();
        }
        
    }
    
    
}
