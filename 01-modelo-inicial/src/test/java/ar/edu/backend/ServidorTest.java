package ar.edu.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ServidorTest {
    @Test
    void construccionYCaclulo() {
        Servidor s = Servidor.desdeCampos(new String[] { "S1", "USW1", "50", "10", "A" });
        assertEquals("S1", s.getId());
        assertEquals("USW1", s.getRegion());
        assertEquals(38, s.getIndiceCarga());
        assertEquals(380, s.costoRiesgoOperativo());
    }

    @Test
    void invalidos() {
        for (int indice : new int[] { -1, 75 })
            assertThrows(IllegalArgumentException.class, () -> new Servidor("S1", "W!", indice));

        assertThrows(IllegalArgumentException.class, () -> new Servidor(null, "W1", 60));
        assertThrows(IllegalArgumentException.class, () -> new Servidor(" ", "W1", 60));
        assertThrows(IllegalArgumentException.class, () -> new Servidor("S2", null, 60));
        assertThrows(IllegalArgumentException.class, () -> new Servidor("S2", " ", 60));

        assertThrows(IllegalArgumentException.class,
                () -> Servidor.desdeCampos(new String[] { "S1", "W1", "105", "1500", "A" }));
        assertThrows(IllegalArgumentException.class,
                () -> Servidor.desdeCampos(new String[] { "S1", "W1", "85", "45000", "A" }));
    }

}
