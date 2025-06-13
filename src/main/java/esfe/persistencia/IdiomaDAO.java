package esfe.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import esfe.dominio.Idioma;

public class IdiomaDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public IdiomaDAO(){
        conn = ConnectionManager.getInstance();
    }

    public Idioma create (Idioma idioma) throws SQLException {
        Idioma res = null;

        try {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO " +
                            "Idiomas (nombre, familia, numeroHablantes)" +
                            "VALUES (?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, idioma.getNombre());
            ps.setString(2, idioma.getFamilia());
            ps.setInt(3, idioma.getNumeroHablantes());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                ResultSet  generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado= generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al crear el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean update(Idioma idioma) throws SQLException{
        boolean res = false;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Idiomas " +
                            "SET nombre = ?, familia = ?, numeroHablantes = ? " +
                            "WHERE id = ?"
            );

            ps.setString(1, idioma.getNombre());
            ps.setString(2, idioma.getFamilia());
            ps.setInt(3, idioma.getNumeroHablantes());
            ps.setInt(4, idioma.getId());

            if(ps.executeUpdate() > 0){
                res = true;
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al modificar el idioma: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete(Idioma idioma) throws SQLException{
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Idiomas WHERE id = ?"
            );

            ps.setInt(1, idioma.getId());

            if(ps.executeUpdate() > 0){
                res = true;
            }
            ps.close();
        }catch (SQLException ex){
            throw new SQLException("Error al eliminar el idioma: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<Idioma> search(String nombre) throws SQLException{
        ArrayList<Idioma> records  = new ArrayList<>();

        try {
            ps = conn.connect().prepareStatement("SELECT id, nombre, familia, numeroHablantes " +
                    "FROM Idiomas " +
                    "WHERE nombre LIKE ?");

            ps.setString(1, "%" + nombre + "%");

            rs = ps.executeQuery();

            while (rs.next()){
                Idioma idioma = new Idioma();
                idioma.setId(rs.getInt(1));
                idioma.setNombre(rs.getString(2));
                idioma.setFamilia(rs.getString(3));
                idioma.setNumeroHablantes(rs.getInt(4));

                records.add(idioma);
            }
            ps.close();
            rs.close();
        }catch (SQLException ex){
            throw new SQLException("Error al buscar idiomas: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    public Idioma getById(int id) throws SQLException{
        Idioma idioma = new Idioma();

        try {
            ps = conn.connect().prepareStatement("SELECT id, nombre, familia, numeroHablantes " +
                    "FROM Idiomas " +
                    "WHERE id = ?");

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()){
                idioma.setId(rs.getInt(1));       // Obtener el ID del usuario.
                idioma.setNombre(rs.getString(2));   // Obtener el nombre del usuario.
                idioma.setFamilia(rs.getString(3));  // Obtener el correo electrónico del usuario.
                idioma.setNumeroHablantes(rs.getInt(4));
            }else {
                idioma = null;
            }
            ps.close();
            rs.close();
        }catch (SQLException ex){
            throw new SQLException("Error al obtener un idioma por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return idioma;
    }
}
