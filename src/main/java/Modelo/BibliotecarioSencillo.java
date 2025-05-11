package Modelo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecarioSencillo implements Serializable {
    @Serial
    private static final long serialVersionUID = 6791126037671970601L; //versión de serialización
    private static int contadorId = 1;
    public static List<BibliotecarioSencillo> listaBibliotecariosSencillos = new ArrayList<>(); //lista de bibliotecarios sencillos
    private static final String ARCHIVO = "bibliotecariosSencillos.dat"; //archivo de almacenamiento de bibliotecarios sencillos
    private String usuario;
    private String password;
    private final int id;

    public BibliotecarioSencillo(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
        this.id = contadorId++;
    }

    private static void guardarEnArchivoSencillos() { //guarda la lista de bibliotecarios en el archivo
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(listaBibliotecariosSencillos); //escribe el objeto en el archivo
        } catch (IOException e) {
            e.printStackTrace(); //muestra error en consola
        }
    }

    /*Carga los datos de los bibliotecarios sencillos desde el archivo binario.
     * Si el archivo existe, lo lee y actualiza la lista de bibliotecarios y el contador de ID.
     * Si no existe o hay un error, inicializa la lista como vacía.*/
    private static void cargarDesdeArchivoSencillo() {
        File file = new File(ARCHIVO); //crea un objeto File con la ruta del archivo

        if (!file.exists()) {
            //si el archivo no existe, inicializamos la lista vacía
            listaBibliotecariosSencillos = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            //lee el archivo y convierte su contenido en una lista de BibliotecarioSencillo
            listaBibliotecariosSencillos = (List<BibliotecarioSencillo>) ois.readObject();

            //si la lista no está vacía, actualiza el contador de ID al siguiente disponible
            if (!listaBibliotecariosSencillos.isEmpty()) {
                contadorId = listaBibliotecariosSencillos.getLast().getId() + 1;
            }

        } catch (IOException | ClassNotFoundException e) {
            //si ocurre un error al leer el archivo, mostramos el error y dejamos la lista vacía
            e.printStackTrace();
            listaBibliotecariosSencillos = new ArrayList<>();
        }
    }

    public static void agregarBibliotecario() { //permite agregar un nuevo bibliotecario
        cargarDesdeArchivoSencillo(); //carga la lista antes de agregar

        String usuario = JOptionPane.showInputDialog(null, "Ingrese el nombre del bibliotecario:");
        String password = JOptionPane.showInputDialog(null, "Ingrese la contraseña del bibliotecario:");

        //valida que los campos no sean vacíos ni iguales
        if (usuario != null && password != null && !usuario.isEmpty() && !password.isEmpty()) {
            if (usuario.equals(password)) {
                JOptionPane.showMessageDialog(null, "El usuario y la contraseña no pueden ser iguales.");
                return;
            }

            //válida que el usuario y contraseña no estén ya en uso
            for (BibliotecarioSencillo b : listaBibliotecariosSencillos) {
                if (b.getUsuario().equals(usuario) || b.getPasswordUsuario().equals(password)) {
                    JOptionPane.showMessageDialog(null, "Ya existe un bibliotecario con esas credenciales.");
                    return;
                }
            }

            BibliotecarioSencillo nuevo = new BibliotecarioSencillo(usuario, password); //crea un nuevo objeto
            listaBibliotecariosSencillos.add(nuevo); //lo añade a la lista
            guardarEnArchivoSencillos(); //guarda los cambios
            JOptionPane.showMessageDialog(null, "Bibliotecario agregado correctamente.");
            System.out.println("\n📥 Bibliotecario agregado:\n" + nuevo);
        } else {
            JOptionPane.showMessageDialog(null, "Datos inválidos. No se agregó el bibliotecario.");
        }
    }

    public static void verBibliotecarios() { //muestra la lista de bibliotecarios
        cargarDesdeArchivoSencillo(); //carga la lista antes de ver los datos
        if (listaBibliotecariosSencillos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay bibliotecarios registrados.");
            return;
        }

        //muestra todos los bibliotecarios en un StringBuilder
        StringBuilder texto = new StringBuilder("📘 LISTA DE BIBLIOTECARIOS SENCILLOS\n\n");
        for (BibliotecarioSencillo c : listaBibliotecariosSencillos) {
            texto.append("ID: ").append(c.getId())
                    .append(" | Usuario: ").append(c.getUsuario())
                    .append(" | Contraseña: ").append(c.getPasswordUsuario()).append("\n");
        }

        //muestra el texto en un área de texto con scroll
        JTextArea areaTexto = new JTextArea(texto.toString());
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaTexto.setBackground(Color.BLACK);
        areaTexto.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        JOptionPane.showMessageDialog(null, scrollPane, "Listado de Bibliotecarios Sencillos", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void actualizarBibliotecario() {
        cargarDesdeArchivoSencillo(); //cargamos la lista antes de modificarla

        try {
            //solicita el ID y lo convierte a entero
            String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID del bibliotecario a actualizar:");
            int id = Integer.parseInt(idStr);

            for (BibliotecarioSencillo b : listaBibliotecariosSencillos) {
                if (b.getId() == id) {
                    //pide nuevos datos
                    String nuevoUsuario = JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre de usuario:");
                    String nuevaPassword = JOptionPane.showInputDialog(null, "Ingrese la nueva contraseña:");

                    //verifica que los campos no estén vacíos o nulos
                    if (nuevoUsuario != null && nuevaPassword != null && !nuevoUsuario.isEmpty() && !nuevaPassword.isEmpty()) {
                        b.setUsuario(nuevoUsuario);
                        b.setPasswordUsuario(nuevaPassword);
                        guardarEnArchivoSencillos(); //guarda los cambios en el archivo
                        JOptionPane.showMessageDialog(null, "Bibliotecario actualizado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Datos inválidos. No se actualizó el bibliotecario.");
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
        cargarDesdeArchivoSencillo(); //cargamos los datos antes de eliminar

        try {
            //solicita el ID y lo convierte a entero
            int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del bibliotecario a eliminar:"));
            for (BibliotecarioSencillo b : listaBibliotecariosSencillos) {
                if (b.getId() == id) {
                    listaBibliotecariosSencillos.remove(b); //elimina el bibliotecario
                    guardarEnArchivoSencillos(); //guarda los cambios
                    JOptionPane.showMessageDialog(null, "Bibliotecario eliminado correctamente.");
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "No se encontró el bibliotecario con ese ID.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.");
        }
    }

    //metodo usado para comparar usuario y contraseña que se pasen al metodo con los ya existentes
    public boolean validarBibliotecario(String usuario, String password) {return this.usuario.equals(usuario) && this.password.equals(password);}

    //toString() para imprimir los datos del objeto con estructura
    public String toString() {return "ID: " + id + " | Usuario: " + usuario + " | Contraseña: " + password;}

    // Getters y Setters
    public String getUsuario() {return usuario;}
    public void setUsuario(String nuevoUsuario) {this.usuario = nuevoUsuario;}
    public String getPasswordUsuario() {return password;}
    public void setPasswordUsuario(String nuevaPassword) {this.password = nuevaPassword;}
    public int getId() {return id;}
    public void getCargarDesdeArchivoSencillo() {cargarDesdeArchivoSencillo();}
}
