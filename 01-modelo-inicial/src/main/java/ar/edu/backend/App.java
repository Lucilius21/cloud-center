package ar.edu.backend;

import java.io.IOException;
import java.nio.file.Path;

// App elige el archivo, ParserServidores interpreta su formato y Servidor construye el objeto.

public class App {
    public static void main(String[] args) throws IOException {

        // ORIGINAL:
        Path archivo = Path.of(args.length == 0 ? "datos/datos.csv" : args[0]);

        /*
        // CONSIGNA 8:
        // Cambiar solamente el archivo por defecto.
        // Si recibe argumento, mantiene la ruta recibida.

        Path archivo = Path.of(
                args.length == 0 ? "datos/datos_nuevo.csv" : args[0]
        );
        */

        ResultadoParseo resultado = new ParserServidores().leer(archivo);
        Cloud colector = new Cloud(resultado.getServidores());

        System.out.println(resultado.informe());
        resultado.getDescartes().forEach(System.out::println);
        resultado.getErrores().forEach(System.out::println);
        System.out.println(colector.panorama());

        /*
        // CONSIGNA 8:
        // Mostrar cantidad y costo por nivel de riesgo.

        System.out.println("Cantidad por nivel de riesgo: "
                + colector.cantidadPorNivelRiesgo());

        System.out.println("Costo por nivel de riesgo: "
                + colector.costosPorNivelRiesgo());
        */
    }
}