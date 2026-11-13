package ar.edu.backend;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ParserServidoresTest {
    @TempDir
    Path dir;

    @Test
    void archivoProvisto() throws IOException {
        var r = new ParserServidores().leer(Path.of("datos/datos.csv"));
        assertEquals(60, r.getLeidas());
        assertEquals(21, r.getDescartadas());
        assertEquals(9, r.getInvalidas());
        assertEquals(30, r.getServidores().size());
        assertEquals(46, r.getServidores().get(0).getIndiceCarga());
        assertEquals("Leídas: 60 | procesadas: 30 | descartadas: 21 | inválidas: 9 | objetos: 30", r.informe());
        assertTrue(r.getErrores().get(0).startsWith("Linea 7:"));
        assertEquals(r.getLeidas(), r.getProcesadas() + r.getDescartadas() + r.getInvalidas());
    }

    @Test
    void agregadosYTestigos() throws IOException {
        var r = new ParserServidores().leer(Path.of("datos/datos.csv"));
        var c = new Cloud(r.getServidores());
        assertEquals(15170, c.total());
        assertEquals(Map.of("SA-EAST-1", 5400, "US-EAST-1", 6790, "US-WEST-2", 2980), c.totalesPorRegion());
        assertEquals(16, c.filtrar(s -> s.getIndiceCarga() < 50).size());
        assertTrue(c.filtrar(s -> s.getId().equals("srv-1042")).isEmpty());
        assertTrue(r.getErrores().get(0).startsWith("Linea 7:"));
        assertTrue(r.getDescartes().get(0).contains("Linea 4: ESTABLE"));
        assertEquals("Leídas: 60 | procesadas: 30 | descartadas: 21 | inválidas: 9 | objetos: 30", r.informe());
    }

    @Test
    void resultadosYCopiasDefensivas() {
        var servidores = new ArrayList<Servidor>();
        var errores = new ArrayList<String>();
        var descartes = new ArrayList<String>();

        servidores.addAll(List.of(new Servidor("S1", "W1", 40)));
        errores.add("errores");
        descartes.add("descarte");

        var r = new ResultadoParseo(servidores, 3, 1, errores, descartes);
        servidores.clear();
        errores.clear();
        descartes.clear();

        assertEquals(1, r.getProcesadas());
        assertEquals(1, r.getDescartadas());
        assertEquals(1, r.getErrores().size());

        assertThrows(UnsupportedOperationException.class, () -> r.getServidores().clear());
        assertThrows(UnsupportedOperationException.class, () -> r.getErrores().clear());
        assertThrows(UnsupportedOperationException.class, () -> r.getDescartes().clear());
    }

    @Test
    void estructuraYContinuacion() throws IOException {
        Path p = dir.resolve("x.csv");
        Files.writeString(p,
                "id,region,porcentajeCPU,usoMemoria,estado\nS2,S1,101,9,ALERTA\ns7,U2,54,40,ALERTA\nS7,U2,20,19,ESTABLE\n");
        var r = new ParserServidores().leer(p);
        assertEquals(3, r.getLeidas());
        assertEquals(1, r.getInvalidas());
        assertEquals(1, r.getProcesadas());
        assertEquals(1, r.getDescartes().size());
    }

    @Test
    void soloCabeceraYErroresDeArchivo() throws IOException {
        Path p = dir.resolve("x.csv");
        Files.writeString(p, "id,region,porcentajeCPU,usoMemoria,estado\n");
        assertEquals(0, new ParserServidores().leer(p).getLeidas());
        Files.writeString(p, "mal\n");
        assertThrows(IllegalArgumentException.class, () -> new ParserServidores().leer(p));
        assertThrows(IOException.class, () -> new ParserServidores().leer(dir.resolve("ausente")));
    }
}
