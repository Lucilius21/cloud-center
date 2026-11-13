package ar.edu.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class CloudTest {
    @Test
    void vacio() {
        var c = new Cloud(List.of());
        assertEquals(0, c.total());
        assertTrue(c.totalesPorRegion().isEmpty());
        assertTrue(c.filtrar(s -> true).isEmpty());
    }

    @Test
    void copiaDefensiva() {
        var lista = new ArrayList<Servidor>();
        lista.add(new Servidor("S1", "W1", 45));
        var c = new Cloud(lista);
        lista.clear();
        assertEquals(450, c.total());
        assertThrows(UnsupportedOperationException.class, () -> c.filtrar(s -> true).clear());
    }

    @Test
    void totalesYFiltro() {
        var lista = List.of(new Servidor("S1", "W1", 65), new Servidor("S2", "W2", 35), new Servidor("S3", "W1", 45));
        var c = new Cloud(lista);
        assertEquals(1450, c.total());
        assertEquals(2, c.filtrar(s -> s.getIndiceCarga() < 50).size());
        assertTrue(c.filtrar(s -> s.getRegion().equals("E1")).isEmpty());
        assertEquals(Map.of("W1", 1100, "W2", 350), c.totalesPorRegion());

    }
}
