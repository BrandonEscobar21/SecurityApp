package esfe.presentacion;

import esfe.dominio.User;
import esfe.persistencia.IdiomaDAO;
import esfe.persistencia.UserDAO;
import esfe.utils.CBOption;
import esfe.utils.CUD;

import javax.swing.*;

import esfe.dominio.Idioma;

public class IdiomaWriteForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtFamily;
    private JTextField txtNumber;
    private JButton btnOk;
    private JButton btnCancel;

    private IdiomaDAO idiomaDAO;
    private MainForm mainForm;
    private CUD cud;
    private Idioma en;

    public IdiomaWriteForm(MainForm mainForm, CUD cud, Idioma idioma) {
        this.cud = cud;
        this.en = idioma;
        this.mainForm = mainForm;
        idiomaDAO = new IdiomaDAO();
        setContentPane(mainPanel);
        setModal(true);
        init();
        pack();
        setLocationRelativeTo(mainForm);

        btnCancel.addActionListener(s -> this.dispose());
        btnOk.addActionListener(s -> ok());
    }

    private void init() {
        switch (this.cud) {
            case CREATE:
                setTitle("Crear Idioma");
                btnOk.setText("Guardar");
                break;
            case UPDATE:
                setTitle("Modificar Idioma");
                btnOk.setText("Guardar");
                break;
            case DELETE:
                setTitle("Eliminar Idioma");
                btnOk.setText("Eliminar");
                break;
        }

        setValuesControls(this.en);
    }

    private void setValuesControls(Idioma idioma) {
        txtName.setText(idioma.getNombre());

        txtFamily.setText(idioma.getFamilia());

        txtNumber.setText(String.valueOf(idioma.getNumeroHablantes()));


        if (this.cud == CUD.DELETE) {
            txtName.setEditable(false);
            txtFamily.setEditable(false);
            txtNumber.setEnabled(false);
        }
    }
    private boolean getValuesControls() {
        boolean res = false;

        if (txtName.getText().trim().isEmpty()) {
            return res;
        }
        else if (txtFamily.getText().trim().isEmpty()) {
            return res;
        }
        else if (txtNumber.getText().trim().isEmpty()) {
            return res;
        }
        else if (this.cud != CUD.CREATE && this.en.getId() == 0) {
            return res;
        }

        res = true;

        this.en.setNombre(txtName.getText());
        this.en.setFamilia(txtFamily.getText());
        this.en.setNumeroHablantes(Integer.parseInt(txtNumber.getText()));

        return res;
    }

    private void ok() {
        try {
            boolean res = getValuesControls();

            if (res) {
                boolean r = false;

                switch (this.cud) {
                    case CREATE:
                        Idioma idioma = idiomaDAO.create(this.en);
                        if (idioma.getId() > 0) {
                            r = true;
                        }
                        break;
                    case UPDATE:
                        r = idiomaDAO.update(this.en);
                        break;
                    case DELETE:
                        r = idiomaDAO.delete(this.en);
                        break;
                }

                if (r) {
                    JOptionPane.showMessageDialog(null,
                            "Transacción realizada exitosamente",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No se logró realizar ninguna acción",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Los campos con * son obligatorios",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

}
