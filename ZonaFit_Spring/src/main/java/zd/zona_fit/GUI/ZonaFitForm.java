package zd.zona_fit.GUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zd.zona_fit.models.Cliente;
import zd.zona_fit.services.IClienteServicio;
import zd.zona_fit.services.Servicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class ZonaFitForm extends JFrame {
    private JPanel PanelPrincipal;
    private JTable clientesTable;
    private JTextField nameTxt;
    private JTextField apellidoTxt;
    private JTextField membresiaTxt;
    private JButton saveBtn;
    private JButton limpiarButton;
    private JButton eliminarButton;
    private DefaultTableModel tableModel;
    private Integer idCliente;
    private static final int COL_ID = 0;
    private static final int COL_NOMBRE = 1;
    private static final int COL_APELLIDO = 2;
    private static final int COL_MEMBRESIA = 3;


    IClienteServicio clienteServicio;

    @Autowired
    public ZonaFitForm(Servicio servicio) {
        this.clienteServicio = servicio;
        init();
        saveBtn.addActionListener(_ -> guardarCliente());
        clientesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarCliente();
            }
        });
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarForm();
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });
    }

    private void init() {
        setContentPane(PanelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.tableModel = new DefaultTableModel(0, 4);
        String[] titulos = {"ID", "Nombre", "Apellido", "Membresía"};
        this.tableModel.setColumnIdentifiers(titulos);
        this.clientesTable = new JTable(this.tableModel);

        listarClientes();
    }

    private void listarClientes() {
        this.tableModel.setRowCount(0);
        var clientes = this.clienteServicio.listarClientes();
        clientes.forEach(cliente -> this.tableModel.addRow(new Object[]{cliente.getId(), cliente.getNombre(),
                cliente.getApellido(), cliente.getMembresia()}));

    }

    private void guardarCliente() {
        String nombre = nameTxt.getText().strip();
        String apellido = apellidoTxt.getText().strip();
        var membresia = membresiaTxt.getText().strip();

        if (nombre.isEmpty() || apellido.isEmpty() || membresia.isEmpty()) {
            mostrarMensaje("Hay campos vacíos, por favor, complétalos");
            return;
        }

        //matches verifica que solo haya numeros enteros en el campo de texto

        if (!membresia.matches("\\d+")) {
            mostrarMensaje("El campo membresía solo acepta números enteros");
            return;
        }

        var introMembresia = Integer.parseInt(membresia);

        Cliente cliente = new Cliente(this.idCliente, nombre, apellido, introMembresia);
        try {
            this.clienteServicio.saveCliente(cliente);
            String mensaje = (this.idCliente == null) ? "Cliente guardado correctamente" : "Cliente editado " +
                                                                                                   "correctamente";
            mostrarMensaje(mensaje);
            limpiarForm();
            listarClientes();
        } catch (RuntimeException e) {
            e.printStackTrace();
            mostrarMensaje("Error al guardar el usuario");
        }

    }

    private void cargarCliente() {
        var renglon = clientesTable.getSelectedRow();
        if (renglon != -1) {
            var modelo = clientesTable.getModel();

            var id = String.valueOf(modelo.getValueAt(renglon, COL_ID));
            idCliente = Integer.parseInt(id);

            this.nameTxt.setText(String.valueOf(modelo.getValueAt(renglon, COL_NOMBRE)));
            this.apellidoTxt.setText(String.valueOf(modelo.getValueAt(renglon, COL_APELLIDO)));
            this.membresiaTxt.setText(String.valueOf(modelo.getValueAt(renglon, COL_MEMBRESIA)));
        }
    }

    private void eliminarCliente() {
        if (this.idCliente == null) {
            mostrarMensaje("Selecciona un cliente para eliminar");
            return;
        }

        var mensaje = "¿Seguro que quieres eliminar el cliente cuyo id es: " + this.idCliente + "?";
        if (!confirmar(mensaje, "Eliminar cliente")) {
            return; // el usuario canceló
        }

        try {
            Cliente c = new Cliente(this.idCliente, null, null, null);
            clienteServicio.deleteCliente(c);
            mostrarMensaje("Cliente eliminado correctamente");
            limpiarForm();
            listarClientes();
        } catch (RuntimeException e) {
            e.printStackTrace();
            mostrarMensaje("Error al eliminar el usuario");
        }
    }

    //funciones comunes creo
    private void limpiarForm() {
        this.nameTxt.setText("");
        this.apellidoTxt.setText("");
        this.membresiaTxt.setText("");
        this.idCliente = null;
        this.clientesTable.getSelectionModel().clearSelection();
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private boolean confirmar(String mensaje, String titulo) {
        int opcion = JOptionPane.showConfirmDialog(null, mensaje, titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        return opcion == JOptionPane.OK_OPTION;
    }
}
