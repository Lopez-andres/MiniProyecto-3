package Modelo;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecarioMaestro implements Serializable{
    @Serial
    private static final long serialVersionUID = 6791126037671970601L; //version de serial
    private static int contadorId = 1;
    private String usuario;
    private String password;
    private final int id;
    public static List<BibliotecarioMaestro> listaBibliotecariosMaestros = new ArrayList<>();
    private static final String ARCHIVO = "bibliotecariosMaestros.dat"; //archivo de bibliotecarios maestros

    public BibliotecarioMaestro(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
        this.id = contadorId++;
        cargarDesdeArchivoMaestros(); //carga desde el archivo bibliotecarios maestros cada vez que se crea un objeto
    }

     /*Carga los datos de los bibliotecarios maestros desde el archivo binario.
     Si el archivo no existe o hay un error, se crea una lista vacía.
     Además, ajusta el contador de ID para evitar duplicaciones.*/

    private static void cargarDesdeArchivoMaestros() {
        File file = new File(ARCHIVO); //crea el archivo desde la ruta especificada

        if (!file.exists()) {
            listaBibliotecariosMaestros = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            //lee la lista de objetos desde el archivo
            listaBibliotecariosMaestros = (List<BibliotecarioMaestro>) ois.readObject();

            //si la lista no está vacía, actualiza el contador de ID al siguiente disponible
            if (!listaBibliotecariosMaestros.isEmpty()) {
                contadorId = listaBibliotecariosMaestros.getLast().getId() + 1;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            listaBibliotecariosMaestros = new ArrayList<>(); //reinicia lista en caso de error
        }
    }

    private static void guardarEnArchivoMaestros() {//abre el archivo que contiene los datos para escribir
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(listaBibliotecariosMaestros); //escribe los datos en el archivo y lo guarda
        } catch (IOException e) {
            e.printStackTrace(); // Esto te mostrará el error en consola
            JOptionPane.showMessageDialog(null, "Error al guardar los bibliotecarios: " + e.getMessage());
        }
    }

    public static void agregarBibliotecario() {
        cargarDesdeArchivoMaestros(); //cargamos los datos antes de agregar
        String usuario = JOptionPane.showInputDialog(null, "Ingrese el nombre del bibliotecario:");
        String password = JOptionPane.showInputDialog(null, "Ingrese la contraseña del bibliotecario:");

        if (usuario != null && password != null && !usuario.isEmpty() && !password.isEmpty()) { //verifica que los campos no sean nulos o vacios
            if (usuario.equals(password)) { //manda un mensaje de alerta si el nombre y la contraseña son iguales
                JOptionPane.showMessageDialog(null, "El usuario y la contraseña no pueden ser iguales.");
                return; //regresa a la administracion de bibliotecarios
            }

            //manda un mensaje de alerta si ese usuario o contraseña son iguales a los de otros usuarios
            for (BibliotecarioMaestro b : listaBibliotecariosMaestros) {
                if (b.getUsuario().equals(usuario) || b.getPasswordUsuario().equals(password)) {
                    JOptionPane.showMessageDialog(null, "Ya existe un bibliotecario con esas credenciales.");
                    return; //regresa a la administracion de bibliotecarios
                }
            }
            BibliotecarioMaestro nuevo = new BibliotecarioMaestro(usuario, password); //creamos un objeto bibliotecario maestro
            listaBibliotecariosMaestros.add(nuevo); //lo añadimos a la lista de bibliotecarios maestros
            guardarEnArchivoMaestros(); //guardamos los cambios en el archivo bibliotecarios maestros
            JOptionPane.showMessageDialog(null, "Bibliotecario agregado correctamente.");
            System.out.println("\n📥 Bibliotecario agregado:\n" + nuevo);
        } else {
            JOptionPane.showMessageDialog(null, "Datos inválidos.");
        }
    }

    public static void verBibliotecarios() { //verifica que los campos no sean vacios
        cargarDesdeArchivoMaestros(); //cargamos los datos antes de ver
        if (listaBibliotecariosMaestros.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay bibliotecarios registrados.");
            return;
        }

        //muestra en un StringBuilder todos los datos de la lista de bibliotecarios maestros
        StringBuilder texto = new StringBuilder("📘 LISTA DE BIBLIOTECARIOS MAESTROS\n\n");

        //recorre la lista de bibliotecarios maestros y muestra su id, usuario y contraseña
        for (BibliotecarioMaestro b : listaBibliotecariosMaestros) {
            texto.append("ID: ").append(b.getId())
                    .append(" | Usuario: ").append(b.getUsuario())
                    .append(" | Contraseña: ").append(b.getPasswordUsuario()).append("\n");
        }

        //mostramos este StringBuilder en un JTextArea junto con un JScrollPane
        JTextArea areaTexto = new JTextArea(texto.toString());
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaTexto.setBackground(Color.BLACK);
        areaTexto.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        JOptionPane.showMessageDialog(null, scrollPane, "Listado de Bibliotecarios", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void actualizarBibliotecario() {
        cargarDesdeArchivoMaestros(); //cargamos la lista antes de modificarla

        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del bibliotecario a actualizar:"));
            for (BibliotecarioMaestro b : listaBibliotecariosMaestros) {
                if (b.getId() == id) {
                    String nuevoUsuario = JOptionPane.showInputDialog(null, "Nuevo nombre de usuario: ");
                    String nuevaPassword = JOptionPane.showInputDialog(null, "Nueva contraseña: ");

                    if (nuevoUsuario != null && nuevaPassword != null && !nuevoUsuario.isEmpty() && !nuevaPassword.isEmpty()) {
                        b.setUsuario(nuevoUsuario);
                        b.setPasswordUsuario(nuevaPassword);
                        guardarEnArchivoMaestros(); //guardamos los cambios
                        JOptionPane.showMessageDialog(null, "Bibliotecario actualizado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Datos inválidos.");
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "No se encontró el bibliotecario con ese ID.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.");
        }
    }

    public static void eliminarBibliotecario() {
        cargarDesdeArchivoMaestros(); //cargamos los datos antes de eliminar

        try {
            //solicita el ID y lo convierte a entero
            int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del bibliotecario a eliminar:"));
            for (BibliotecarioMaestro b : listaBibliotecariosMaestros) {
                if (b.getId() == id) {
                    listaBibliotecariosMaestros.remove(b); //elimina el bibliotecario
                    guardarEnArchivoMaestros(); //guardamos los cambios
                    JOptionPane.showMessageDialog(null, "Bibliotecario eliminado correctamente.");
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "No se encontró el bibliotecario con ese ID.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.");
        }
    }

    public boolean validarBibliotecario(String usuario, String password) { //metodo usado para comparar usuario y contraseña que se pasen al metodo con los ya existentes
        return this.usuario.equals(usuario) && this.password.equals(password);
    }

    //getters y setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String nuevoUsuario) {
        this.usuario = nuevoUsuario;
    }

    public String getPasswordUsuario() {
        return password;
    }

    public void setPasswordUsuario(String nuevaPassword) {
        this.password = nuevaPassword;
    }

    public int getId() {
        return id;
    }

    public void getCargarDesdeArchivoMaestros() {
        cargarDesdeArchivoMaestros();
    }
}
