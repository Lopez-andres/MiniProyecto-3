package Vista;

import javax.swing.*;
import Modelo.BibliotecarioMaestro;
import Modelo.BibliotecarioSencillo;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import static Modelo.BibliotecarioMaestro.listaBibliotecariosMaestros;
import static Modelo.BibliotecarioSencillo.listaBibliotecariosSencillos;

public class Login extends JFrame {
    private JPanel panelPrincipal;
    private JTextField campoUsuarioTexto;
    private JPasswordField passwordTexto;
    private JButton administracionButton, modeButton;
    private JLabel UsuarioJLabel, PasswordJLabel;
    private JLabel LoginJLabel;
    private JButton salirButton, bibliotecaButton;
    private boolean modoOscuro = true; // Estado inicial en oscuro


     /*Inicializa y configura las propiedades de la ventana de login.
     Establece el título, contenido, operación de cierre, tamaño y posición.*/

    public Login() {
        setTitle("Login");
        setContentPane(panelPrincipal); //lo que el JFrame debe de mostrar
        setDefaultCloseOperation(EXIT_ON_CLOSE); //la aplicacion se cierre correctamente
        setSize(450, 500);
        setLocationRelativeTo(null);

        administracionButton.addActionListener(_ -> validarAdministracion()); // Se asocia este botón con su respectiva validación
        modeButton.addActionListener(_ -> cambioColor()); // Se asocia este botón a cambiar el tema cuando se presione
        salirButton.addActionListener(_ -> System.exit(0)); //Boton con la logica de salir de la aplicacion
        bibliotecaButton.addActionListener(_ -> validarBiblioteca()); // Se asocia este botón con su respectiva validación
    }

     /*Cambia entre el modo oscuro y claro utilizando la librería FlatLaf.
     Actualiza la interfaz de usuario y restaura las fuentes personalizadas.*/

    private void cambioColor() {
        try {
            if (modoOscuro) {
                UIManager.setLookAndFeel(new FlatLightLaf()); // Modo claro
            } else {
                UIManager.setLookAndFeel(new FlatDarculaLaf()); // Modo oscuro
            }
            modoOscuro = !modoOscuro; // Alternar estado

            SwingUtilities.updateComponentTreeUI(this); // Aplicar cambios y actualizar la interfaz
            restaurarFuente(); // Volver a aplicar la fuente
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /*Establece fuentes personalizadas para los componentes de texto. Aplica una fuente monoespaciada
     a todos los elementos de la interfaz para mantener una apariencia consistente.*/

    private void restaurarFuente() {
        // Se define la fuente de cada componente
        Font fuente = new Font("Monospaced", Font.PLAIN, 20); // Fuente Monospaced, tamaño 20, estilo normal
        Font fuente2 = new Font("Monospaced", Font.BOLD, 30);
        LoginJLabel.setFont(fuente2);
        UsuarioJLabel.setFont(fuente);
        PasswordJLabel.setFont(fuente);
        campoUsuarioTexto.setFont(fuente);
        passwordTexto.setFont(fuente);
        administracionButton.setFont(fuente);
        modeButton.setFont(fuente);
        salirButton.setFont(fuente);
        bibliotecaButton.setFont(fuente);
    }

    /* Valida los datos ingresados en el login.
      Si son válidos, abre la ventana correspondiente según el tipo de usuario:
      - Si es administrador, abre la ventana de administrador
      - Si es bibliotecario, abre la ventana de biblioteca
      Muestra mensajes de error si las credenciales son incorrectas.*/

    private void validarAdministracion() {
        BibliotecarioMaestro bm = new BibliotecarioMaestro("","");
        bm.getCargarDesdeArchivoMaestros(); // Cargar bibliotecarios maestros desde archivo binario

        String usuario = campoUsuarioTexto.getText().trim();
        String password = new String(passwordTexto.getPassword()).trim();

        // Verificar si los campos están vacíos primero
        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de continuar.",
                    "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Recorre la lista de bibliotecarios maestros para validar credenciales
        boolean encontradoBibliotecarioMaestro = false;
        for (BibliotecarioMaestro b : listaBibliotecariosMaestros) { //recorre cada objeto de esta clase
            if (b.validarBibliotecario(usuario, password)) { //valida el usuario y contraseña
                System.out.println(listaBibliotecariosMaestros); //imprimir la lista de bibliotecarios maestros

                Administracion admin = new Administracion();
                admin.setVisible(true); //visualiza la ventana administracion
                this.dispose(); //cierra la venta de login

                encontradoBibliotecarioMaestro = true;
                break;
            }
        }

        // Si no se encontró ningún bibliotecario
        if (!encontradoBibliotecarioMaestro) {
            mostrarMensaje("Usuario o contraseña de bibliotecario incorrectos.");
        }
    }

    private void validarBiblioteca() {
        BibliotecarioSencillo BS = new BibliotecarioSencillo("", "");
        BS.getCargarDesdeArchivoSencillo(); //carga el archivo binario de bibliotecario sencillo

        BibliotecarioMaestro bm = new BibliotecarioMaestro("", "");
        bm.getCargarDesdeArchivoMaestros(); //carga el archivo binario de bibliotecario maestro

        String usuario = campoUsuarioTexto.getText().trim();
        String password = new String(passwordTexto.getPassword()).trim();

        //verifica que los campos no esten vacios
        if (usuario.trim().isEmpty()  || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de continuar.",
                    "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar primero maestros
        for (BibliotecarioMaestro b : listaBibliotecariosMaestros) {
            if (b.validarBibliotecario(usuario, password)) {
                Biblioteca Bib = new Biblioteca();
                Bib.setVisible(true); //visualiza la ventana biblioteca
                this.dispose();
                return;  //sale del metodo
            }
        }

        // Validar sencillos solo si no es maestro
        for (BibliotecarioSencillo bs : listaBibliotecariosSencillos) {
            if (bs.validarBibliotecario(usuario, password)) {
                Biblioteca Bib = new Biblioteca();
                Bib.setVisible(true); //visualiza la ventana biblioteca
                this.dispose();
                return;  //sale del metodo
            }
        }

        // Si ninguno coincide muestra un mensaje de error
        mostrarMensaje("Usuario o contraseña de bibliotecario incorrectos.");
    }

    //Se encarga de mostrar un mensaje emergente con el texto proporcionado.
    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        Login log = new Login();
        log.setVisible(true);
    }
}
