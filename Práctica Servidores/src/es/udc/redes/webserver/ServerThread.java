package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;


public class ServerThread extends Thread {

    private final Socket socket;
    private static final String serverName = "7FServer";

    public ServerThread(Socket s) {
        this.socket = s;
    }

    private static void sendResponse (PrintWriter output, String state, Date sendDate, File file, Socket socket, String method, Boolean send) throws IOException{
        output.println(state);
        output.println("Date: " + sendDate.toString());
        output.println("Server: " + serverName);
        output.println("Last-Modified: " + file.lastModified());
        output.println("Content-Length: " + file.length());
        output.println("Content-Type: " + Files.probeContentType(file.toPath()));
        output.println("");

        if(method.equals("GET") || send)
            sendFile(socket, file);
    }

    private static void sendFile(Socket socket, File file) throws IOException {

        FileInputStream Istream = new FileInputStream(file);
        OutputStream os = socket.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = Istream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        Istream.close();
        os.close();
    }

    public void run() {

        try {

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            String clientMessage = input.readLine();
            String [] headers;
            HashMap<String, String> headersContent = new HashMap<>();
            String method, fileName;
            Date sendDate = new Date();

            if(clientMessage != null){
                headers = clientMessage.split(" ");
                method = headers[0];
                fileName = headers[1];
            }else{
                throw new RuntimeException();
            }

            File file = new File("p1-files" + File.separator + fileName);

            while((clientMessage = input.readLine()) != null){

                if(clientMessage.isEmpty()) {
                    break;
                }

                headers = clientMessage.split(":");
                headersContent.put(headers[0].trim(), headers[1].trim());
            }

            if(!method.equals("GET") && !method.equals("HEAD")){

                file = new File("p1-files/error400.html");

                sendResponse(output, "HTTP/1.0 400 Bad Request", sendDate, file, socket, method, true);

            }else if (!file.exists()){

                file = new File("p1-files/error404.html");

                sendResponse(output, "HTTP/1.0 404 Not Found", sendDate, file, socket, method, false);

            }else if(headersContent.get("If-Modified-Since") != null){

                Date lastModificationDate = new Date(file.lastModified());
                Date modifiedSince = new Date(Long.parseLong(headersContent.get("If-Modified-Since")));
                int result = modifiedSince.compareTo(lastModificationDate);

                if(result >= 0){
                    output.println("HTTP/1.0 304 Not modified");
                    output.println(serverName);
                    output.println("");
                }else{
                    sendResponse(output, "HTTP/1.0 200 OK", sendDate, file, socket, method, false);
                }

            }else{
                sendResponse(output, "HTTP/1.0 200 OK", sendDate, file, socket, method, false);
            }

            input.close();
            output.close();

        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
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