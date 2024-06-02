package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) {

        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }

        ServerSocket serverSocket = null;

        try {
            // Create a server socket
            int serverPort = Integer.parseInt(argv[0]);
            serverSocket = new ServerSocket(serverPort);

            // Set a timeout of 300 secs
            serverSocket.setSoTimeout(300000);
            
            while (true) {
                // Wait for connections
                Socket socket = serverSocket.accept();

                // Set the input channel
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Set the output channel
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                // Receive the client message
                String clientMessage = input.readLine();
                System.out.println("SERVER: Received " + clientMessage + " from " + socket.getRemoteSocketAddress());
                
                // Send response to the client
                System.out.println("SERVER: Sending " + clientMessage + " to " + socket.getRemoteSocketAddress());
                output.println(clientMessage);

                // Close the streams
                input.close();
                output.close();
                socket.close();
            }

        // Uncomment next catch clause after implementing the logic            
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();

        } finally {

	        //Close the socket
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }

            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
