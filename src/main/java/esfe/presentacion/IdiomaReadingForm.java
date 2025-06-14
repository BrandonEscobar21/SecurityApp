package esfe.presentacion;

import esfe.persistencia.IdiomaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import esfe.dominio.Idioma;
import esfe.utils.CUD;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class IdiomaReadingForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtName;
    private JButton btnCreate;
    private JTable tableIdioma;
    private JButton btnUpdate;
    private JButton btnDelete;

    private IdiomaDAO idiomaDAO;
    private MainForm mainForm;

    public IdiomaReadingForm(MainForm mainForm){
        this.mainForm = mainForm;
        idiomaDAO = new IdiomaDAO();
        setContentPane(mainPanel); // Establece el panel principal como el contenido de este diálogo.
        setModal(true); // Hace que este diálogo sea modal, bloqueando la interacción con la ventana principal hasta que se cierre.
        setTitle("Buscar Idioma"); // Establece el título de la ventana del diálogo.
        pack(); // Ajusta el tamaño de la ventana para que todos sus componentes se muestren correctamente.
        setLocationRelativeTo(mainForm);

        txtName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!txtName.getText().trim().isEmpty()) {
                    search(txtName.getText());
                } else {
                    DefaultTableModel emptyModel = new DefaultTableModel();
                    tableIdioma.setModel(emptyModel);
                }
            }
        });

        btnCreate.addActionListener(s -> {
            IdiomaWriteForm idiomaWriteForm = new IdiomaWriteForm(this.mainForm, CUD.CREATE, new Idioma());
            idiomaWriteForm.setVisible(true);
            DefaultTableModel emptyModel = new DefaultTableModel();
            tableIdioma.setModel(emptyModel);
        });

        btnUpdate.addActionListener(s -> {
            Idioma idioma = getIdiomaFromTableRow();
            if (idioma != null) {
                IdiomaWriteForm idiomaWriteForm = new IdiomaWriteForm(this.mainForm, CUD.UPDATE, idioma);
                idiomaWriteForm.setVisible(true);
                DefaultTableModel emptyModel = new DefaultTableModel();
                tableIdioma.setModel(emptyModel);
            }
        });

        btnDelete.addActionListener(s -> {
            Idioma idioma = getIdiomaFromTableRow();
            if (idioma != null) {
                IdiomaWriteForm idiomaWriteForm = new IdiomaWriteForm(this.mainForm, CUD.DELETE, idioma);
                idiomaWriteForm.setVisible(true);
                DefaultTableModel emptyModel = new DefaultTableModel();
                tableIdioma.setModel(emptyModel);
            }
        });
    }

    private void search(String query) {
        try {
            ArrayList<Idioma> idiomas = idiomaDAO.search(query);
            createTable(idiomas);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void createTable(ArrayList<Idioma> idiomas) {

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Id");
        model.addColumn("Nombre");
        model.addColumn("Familia");
        model.addColumn("Numero de Hablantes");

        this.tableIdioma.setModel(model);

        Object row[] = null;

        for (int i = 0; i < idiomas.size(); i++) {
            Idioma idioma = idiomas.get(i);
            model.addRow(row);
            model.setValueAt(idioma.getId(), i, 0);
            model.setValueAt(idioma.getNombre(), i, 1);
            model.setValueAt(idioma.getFamilia(), i, 2);
            model.setValueAt(idioma.getNumeroHablantes(), i, 3);
        }

        hideCol(0);
    }

    private void hideCol(int pColumna) {
        this.tableIdioma.getColumnModel().getColumn(pColumna).setMaxWidth(0);
        this.tableIdioma.getColumnModel().getColumn(pColumna).setMinWidth(0);
        this.tableIdioma.getTableHeader().getColumnModel().getColumn(pColumna).setMaxWidth(0);
        this.tableIdioma.getTableHeader().getColumnModel().getColumn(pColumna).setMinWidth(0);
    }

    private Idioma getIdiomaFromTableRow() {
        Idioma idioma = null;
        try {
            int filaSelect = this.tableIdioma.getSelectedRow();
            int id = 0;

            if (filaSelect != -1) {
                id = (int) this.tableIdioma.getValueAt(filaSelect, 0);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Seleccionar una fila de la tabla.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            idioma = idiomaDAO.getById(id);

            if (idioma.getId() == 0) {
                JOptionPane.showMessageDialog(null,
                        "No se encontró ningún idioma.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            return idioma;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
