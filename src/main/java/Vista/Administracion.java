package Vista;

import Modelo.BibliotecarioMaestro;
import Modelo.BibliotecarioSencillo;
import Modelo.ImagenUniversidad;
import javax.swing.*;
import java.awt.*;

public class Administracion extends JFrame{
    private transient JPanel panelPrincipal;
    private transient JButton agregarButton, actualizarButton, eliminarButton, verButton, regresarButton, salirButton;
    private transient JPanel imagenAdmin, funcionalidadButtons, funcionalidadCrud;
    private transient JComboBox eleccionBibliotecarioJComboBox;

    public Administracion() {
        ImagenUniversidad img = new ImagenUniversidad("src/main/java/biblioteca2.png");
        imagenAdmin.setLayout(new BorderLayout());
        imagenAdmin.add(img);

        setTitle("Admin");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(610, 490);
        setLocationRelativeTo(null);

        // Inicializamos el JComboBox con las dos opciones
        eleccionBibliotecarioJComboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                "Bibliotecario Sencillo",
                "Bibliotecario Maestro"
        }));


        // Botones con lógica dinámica dependiendo del tipo de bibliotecario seleccionado en el JComboBox

        agregarButton.addActionListener(e -> {
            if (esSencillo()) {
                BibliotecarioSencillo.agregarBibliotecario();
            } else {
                BibliotecarioMaestro.agregarBibliotecario();
            }
        });

        verButton.addActionListener(e -> {
            if (esSencillo()) {
                BibliotecarioSencillo.verBibliotecarios();
            } else {
                BibliotecarioMaestro.verBibliotecarios();
            }
        });

        actualizarButton.addActionListener(e -> {
            if (esSencillo()) {
                BibliotecarioSencillo.actualizarBibliotecario();
            } else {
                BibliotecarioMaestro.actualizarBibliotecario();
            }
        });

        eliminarButton.addActionListener(e -> {
            if (esSencillo()) {
                BibliotecarioSencillo.eliminarBibliotecario();
            } else {
                BibliotecarioMaestro.eliminarBibliotecario();
            }
        });

        regresarButton.addActionListener(e -> {
            this.dispose(); //cierra la ventana actual
            Login login = new Login(); //abre la ventana de Login
            login.setVisible(true); //visualiza la ventana de Login
        });

        salirButton.addActionListener(_ -> System.exit(0)); //sale de la aplicacion
    }

    private boolean esSencillo() { //si es Bibliotecario sencillo devuelve true, sino false
        String seleccion = (String) eleccionBibliotecarioJComboBox.getSelectedItem();
        return "Bibliotecario Sencillo".equals(seleccion);
    }
}
