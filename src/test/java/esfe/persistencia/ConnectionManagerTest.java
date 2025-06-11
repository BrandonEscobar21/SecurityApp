package esfe.persistencia;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionManagerTest {
    ConnectionManager connectionManager;
    @BeforeEach
    void setUp() throws SQLException{
        // Se ejecuta antes de cada método de prueba.
        // Inicializa el ConnectionManager.
        connectionManager = ConnectionManager.getInstance();
    }

    @AfterEach
    void tearDown() throws SQLException{
        // Se ejecuta despues de cada método de prueba.
        // Cierra la conexion y limpia los recursos.
        if (connectionManager != null) {
            connectionManager.disconnect();
            connectionManager = null; // Para asegurar que no se use accidentalmente
        }
    }

    @Test
    void connect() throws SQLException {
        // Intenta establecer una conexión a la base de datos utilizando el método connect() de ConnectionManager.
        Connection conn = connectionManager.connect();
        // Realiza una aserción para verificar que la conexión establecida no sea nula.
        // Si conn es nulo, la prueba fallará con el mensaje "La conexión no debe ser nula".
        assertNotNull(conn, "La conexión no debe ser nula");
        //Realiza una aserción para verificar que la conexión establecida este abierta.
        // El método isClosed() devuelve true si la conexión esta cerrada, por lo que assertFalse espera que devuelva false.
        // Si la conexión esta cerrada, la prueba fallara con el mensaje "La conexión debe estar abierta".
        assertFalse(conn.isClosed(), "La conexión debe estar abierta");
        if (conn != null) {
            conn.close(); // Cierra la conexión despues de la prueba.
        }
    }
}