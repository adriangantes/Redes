package es.udc.redes.tutorial.copy;
import java.io.*;

public class Copy {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Uso: java es.udc.redes.tutorial.copy.Copy <fichero origen> <fichero destino>");
            System.exit(1);
        }

        String origen = args[0];
        String destino = args[1];

        try (InputStream inputStream = new FileInputStream(origen);
             OutputStream outputStream = new FileOutputStream(destino)) {

            byte[] buffer = new byte[4096];
            int byte_lec;

            while ((byte_lec = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byte_lec);
            }

            System.out.println("Copia completada.");
        } catch (IOException e) {
            System.err.println("Error al copiar el fichero: " + e.getMessage());
        }
    }
}
