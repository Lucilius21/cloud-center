package ar.edu.backend;

// Para consigna 1:
// Se requiere incorporar el concepto de nivel de riesgo a los servidores.
// La nueva versión del archivo CSV agrega una sexta columna:
// id,region,porcentajeCPU,usoMemoria,estado,nivelRiesgo
//
// El campo nivelRiesgo puede tomar los valores:
// NORMAL
// CRITICO
//
// MODIFICAR EN ESTE ORDEN:
// 1. SERVIDOR
// 2. desdeCampos()
// 3. ParserServidores
//
// IMPORTANTE:
// No modificar ni eliminar los elementos originales que puedan necesitar
// los tests existentes.


//Para consigna 2:
/*
Para los servidores cuyo nivelRiesgo sea NORMAL, se mantienen las reglas actuales.

Para los servidores cuyo nivelRiesgo sea CRITICO, además deberá cumplirse:

porcentaje de CPU mayor o igual a 60.
uso de memoria mayor o igual a 8000.

Si no se cumple alguna condición, la fila deberá registrarse como inválida y el procesamiento deberá continuar con la siguiente fila.

Los servidores ESTABLE continúan siendo descartados.
*/

public class Servidor {
    private final String id;
    private final String region;
    private final int indiceCarga;

    // NUEVO ATRIBUTO PARA LA CONSIGNA 1:
    // private final String nivelRiesgo;

    private static final int INGRESO_PROMEDIO = 1000;

    public Servidor(String id, String region, int indiceCarga) {
        // ESTE CONSTRUCTOR SE MANTIENE PARA NO ROMPER LOS TESTS EXISTENTES

        if (id == null || id.isBlank() || region == null || region.isBlank())
            throw new IllegalArgumentException("El id y la region son obligatorios");

        if (indiceCarga < 1 || indiceCarga > 70)
            throw new IllegalArgumentException("El indice de carga es un valor fuera del rango permitido");

        this.id = id;
        this.region = region;
        this.indiceCarga = indiceCarga;
    }

    /*
    // NUEVO CONSTRUCTOR PARA SERVIDORES QUE VIENEN DEL CSV DE 6 COLUMNAS

    public Servidor(String id, String region, int indiceCarga, String nivelRiesgo) {

        if (id == null || id.isBlank() || region == null || region.isBlank())
            throw new IllegalArgumentException("El id y la region son obligatorios");

        if (indiceCarga < 1 || indiceCarga > 70)
            throw new IllegalArgumentException("El indice de carga es un valor fuera del rango permitido");

        this.id = id;
        this.region = region;
        this.indiceCarga = indiceCarga;

        this.nivelRiesgo = nivelRiesgo;
    }
    */

    public static Servidor desdeCampos(String[] tokens) { //BORRAR ESTO SI PIDE ACEPTAR DOS CABECERAS
        if (tokens.length != 5)
            throw new IllegalArgumentException("se esperan 5 columnas");

        int usoCpu = Integer.parseInt(tokens[2]);
        int usoMemoria = Integer.parseInt(tokens[3]);

        if (usoCpu < 1 || usoCpu > 100)
            throw new IllegalArgumentException("uso del cpu esta fuera de rango");

        if (usoMemoria < 1 || usoMemoria > 24576)
            throw new IllegalArgumentException("uso de memoria esta fuera de rango");

        int indiceCarga = (int) ((usoCpu * 0.7) + (usoMemoria * 0.3));

        return new Servidor(tokens[0], tokens[1], indiceCarga);
    }

    /*
    // VERSION MODIFICADA PARA LA CONSIGNA 1 + 2

    public static Servidor desdeCampos(String[] tokens) {

        if (tokens.length != 5 && tokens.length != 6)
            throw new IllegalArgumentException("se esperan 5 o 6 columnas");

        int usoCpu = Integer.parseInt(tokens[2]);
        int usoMemoria = Integer.parseInt(tokens[3]);

        if (usoCpu < 1 || usoCpu > 100)
            throw new IllegalArgumentException("uso del cpu esta fuera de rango");

        if (usoMemoria < 1 || usoMemoria > 24576)
            throw new IllegalArgumentException("uso de memoria esta fuera de rango");

        String nivelRiesgo = "NORMAL";

        if (tokens.length == 6)
            nivelRiesgo = tokens[5];

        // CONSIGNA 2:
        // Solo CRITICO tiene restricciones adicionales.

        if (nivelRiesgo.equals("CRITICO")) {

            if (usoCpu < 60)
                throw new IllegalArgumentException("CPU insuficiente para riesgo CRITICO");

            if (usoMemoria < 8000)
                throw new IllegalArgumentException("memoria insuficiente para riesgo CRITICO");
        }

        int indiceCarga = (int) ((usoCpu * 0.7) + (usoMemoria * 0.3));

        return new Servidor(tokens[0], tokens[1], indiceCarga, nivelRiesgo);
    }
    */

    public String getId() {
        return id;
    }

    public String getRegion() {
        return region;
    }

    public int getIndiceCarga() {
        return indiceCarga;
    }

    public int costoRiesgoOperativo() {
        int v = (INGRESO_PROMEDIO * this.indiceCarga) / 100;
        return v;
    }

    /*
    // NUEVO GETTER PARA LA CONSIGNA 1

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }
    */
}