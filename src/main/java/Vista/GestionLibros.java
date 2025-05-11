package Vista;
import Modelo.ImagenUniversidad;
import Modelo.Libro;
import javax.swing.*;
import java.awt.*;

import static Modelo.Libro.cargarDesdeArchivoLibro;

public class GestionLibros extends JDialog {
    private JPanel panelPrincipal;
    private JButton verButton;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton actualizarButton;
    private JComboBox<String> tipoComboBox;
    private JPanel categoriasPanel, librosPanel, funcionalidadPanel;
    private JButton regresarButton;

    //clase de JDialog que actua como una ventana secundaria de Biblioteca
    public GestionLibros(JFrame parent, boolean modal) {
        super();
        ImagenUniversidad img = new ImagenUniversidad("src/main/java/biblioteca.png");
        categoriasPanel.setLayout(new BorderLayout());
        categoriasPanel.add(img);

        // Inicializamos el JComboBox con las dos opciones, Libros o categorias, respecto al CRUD de cada uno
        tipoComboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                "Libro", "Categoria"}));

        //segun el tipo de elecion en el JComboBox se carga desde el archivo y se llama el metodo del CRUD correspondiente
        agregarButton.addActionListener(e -> {
            cargarDesdeArchivoLibro();
            if ("Libro".equals(tipoComboBox.getSelectedItem())) {
                Libro.agregarLibro();
            } else {
                Libro.crearCategoria();
            }
        });

        verButton.addActionListener(e -> {
            cargarDesdeArchivoLibro();
            if ("Libro".equals(tipoComboBox.getSelectedItem())) {
                Libro.verLibros();
            } else {
                Libro.verCategorias();
            }
        });

        actualizarButton.addActionListener(e -> {
            cargarDesdeArchivoLibro();
            if ("Libro".equals(tipoComboBox.getSelectedItem())) {
                Libro.actualizarLibro();
            } else {
                Libro.actualizarCategoria();
            }
        });

        eliminarButton.addActionListener(e -> {
            cargarDesdeArchivoLibro();
            if ("Libro".equals(tipoComboBox.getSelectedItem())) {
                Libro.eliminarLibro();
            } else {
                Libro.eliminarCategoria();
            }
        });

        regresarButton.addActionListener(e -> {
            parent.dispose(); // Cierra la ventana actual
            new Biblioteca().setVisible(true); // Abre la ventana Biblioteca
        });

        // Configuramos la ventana
        parent.setTitle("GestionLibros");
        parent.setContentPane(panelPrincipal);
        parent.setDefaultCloseOperation(EXIT_ON_CLOSE);
        parent.setSize(610, 490);
        parent.setLocationRelativeTo(null);
    }

    //metodo para mostrar la ventana GestionLibros
    public void setVisible(boolean b) {}
}
