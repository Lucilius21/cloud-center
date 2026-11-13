package ar.edu.backend;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
//import java.util.Comparator;
//import java.util.Optional;

public class Cloud {
    private final List<Servidor> servidores;

    public Cloud(List<Servidor> servidores) {
        this.servidores = List.copyOf(servidores);
    }

    public List<Servidor> filtrar(Predicate<Servidor> criterio) {
        return this.servidores.stream().filter(criterio).toList();
    }

    /**
     * Metodo que retorna el costo total del riesgo operativo por los servidores que
     * se estan procesando
     */
    public int total() {
        return this.servidores.stream().mapToInt(Servidor::costoRiesgoOperativo).sum();
    }

    /**
     * Metodo que retorna el Map donde por cada region a la que pertence el servidor
     * 
     * @return
     */
    public Map<String, Integer> totalesPorRegion() {
        return this.servidores.stream()
                .collect(Collectors.groupingBy(Servidor::getRegion,
                        Collectors.summingInt(Servidor::costoRiesgoOperativo)));
    }

    // CONSIGNA 7:
    // Incorporar cantidad de servidores por nivel de riesgo.
    // CONSERVAR la información anterior.
    
    public String panorama() {
        String reporte = "Total: %d | Por Region: %s | indice menores a 50: %d";
        return reporte.formatted(this.total(), new TreeMap<>(this.totalesPorRegion()),
                this.filtrar(s -> s.getIndiceCarga() < 50).size()); // ,this.cantidadPorNivelRiesgo());
    }

        /*
    // CONSIGNA 4:
    // Cantidad de servidores agrupados por nivel de riesgo.
    // Retorna Map<String, Long>.
    
    public Map<String, Long> cantidadPorNivelRiesgo() {
        return this.servidores.stream()
                .collect(Collectors.groupingBy(
                        Servidor::getNivelRiesgo,
                        Collectors.counting()
                ));
    }
    */

        /*
    // CONSIGNA 5:
    // Costo total de riesgo operativo agrupado por nivel de riesgo.

    public Map<String, Integer> costosPorNivelRiesgo() {
        return this.servidores.stream()
                .collect(Collectors.groupingBy(
                        Servidor::getNivelRiesgo,
                        Collectors.summingInt(Servidor::costoRiesgoOperativo)
                ));
    }
    */

        /*
    // CONSIGNA 6:
    // Obtener todos los servidores de riesgo CRITICO.

    public List<Servidor> servidoresCriticos() {
        return this.servidores.stream()
                .filter(s -> s.getNivelRiesgo().equals("CRITICO"))
                .toList();
    }
    */

    /*
    // ANOTACIONES EXTRAS - BÚSQUEDAS SIMPLES

    // Servidor con mayor índice de carga
    public Optional<Servidor> masCargado() {
        return servidores.stream()
                .max(Comparator.comparingInt(Servidor::getIndiceCarga));
    }

    // Servidor con menor índice de carga
    public Optional<Servidor> menosCargado() {
        return servidores.stream()
                .min(Comparator.comparingInt(Servidor::getIndiceCarga));
    }

    // Servidor con mayor costo de riesgo operativo
    public Optional<Servidor> masCaro() {
        return servidores.stream()
                .max(Comparator.comparingInt(Servidor::costoRiesgoOperativo));
    }

    // Servidor con menor costo de riesgo operativo
    public Optional<Servidor> masBarato() {
        return servidores.stream()
                .min(Comparator.comparingInt(Servidor::costoRiesgoOperativo));
    }

    // Buscar servidor por ID
    public Optional<Servidor> buscarPorId(String id) {
        return servidores.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }


    // ANOTACIONES EXTRAS - AGRUPACIONES

    // Cantidad de servidores por región
    public Map<String, Long> cantidadPorRegion() {
        return servidores.stream()
                .collect(Collectors.groupingBy(
                        Servidor::getRegion,
                        Collectors.counting()));
    }

    // Promedio de índice de carga por región
    public Map<String, Double> promedioCargaPorRegion() {
        return servidores.stream()
                .collect(Collectors.groupingBy(
                        Servidor::getRegion,
                        Collectors.averagingInt(Servidor::getIndiceCarga)));
    }

    // Servidor más cargado por región
    public Map<String, Servidor> masCargadoPorRegion() {
        return servidores.stream()
                .collect(Collectors.groupingBy(
                        Servidor::getRegion,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(
                                        Comparator.comparingInt(Servidor::getIndiceCarga)),
                                Optional::get)));
    }


    // ANOTACIONES EXTRAS - BÚSQUEDAS SOBRE AGRUPACIONES

    // Región que más gasta
    public Optional<String> regionQueMasGasta() {
        return totalesPorRegion().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    // Región que menos gasta
    public Optional<String> regionQueMenosGasta() {
        return totalesPorRegion().entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    // Región con más servidores
    public Optional<String> regionConMasServidores() {
        return cantidadPorRegion().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    // Región con mayor promedio de carga
    public Optional<String> regionConMayorPromedio() {
        return promedioCargaPorRegion().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }


    // ANOTACIONES EXTRAS - FILTROS Y SUBCONJUNTOS

    // Servidores con carga menor a X
    public List<Servidor> conCargaMenorA(int umbral) {
        return filtrar(s -> s.getIndiceCarga() < umbral);
    }

    // Servidores con carga mayor a X
    public List<Servidor> conCargaMayorA(int umbral) {
        return filtrar(s -> s.getIndiceCarga() > umbral);
    }

    // Top N servidores por costo descendente
    public List<Servidor> topNPorCosto(int n) {
        return servidores.stream()
                .sorted(Comparator.comparingInt(Servidor::costoRiesgoOperativo).reversed())
                .limit(n)
                .toList();
    }

    // Ordenados por índice de carga ascendente
    public List<Servidor> ordenadosPorCarga() {
        return servidores.stream()
                .sorted(Comparator.comparingInt(Servidor::getIndiceCarga))
                .toList();
    }


    // ANOTACIONES EXTRAS - AGREGACIONES GLOBALES

    // Promedio de costo
    public double promedioCosto() {
        return servidores.stream()
                .mapToInt(Servidor::costoRiesgoOperativo)
                .average()
                .orElse(0);
    }

    // Cantidad total de servidores
    public int cantidad() {
        return servidores.size();
    }

    // Costo total de los que cumplen un criterio
    public int totalDe(Predicate<Servidor> criterio) {
        return servidores.stream()
                .filter(criterio)
                .mapToInt(Servidor::costoRiesgoOperativo)
                .sum();
    }


    // ANOTACIONES EXTRAS - PREGUNTAS BOOLEANAS

    // ¿Existe algún servidor con costo > X?
    public boolean existeConCostoMayorA(int umbral) {
        return servidores.stream()
                .anyMatch(s -> s.costoRiesgoOperativo() > umbral);
    }

    // ¿Todos los servidores tienen carga < X?
    public boolean todosConCargaMenorA(int umbral) {
        return servidores.stream()
                .allMatch(s -> s.getIndiceCarga() < umbral);
    }

    // ¿Ningún servidor tiene carga > X?
    public boolean ningunoConCargaMayorA(int umbral) {
        return servidores.stream()
                .noneMatch(s -> s.getIndiceCarga() > umbral);
    }
    */
}