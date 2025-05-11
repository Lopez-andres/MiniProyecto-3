package Modelo;
import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Libro implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; //version de serial
    private static int contadorId = 1; //contador de libros para asignarle un ID
    private final int id;
    private String titulo;
    private String autor;
    private boolean multaPagada;
    private String categoria;
    private boolean prestado;
    private String personaPrestamo;
    private LocalDate fechaPrestamo;
    private int diasPrestamo;
    private int precioMulta;
    private static final String ARCHIVO = "libro.dat"; //ruta del archivo binario
    public static List<Libro> listaLibros = new ArrayList<>(); //ArrayList que almacena los libros

    public Libro(String titulo, String autor, String categoria) {
        this.id = contadorId++;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
    }

    // Métodos CRUD

    public static void agregarLibro() {
        cargarDesdeArchivoLibro(); //cargar los libros antes de agregar

        String titulo = JOptionPane.showInputDialog(null, "Ingrese el título del libro:");
        String autor = JOptionPane.showInputDialog(null, "Ingrese el autor del libro:");
        String categoria = JOptionPane.showInputDialog(null, "Ingrese la categoría del libro:");

        //verifica que ninguno de los campos sea nulo o vacio
        if (titulo != null && autor != null && categoria != null && !titulo.isEmpty() && !autor.isEmpty() && !categoria.isEmpty()) {
            Libro nuevo = new Libro(titulo, autor, categoria);
            listaLibros.add(nuevo); //lo añadimos a la lista de libros
            guardarEnArchivoLibro(); //guardamos los cambios
            JOptionPane.showMessageDialog(null, "Libro agregado correctamente con ID: " + nuevo.id);
        } else {
            JOptionPane.showMessageDialog(null, "Datos inválidos.");
        }
    }

    public static void verLibros() {
        cargarDesdeArchivoLibro(); //cargar los libros antes de mostrarlos

        if (listaLibros.isEmpty()) {//verifica si la lista de libros esta vacia
            JOptionPane.showMessageDialog(null, "No hay libros registrados.");
            return;
        }

        //muestra todos los libros en un StringBuilder
        StringBuilder sb = new StringBuilder("📚 LISTA DE LIBROS\n\n");
        for (Libro lb : listaLibros) {
            sb.append("\nID: ").append(lb.getId()).append("\n");
            sb.append("Titulo: ").append(lb.getTitulo()).append("\n");
            sb.append("Autor: ").append(lb.getAutor()).append("\n");
            sb.append("Categoria: ").append(lb.getCategoria()).append("\n");
        }

        //mostramos este StringBuilder en un JTextArea junto con un JScrollPane
        JTextArea areaTexto = new JTextArea(sb.toString());
        areaTexto.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 250));
        JOptionPane.showMessageDialog(null, scrollPane, "Listado de Libros", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void actualizarLibro() {
        cargarDesdeArchivoLibro(); //cargar los libros antes de actualizarlos

        String inputId = JOptionPane.showInputDialog(null, "Ingrese el ID del libro a actualizar:");
        if (inputId == null || inputId.trim().isEmpty()) { //validacion de que el id no sea vacio o nulo
            JOptionPane.showMessageDialog(null, "ID inválido.");
            return;
        }

        try {
            int id = Integer.parseInt(inputId); //convertimos el input a int
            for (Libro l : listaLibros) {
                if (l.getId() == id) { //en cada iteracion comprobamos si el id del libro es igual al del input
                    String nuevoTitulo = JOptionPane.showInputDialog(null, "Nuevo titulo (actual: " + l.getTitulo() + "):");
                    String nuevoAutor = JOptionPane.showInputDialog(null, "Nuevo autor (actual: " + l.getAutor() + "):");
                    String nuevaCategoria = JOptionPane.showInputDialog(null, "Nueva categoría (actual: " + l.getCategoria() + "):");

                    //verificamos que los datos no sean nulos o vacios
                    if (nuevoTitulo != null && !nuevoTitulo.isEmpty() && nuevoAutor != null && !nuevoAutor.isEmpty() && nuevaCategoria != null && !nuevaCategoria.isEmpty()) {
                        l.setTitulo(nuevoTitulo); //modificamos el titulo, autor y categoria del libro
                        l.setAutor(nuevoAutor);
                        l.setCategoria(nuevaCategoria);
                        guardarEnArchivoLibro(); //guardamos los cambios en el archivo
                        JOptionPane.showMessageDialog(null, "Libro actualizado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Datos inválidos.");
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Libro no encontrado con ese ID.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Debe ser numérico.");
        }
    }

    public static void eliminarLibro() {
        cargarDesdeArchivoLibro(); //cargar los libros antes de eliminarlos

        String idTexto = JOptionPane.showInputDialog(null, "Ingrese el ID del libro a eliminar:");
        try {
            int idBuscar = Integer.parseInt(idTexto); //convertimos el input a int

            for (Libro l : listaLibros) {
                if (l.getId() == idBuscar) { //en cada iteracion comprobamos si el id del libro es igual al del input
                    listaLibros.remove(l); //eliminamos ese objeto de la lista de libros
                    guardarEnArchivoLibro(); //guardamos los cambios en el archivo
                    JOptionPane.showMessageDialog(null, "Libro eliminado correctamente.");
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Libro no encontrado con ID: " + idBuscar);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido.");
        }
    }

    public static void cargarLibros() {
        File file = new File(ARCHIVO); //creamos el archivo file con la ruta ARCHIVO
        if (file.exists()) { //si el archivo ya existe significa que ya hay datos guardados
            System.out.println("El archivo de libros ya existe. No se sobrescribirá el stock.");
            cargarDesdeArchivoLibro(); //cargamos los datos desde el archivo

            // Actualizar el contadorId al mayor ID + 1
            int maxId = 0;
            for (Libro l : listaLibros) {
                if (l.getId() > maxId) {
                    maxId = l.getId();
                }
            }
            contadorId = maxId + 1; //actualizamos el contadorId con el mayor ID + 1 si luego queremos agregar un libro
            return;
        }

        // Aquí ya sabemos que no existe el archivo, así que se puede crear el stock inicial
        listaLibros.add(new Libro("El Resplandor", "Stephen King", "Terror"));
        listaLibros.add(new Libro("Drácula", "Bram Stoker", "Terror"));
        listaLibros.add(new Libro("Frankenstein", "Mary Shelley", "Terror"));
        listaLibros.add(new Libro("It", "Stephen King", "Terror"));
        listaLibros.add(new Libro("La llamada de Cthulhu", "H.P. Lovecraft", "Terror"));

        listaLibros.add(new Libro("Don Quijote", "Miguel de Cervantes", "Novelas clásicas"));
        listaLibros.add(new Libro("Cien Años de Soledad", "Gabriel García Márquez", "Novelas clásicas"));
        listaLibros.add(new Libro("Orgullo y Prejuicio", "Jane Austen", "Novelas clásicas"));
        listaLibros.add(new Libro("Crimen y Castigo", "Dostoyevski", "Novelas clásicas"));
        listaLibros.add(new Libro("Madame Bovary", "Flaubert", "Novelas clásicas"));

        listaLibros.add(new Libro("Fundamentos de Circuitos", "Alexander", "Ingeniería"));
        listaLibros.add(new Libro("Álgebra Lineal", "Lay", "Ingeniería"));
        listaLibros.add(new Libro("Termodinámica", "Cengel", "Ingeniería"));
        listaLibros.add(new Libro("Estructuras", "Hibbeler", "Ingeniería"));
        listaLibros.add(new Libro("Cálculo", "Stewart", "Ingeniería"));

        guardarEnArchivoLibro(); //guardamos los datos en el archivo
        System.out.println("Stock inicial de libros cargado y guardado en el archivo.");
    }

    public static void guardarEnArchivoLibro() {
        //
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(listaLibros); //se escribe la lista de libros serializada en el archivo
        } catch (IOException e) { //si ocurre un error se imprime la exepcion
            e.printStackTrace();
        }
    }

    public static void cargarDesdeArchivoLibro() {
        //se crea un objeto File que representa el archivo a cargar
        File file = new File(ARCHIVO);
        if (!file.exists()) { //si el archivo no existe significa que no hay datos guardados, y creamos la listaLibros
            listaLibros = new ArrayList<>();
            return;
        }

        //abre el archivo binario
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            listaLibros = (List<Libro>) ois.readObject(); //se lee los datos del archivo y se guardan en la listaLibros
        } catch (IOException | ClassNotFoundException e){ //se captura alguna exepcion que pase y se imprime el error
            e.printStackTrace();
        }
    }

    // CATEGORÍAS (CRUD)

    public static void crearCategoria() {
        cargarDesdeArchivoLibro(); //cargar los libros antes crear una categoria
        String nuevaCategoria = JOptionPane.showInputDialog(null, "Ingrese el nombre de la nueva categoría:");

        //verificamos si ese campo es nulo o vacio y en ese caso mandamos una alerta
        if (nuevaCategoria == null || nuevaCategoria.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nombre inválido.");
            return;
        }

        /*covertimos la lista de libros a un stream() y usamos anyMatch para saber si al menos 1 libro esta relacionado con la nueva categoria
        esto para saber si esa categoria ya existe, si este es el caso, no se crea para evitar duplicidad*/
        boolean existe = listaLibros.stream().anyMatch(l -> l.getCategoria().equalsIgnoreCase(nuevaCategoria));
        if (existe) { //si la categoria ya existe se le avisa y no se crea
            JOptionPane.showMessageDialog(null, "La categoría ya existe.");
        } else {
            JOptionPane.showMessageDialog(null, "Categoría creada. (Se agregará al usarla en un libro)");
        }
    }

    public static void verCategorias() {
        cargarDesdeArchivoLibro(); //cargar los libros antes ver una categoria

        if (listaLibros.isEmpty()) { //verificamos si la lista de libros esta vacia
            JOptionPane.showMessageDialog(null, "No hay libros registrados.");
            return;
        }

        List<String> categoriasUnicas = new ArrayList<>(); //creamos una lista de String con las categorias

        for (Libro l : listaLibros) { //recorremos la lista de libros
            if (!categoriasUnicas.contains(l.getCategoria())) { //si la categoria no esta en la lista de categorias unicas
                categoriasUnicas.add(l.getCategoria()); //entonces añadimos esa categoria al ArrayList
            }
        }

        if (categoriasUnicas.isEmpty()) {//si la lista de categorias unicas esta vacia, entonces no hay categorias registradas
            JOptionPane.showMessageDialog(null, "No hay categorías registradas.");
            return;
        }

        //mostramos todas las categorias en un formato StringBuilder
        StringBuilder sb = new StringBuilder("📂 Categorías disponibles:\n\n");
        for (String cat : categoriasUnicas) { //mostramos todas las categorias
            sb.append("- ").append(cat).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static void actualizarCategoria() {
        cargarDesdeArchivoLibro(); //cargamos desde el archivo antes de actualizar una categoria

        String categoriaAntigua = JOptionPane.showInputDialog(null, "Ingrese el nombre de la categoría a actualizar:");
        String nuevaCategoria = JOptionPane.showInputDialog(null, "Ingrese el nuevo nombre:");

        //si los campos son nulos o vacios, mandamos una alerta
        if (categoriaAntigua == null || nuevaCategoria == null || categoriaAntigua.trim().isEmpty() || nuevaCategoria.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Datos inválidos.");
            return;
        }

        boolean modificada = false;
        for (Libro l : listaLibros) {//recorremos la lista de libros y obtenemos la categoria de cada uno
            if (l.getCategoria().equalsIgnoreCase(categoriaAntigua)) { //si la categoria de ese libro es igual a la que queremos modificar
                l.setCategoria(nuevaCategoria); //entonces le actualizamos la categoria del libro
                modificada = true; //modificamos correctamente
            }
        }

        if (modificada) {
            guardarEnArchivoLibro(); //guardamos los cambios en el archivo
            JOptionPane.showMessageDialog(null, "Categoría actualizada correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Categoría no encontrada.");
        }
    }

    public static void eliminarCategoria() {
        cargarDesdeArchivoLibro(); //cargamos desde el archivo antes de eliminar una categoria

        String categoriaEliminar = JOptionPane.showInputDialog(null, "Ingrese el nombre de la categoría a eliminar:");
        if (categoriaEliminar == null || categoriaEliminar.trim().isEmpty()) { //validamos de que no sea un dato nulo o vacio
            JOptionPane.showMessageDialog(null, "Nombre inválido.");
            return;
        }

        //convertimos la listaLibros() en un stream() y usamos anyMatch para saber si la categoria a eliminar si existe en algun libro
        boolean encontrada = listaLibros.stream().anyMatch(l -> l.getCategoria().equalsIgnoreCase(categoriaEliminar));
        if (!encontrada) {
            JOptionPane.showMessageDialog(null, "Categoría no encontrada.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(null,
                "¿Desea borrar la categoría de todos los libros? (quedarán sin categoría)",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            for (Libro l : listaLibros) {//recorremos todos los libros y comparamos si tienen la categoria que queremos eliminar
                if (l.getCategoria().equalsIgnoreCase(categoriaEliminar)) {
                    l.setCategoria("Sin categoría"); //estos libros quedan sin categoria
                }
            }
            guardarEnArchivoLibro();//guardamos los cambios en el archivo
            JOptionPane.showMessageDialog(null, "Categoría eliminada de los libros.");
        }
    }


    //getters y setters
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isPrestado() { return prestado; }
    public void setPrestado(boolean prestado) { this.prestado = prestado; }
    public String getPersonaPrestamo() { return personaPrestamo; }
    public void setPersonaPrestamo(String personaPrestamo) { this.personaPrestamo = personaPrestamo; }
    public void setDiasPrestamo(int diasPrestamo) {this.diasPrestamo = diasPrestamo;}

    //getter encargado de calcular la diferencia entre la fecha de prestamo y devolucion
    public int getDiasPrestamo(LocalDate fechaDevolucion, LocalDate fechaPrestamo) {
        if (fechaPrestamo == null || fechaDevolucion == null) return 0;
        return (int) ChronoUnit.DAYS.between(fechaPrestamo, fechaDevolucion);
    }

    public void setPrecioMulta(int precioMulta) {this.precioMulta = precioMulta;}
    public void setMultaPagada(boolean multaPagada) {this.multaPagada = multaPagada;}
    public boolean isMultaPagada() {return multaPagada;}
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public int getId() {return id;}
    public void setTitulo(String titulo) {this.titulo = titulo;}
}

