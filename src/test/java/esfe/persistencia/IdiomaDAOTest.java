package esfe.persistencia;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import esfe.dominio.Idioma;

import java.util.ArrayList;
import java.util.Random;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;



class IdiomaDAOTest {
    private IdiomaDAO idiomaDAO;

    @BeforeEach
    void setUp(){
        idiomaDAO = new IdiomaDAO();
    }

    private Idioma create(Idioma idioma) throws SQLException{
        Idioma res = idiomaDAO.create(idioma);

        assertNotNull(res, "El idioma creado no debería ser nulo.");
        assertEquals(idioma.getNombre(), res.getNombre(), "El nombre del idioma creado debe ser igual al original.");
        assertEquals(idioma.getFamilia(), res.getFamilia(), "La familia del idioma creado debe ser igual al original.");
        assertEquals(idioma.getNumeroHablantes(), res.getNumeroHablantes(), "El numero de hablantes del idioma creado debe ser igual al original.");

        return res;
    }

    private void update(Idioma idioma) throws SQLException{
        idioma.setNombre(idioma.getNombre() + "_u");
        idioma.setFamilia("u" + idioma.getFamilia());
        idioma.setNumeroHablantes(idioma.getNumeroHablantes());

        boolean res = idiomaDAO.update(idioma);

        assertTrue(res, "La actualización del idioma debería ser exitosa.");

        getById(idioma);
    }

    private void getById(Idioma idioma) throws SQLException {
        Idioma res = idiomaDAO.getById(idioma.getId());

        assertNotNull(res, "El idioma obtenido por ID no debería ser nulo.");
        assertEquals(idioma.getId(), res.getId(), "El ID del idioma obtenido debe ser igual al original.");
        assertEquals(idioma.getNombre(), res.getNombre(), "El nombre del idioma obtenido debe ser igual al esperado.");
        assertEquals(idioma.getFamilia(), res.getFamilia(), "La familia del idioma obtenido debe ser igual al esperado.");
        assertEquals(idioma.getNumeroHablantes(), res.getNumeroHablantes(), "El numero de hablantes del idioma obtenido debe ser igual al esperado.");
    }

    private void search(Idioma idioma) throws SQLException {
        ArrayList<Idioma> idiomas = idiomaDAO.search(idioma.getNombre());
        boolean find = false;

        for (Idioma userItem : idiomas) {
            if (userItem.getNombre().contains(idioma.getNombre())) {
                find = true;
            }
            else{
                find = false;
                break;
            }
        }

        assertTrue(find, "el nombre buscado no fue encontrado : " + idioma.getNombre());
    }

    private void delete(Idioma idioma) throws SQLException{
        boolean res = idiomaDAO.delete(idioma);

        assertTrue(res, "La eliminación del idioma debería ser exitosa.");

        Idioma res2 = idiomaDAO.getById(idioma.getId());

        assertNull(res2, "El idioma debería haber sido eliminado y no encontrado por ID.");
    }

    @Test
    void testIdiomaDAO() throws SQLException {
        Idioma idioma = new Idioma(0, "Test Idioma", "familia", 10);

        // Llama al método 'create' para persistir el usuario de prueba en la base de datos (simulada) y verifica su creación.
        Idioma testIdioma = create(idioma);

        // Llama al método 'update' para modificar los datos del usuario de prueba y verifica la actualización.
        update(testIdioma);

        // Llama al método 'search' para buscar usuarios por el nombre del usuario de prueba y verifica que se encuentre.
        search(testIdioma);


        // Llama al método 'delete' para eliminar el usuario de prueba de la base de datos y verifica la eliminación.
        delete(testIdioma);
    }

    @Test
    void createIdioma() throws SQLException {
        Idioma idioma = new Idioma(0, "Español", "Lenguas romanicas", 6000);
        Idioma res = idiomaDAO.create(idioma);
        assertNotEquals(res,null);
    }
}