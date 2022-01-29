/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.cliente;

import app.bean.FileMesage;
import com.sun.corba.se.impl.io.OutputStreamHook;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author Jocelio
 */
public class Cliente {
    private Socket socket;
    private ObjectOutputStream outputStream;

    public Cliente() throws IOException {
        this.socket = new Socket("localhost", 5555);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        
        
        new Thread ( new ListenerSocket(socket)).start();
        
        menu();
        
    }

    private void menu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Digite seu nome: ");
        String nome = scanner.nextLine();
        
        this.outputStream.writeObject(new FileMesage(nome));
        
        int op = 0;
        
        while(op != 1){
             System.out.println("1- Sair || 2- Enviar ");
             op = scanner.nextInt();
             
             if(op == 2){
                 send(nome);
             }else if(op != 1){
                 System.exit(0);
             }
        }
    }

    private void send(String nome) throws IOException {
       FileMesage filem = new FileMesage();
       
        JFileChooser fileChooser = new JFileChooser();
        
        int opt = fileChooser.showOpenDialog(null);
        
        if(opt == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            
            this.outputStream.writeObject(new FileMesage(nome, file));
        }
    }

    private static class ListenerSocket implements Runnable {
           private ObjectInputStream inputStream;
           
        public ListenerSocket(Socket socket) throws IOException {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            FileMesage mesage = null;
           
             try {
                    while((mesage= (FileMesage) inputStream.readObject())!= null){
                        System.out.println("\n Voce recebeu um arquivo de: " + mesage.getCliente());
                        System.out.println("O arquivo é " + mesage.getFile().getName());
                        System.out.println("Salvo em: c:/sinc/" + mesage.getFile().getName());
                    
                       salvar(mesage);
                       
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
              }

 
        private void salvar(FileMesage mesage) {
               try {
                   
                  // long time = System.currentTimeMillis();
                  //+ time + "-"
                   
                  
                   FileInputStream fileInputStream = new FileInputStream(mesage.getFile());
                   FileOutputStream fileOutputStream = new FileOutputStream("c:\\sinc\\" + mesage.getFile().getName());
                   
                   FileChannel fin = fileInputStream.getChannel();
                   FileChannel fout = fileOutputStream.getChannel();
                   
                   try {
                       long size = fin.size();
                       
                       fin.transferTo(0, size, fout);
                       
                   } catch (IOException ex) {
                       Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   
                   
               } catch (FileNotFoundException ex) {
                   Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
               }
        
        }
       
    
    }
    
        public static void main(String[] args) {
    try {
            new Cliente();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
    
    

