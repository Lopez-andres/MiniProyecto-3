package Modelo;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

//clase de tipo record, ya que es inmutable y solo contiene datos
public record UsuarioMora(String nombre, String tituloLibro, int multa) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; //version del serial
    private static final String ARCHIVO = "usuarios_mora.dat"; //archivo binario donde se guardan los usuarios mora
    public static List<UsuarioMora> listaUsuariosMora = new ArrayList<>(); //lista de usuarios mora

    // Constructor para crear un nuevo usuario mora

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Libro: " + tituloLibro + ", Multa: $" + multa;
    }

    // Métodos para guardar y cargar desde archivo

    public static void guardarEnArchivoMora() { //se abre un flujo de salida hacia la ruta del archivo
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(listaUsuariosMora); //escribe en el archivo el objeto completo listaUsuariosMora
        } catch (IOException e) { //se captura la exepcion ante un error , luego imprime en la consola la exepcion
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar los usuarios mora: " + e.getMessage());
            System.exit(0); //termina la ejecucion del programa con error
        }
    }

    public static void cargarDesdeArchivoMora() {
        File file = new File(ARCHIVO); //crea un archivo con la ruta ARCHIVO
        if (!file.exists()) { //si el archivo no existe significa que no hay datos guardados
            listaUsuariosMora = new ArrayList<>();
            return;
        }

        //se abre el archivo para leer objetos desde él
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            listaUsuariosMora = (List<UsuarioMora>) ois.readObject(); //se lee el objeto guardado y pasa a la listaUsuariosMora
        } catch (IOException | ClassNotFoundException e) { //Captura las posibles exepciones
            e.printStackTrace();
        }
    }
}
