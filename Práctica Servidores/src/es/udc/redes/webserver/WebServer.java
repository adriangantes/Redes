package es.udc.redes.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer {

    public static void main(String[] args){

        if(args.length != 1){
            System.err.println("Format: java <path to the java file> port");
            System.exit(-1);
        }

        ServerSocket serverSocket = null;

        try{
            int port = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(300000);

            while(true){
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
            }

        }catch (SocketTimeoutException e){
            System.err.println("Nothing received in 300 secs");
        }catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }finally{

            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            }catch (IOException e){
                System.err.println("Error closing server socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}