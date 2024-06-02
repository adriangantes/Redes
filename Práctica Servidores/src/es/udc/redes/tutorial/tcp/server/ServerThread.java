package es.udc.redes.tutorial.tcp.server;
import java.net.*;
import java.io.*;

/** Thread that processes an echo server connection. */

public class ServerThread extends Thread {

  private Socket socket;

  public ServerThread(Socket s) {
    // Store the socket s
    this.socket = s;
  }

  public void run() {

    try {
      // Set the input channel
      BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      // Set the output channel
      PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

      // Receive the message from the client
      String clientMessage = input.readLine();
      System.out.println("SERVER: Received " + clientMessage + " from " + socket.getRemoteSocketAddress());

      // Sent the echo message to the client
      System.out.println("SERVER: Sending " + clientMessage + " to " + socket.getRemoteSocketAddress());
      output.println(clientMessage);

      // Close the streams
      input.close();
      output.close();

    // Uncomment next catch clause after implementing the logic
    } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());

    } finally {

	  // Close the socket
      try {
        // Close the socket
        if (socket != null && !socket.isClosed()) {
          socket.close();
        }
      } catch (IOException e) {
        System.err.println("Error closing socket: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }
}
