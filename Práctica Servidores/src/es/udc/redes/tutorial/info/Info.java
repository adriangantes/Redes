package es.udc.redes.tutorial.info;
import java.io.File;
import java.util.Date;

public class Info {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Uso: java es.udc.redes.tutorial.info.Info <ruta relativa>");
            System.exit(1);
        }

        String ruta = args[0];
        String rutaAbsoluta = System.getProperty("user.dir") + File.separator + ruta;

        File archivo = new File(rutaAbsoluta);

        if (!archivo.exists()) {
            System.out.println("El archivo no existe.");
            System.exit(1);
        }

        String nombre = archivo.getName();
        Date ultimaModificacion =  new Date(archivo.lastModified());

        System.out.println("Tamaño: " + archivo.length() + " bytes");
        System.out.println("Fecha de última modificación: " + ultimaModificacion);
        System.out.println("Nombre: " + nombre);
        System.out.println("Extensión: " + obtenerExtension(nombre));
        System.out.println("Tipo de archivo: " + obtenerTipoArchivo(archivo));
        System.out.println("Ruta absoluta: " + rutaAbsoluta);
    }

    private static String obtenerExtension(String nombreArchivo) {

        int puntoIndex = nombreArchivo.lastIndexOf(".");

        if (puntoIndex == -1) {
            return "";
        }

        return nombreArchivo.substring(puntoIndex + 1);
    }

    private static String obtenerTipoArchivo(File archivo) {

        if (archivo.isDirectory()) {
            return "directory";
        } else if (archivo.isFile()) {

            String extension = obtenerExtension(archivo.getName()).toLowerCase();

            if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") ||
                    extension.equals("gif") || extension.equals("bmp")) {
                return "image";
            } else if (extension.equals("txt") || extension.equals("csv") || extension.equals("xml")) {
                return "text";
            } else {
                return "unknown";
            }

        } else {
            return "unknown";
        }
    }
}
