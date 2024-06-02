package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {

        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }

        DatagramSocket socket = null;

        try {

            int port = Integer.parseInt(argv[0]);
            socket = new DatagramSocket(port); // Create a server socket


            socket.setSoTimeout(300000); // Set maximum timeout to 300 secs

            while (true) {
                // Prepare datagram for reception
                byte array[] = new byte[1024];
                DatagramPacket packet = new DatagramPacket(array, array.length);
                
                // Receive the message
                socket.receive(packet);
                String message= new String(packet.getData(), 0, packet.getLength());
                System.out.println("SERVER: Received " + message + " from "
                        + packet.getAddress().toString() + ":" + packet.getPort());
                
                // Prepare datagram to send response
                DatagramPacket answer = new DatagramPacket(message.getBytes(),
                        message.getBytes().length, packet.getAddress(), packet.getPort());

                // Send response
                socket.send(answer);
                System.out.println("SERVER: Sending " + message + " to "
                        + packet.getAddress().toString() + ":" + packet.getPort());
            }
          
        // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the socket
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
