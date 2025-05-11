package Vista;
import Modelo.Libro;
import Modelo.UsuarioMora;
import Modelo.ImagenUniversidad;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Biblioteca extends JFrame {
    private JPanel panelPrincipal;
    private JButton registrarPrestamoButton, registrarDevolucionButton, verLibrosButton;
    private JButton verUsuariosEnMoraButton, regresarButton, salirButton, librosCategoriasButton;
    private JPanel imagenBiblioteca, funcionalidadButtons, funcionalidadAcciones;

    public Biblioteca() {
        ImagenUniversidad img2 = new ImagenUniversidad("src/main/java/biblioteca3.png");
        imagenBiblioteca.setLayout(new BorderLayout());
        imagenBiblioteca.add(img2);

        setTitle("Biblioteca");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(610, 490);
        setLocationRelativeTo(null);

        Libro.cargarLibros();
        Libro.cargarDesdeArchivoLibro();  //cuando se abra la biblioteca se cargan los libros desde el archivo

        //llamada a cada metodo de esta clase para la logica de la biblioteca
        registrarPrestamoButton.addActionListener(_ -> registrarPrestamo());
        registrarDevolucionButton.addActionListener(_ -> registrarDevolucion());
        verLibrosButton.addActionListener(_ -> mostrarLibrosPorCategoria());
        verUsuariosEnMoraButton.addActionListener(_ -> mostrarUsuariosEnMora());
        salirButton.addActionListener(_ -> System.exit(0));
        regresarButton.addActionListener(_ -> {
            this.dispose();
            Login login = new Login();
            login.setVisible(true);
        });
        librosCategoriasButton.addActionListener(_ -> {
            GestionLibros dialogo = new GestionLibros(Biblioteca.this, true); // true = modal
            dialogo.setVisible(true);
        });
    }

    // Se encarga de mostrar los libros por categoria en la biblioteca
    private void mostrarLibrosPorCategoria() {
        Libro.cargarDesdeArchivoLibro(); //cargamos los libros desde el archivo antes de mostrarlos

        if (Libro.listaLibros.isEmpty()) { //verifica que la listaLibros esta vacio, en este caso manda una alerta
            JOptionPane.showMessageDialog(null, "No hay libros registrados.");
        }

        // Obtener categorías únicas
        ArrayList<String> categoriasUnicas = new ArrayList<>(); //crea un ArrayList que almacena cadenas
        for (Libro libro : Libro.listaLibros) { //recorre la lista de libros
            String categoria = libro.getCategoria(); //en cada recorrido obtiene la categoria del libro
            if (categoria != null && !categoriasUnicas.contains(categoria)) { //si es una categoria que no es nula y no esta en la lista la agrega
                categoriasUnicas.add(categoria); //esto verifica que no se agreguen categorias repetidas, si ya esta en la lista no la agrega
            }
        }

        if (categoriasUnicas.isEmpty()) { //si la lista de categorias es vacia manda una alerta
            JOptionPane.showMessageDialog(null, "No hay categorías disponibles.");
            return;
        }

        // Convertir lista a arreglo para usar en el JOptionPane
        String[] categorias = new String[categoriasUnicas.size()]; //obtenemos el tamaño de la lista para el arreglo
        for (int i = 0; i < categoriasUnicas.size(); i++) { //recorre la lista de categorias y la agrega al arreglo
            categorias[i] = categoriasUnicas.get(i); //cada categoria del ArrayList se agrega al arreglo
        }

        // Mostrar opción para seleccionar categoría en un JOptionPane, se empieza mostrando en la posicion 0
        String seleccion = (String) JOptionPane.showInputDialog(null, "Seleccione una categoría:", "Categorías", JOptionPane.QUESTION_MESSAGE,
                null, categorias, categorias[0]
        );

        // Mostrar libros según categoría seleccionada
        if (seleccion != null) { //si la seleccion no es nula entonces:
            StringBuilder sb = new StringBuilder("📚 Libros en la categoría: " + seleccion + "\n\n");
            boolean hayLibros = false; //significa que no hay libros empezando

            for (Libro libro : Libro.listaLibros) { //recorre la lista de libros
                if (libro.getCategoria().equalsIgnoreCase(seleccion)) { //obtenemos la categoria del libro y compara con la seleccionada por el Bibliotecario para filtrar solo esta
                    sb.append("- ").append(libro.getTitulo()) //obtenemos la categoria del libro, titulo, si esta prestado o no y agregamos al StringBuilder
                            .append(" (").append(libro.isPrestado() ? "Prestado" : "Disponible").append(")\n");
                    hayLibros = true; //significa que si hay libros para esa categoria
                }
            }

            if (!hayLibros) {
                sb.append("No hay libros en esta categoría.");
            }
            //finalmente muestra los libros para cada categoria seleccionada y les da un formato de toString()
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }

    private void registrarPrestamo(){
        Libro.cargarDesdeArchivoLibro(); //cargamos los libros desde el archivo antes de mostrarlos
        ArrayList<Libro> disponibles = new ArrayList<>(); //creamos un ArrayList de disponibles para libros no prestados
        for (Libro l : Libro.listaLibros) { //recorremos la lista de libros
            if (!l.isPrestado()) { //miramos si esta disponible y lo agregamos al ArrayList
                disponibles.add(l);
            }
        }

        if (disponibles.isEmpty()) { //Verifica que la listaLibros sea vacia, en este caso manda una alerta
            JOptionPane.showMessageDialog(null, "No hay libros disponibles para préstamo.");
            return;
        }

        JComboBox<String> comboLibros = new JComboBox<>(); //Creamos un JComboBox para mostrar los libros disponibles en el JOptionPane

        for (Libro l : disponibles) {
            comboLibros.addItem(l.getTitulo() + " | " + l.getAutor()); //al JComboBox mostramos el titulo de cada libro disponible
        }

        //indice_libro puede ser 0 para ok, -1 para cancelar y 2 como que presiono la X
        int indice_libro = JOptionPane.showConfirmDialog(null, comboLibros, "Seleccione un libro para prestar", JOptionPane.OK_CANCEL_OPTION);

        if (indice_libro == JOptionPane.OK_OPTION) {
            Libro libro = disponibles.get(comboLibros.getSelectedIndex()); //obtenemos el indice del libro seleccionado en el JComboBox
            String nombrePersona = JOptionPane.showInputDialog("Nombre de la persona que toma el préstamo:"); //pedimos el nombre de la persona
            String fechaStr = JOptionPane.showInputDialog("Ingrese la fecha de préstamo (AAAA-MM-DD):"); //pedimos la fecha del prestamo

            //si esos datos son vacios o nulos, nos muestra una alerta
            if (nombrePersona == null || nombrePersona.trim().isEmpty() || fechaStr == null || fechaStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Datos incorrectos. Intente nuevamente.");
                return;
            }

            try {
                LocalDate fechaPrestamo = LocalDate.parse(fechaStr); //convertimos la fechaStr a LocalDate
                LocalDate hoy = LocalDate.now(); //fecha actual

                // Validar que la fecha de préstamo sea exactamente hoy
                if (!fechaPrestamo.isEqual(hoy)) {
                    JOptionPane.showMessageDialog(null, "La fecha de préstamo debe ser exactamente la fecha de hoy: " + hoy);
                    return;
                }

                libro.setPrestado(true); //marcamos el libro como prestado
                libro.setPersonaPrestamo(nombrePersona); //asignamos el prestamo al nombre de la persona
                libro.setFechaPrestamo(fechaPrestamo); //asignamos la fecha de prestamo al libro
                libro.setMultaPagada(false); //al momento no hay multas y el precio es 0
                libro.setPrecioMulta(0);

                Libro.guardarEnArchivoLibro();  // Guardamos los cambios en el archivo
                JOptionPane.showMessageDialog(null, "Libro prestado correctamente a " + nombrePersona + " con fecha de prestamo " + libro.getFechaPrestamo() + ".");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Fecha inválida. Use el formato correcto (AAAA-MM-DD).");
            }
        }
    }

    private void registrarDevolucion() {
        Libro.cargarDesdeArchivoLibro(); //cargamos los libros desde el archivo antes de mostrarlos
        UsuarioMora.cargarDesdeArchivoMora(); // Cargar los usuarios morosos actuales

        ArrayList<Libro> prestados = new ArrayList<>(); //creamos un ArrayList de prestados para libros prestados
        for (Libro l : Libro.listaLibros) {
            if (l.isPrestado()) { //si el libro es prestado lo añadimos al ArrayList
                prestados.add(l);
            }
        }

        if (prestados.isEmpty()) { //si todos los libros estan disponibles mandamos una alerta
            JOptionPane.showMessageDialog(null, "No hay libros en préstamo actualmente.");
            return;
        }

        JComboBox<String> comboLibros = new JComboBox<>(); //creamos un JComboBox con los libros prestados

        for (Libro l : prestados) { //cada libro prestado lo añadimos al JComboBox
            comboLibros.addItem(l.getTitulo() + " (prestado a " + l.getPersonaPrestamo() + ")");
        }

        //indice_libro puede ser 0 para ok, -1 para cancelar y 2 como que presiono la X
        int seleccion = JOptionPane.showConfirmDialog(null, comboLibros, "Seleccione un libro a devolver", JOptionPane.OK_CANCEL_OPTION);
        if (seleccion == JOptionPane.OK_OPTION) {
            Libro libro = prestados.get(comboLibros.getSelectedIndex()); //obtenemos el indice del libro seleccionado para devolver
            String fechaStr = JOptionPane.showInputDialog("Ingrese la fecha de devolución (AAAA-MM-DD):");

            try {
                LocalDate fechaDevolucion = LocalDate.parse(fechaStr); //convertimos la fechaStr a LocalDate
                LocalDate fechaPrestamo = libro.getFechaPrestamo(); //obtenemos la fecha de prestamo del libro

                // Validamos que la fecha de devolución no sea anterior a la fecha de préstamo
                if (fechaDevolucion.isBefore(fechaPrestamo)) {
                    throw new IllegalArgumentException("La fecha de devolución no puede ser anterior a la fecha de préstamo."); //creamos una exepcion
                }
                int dias = libro.getDiasPrestamo(fechaDevolucion, fechaPrestamo); //calculamos los dias entre la fecha de devolución y la fecha de préstamo
                int multa = 0; //hasta el momento no hay multa y el precio es 0
                boolean pagoMulta = true;
                String nombrePersona = libro.getPersonaPrestamo(); //obtenemos el nombre de la persona que hizo el prestamo

                if (dias > 7) { //si la diferencia entre fechas es mayor a 7 empezamos a cobrar 1000 por dia
                    multa = (dias - 7) * 1000;
                    libro.setPrecioMulta(multa); //asignamos el precio de la multa al libro

                    //preguntamos si la multa sera pagada o no
                    int pagar = JOptionPane.showConfirmDialog(null,
                            "El libro tiene " + (dias - 7) + " días de atraso.\nMulta: $" + multa + "\n¿Desea pagar la multa?",
                            "Pago de Multa", JOptionPane.YES_NO_OPTION);

                    // Actualizar el estado de la multa pagada según la respuesta
                    libro.setMultaPagada(pagar == JOptionPane.YES_OPTION);

                    if (!libro.isMultaPagada()) { //si la multa no fue pagada entonces: añadimos ese usuario a la lista de usuarios morosos y guardamos en el archivo
                        UsuarioMora nuevoMoroso = new UsuarioMora(nombrePersona, libro.getTitulo(), multa);
                        UsuarioMora.listaUsuariosMora.add(nuevoMoroso);
                        UsuarioMora.guardarEnArchivoMora(); //Guardar inmediatamente el moroso
                    }
                } else { //si la diferencia de dias es menor a 7 entonces no hay multa y el precio es 0
                    libro.setMultaPagada(true);
                    libro.setPrecioMulta(0);
                }

                //modificamos todos los datos del libro para que pueda prestarse
                libro.setPrestado(false);
                libro.setPersonaPrestamo(null);
                libro.setDiasPrestamo(0);
                libro.setFechaPrestamo(null);
                Libro.guardarEnArchivoLibro(); //Guardar cambios en libros

                String mensaje = "Libro devuelto correctamente.\nDías de préstamo: " + dias + "\n";

                //si hay multa muestra el mensaje de usuario en mora, sino hay multa muestra el mensaje de pago realizado correctamente
                mensaje += multa > 0 ? "Multa: $" + multa + (libro.isMultaPagada() ? "" : " ⚠️ Usuario en mora") : "No hay multa, pago realizado correctamente!";
                JOptionPane.showMessageDialog(null, mensaje);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Fecha inválida. Use el formato correcto (AAAA-MM-DD).");
            }
        }
    }

    private void mostrarUsuariosEnMora() {
        //Cargar la lista de morosos desde el archivo antes de mostrarla
        UsuarioMora.cargarDesdeArchivoMora();

        if (UsuarioMora.listaUsuariosMora.isEmpty()) { //si no hay morosos envia un mensaje
            JOptionPane.showMessageDialog(null, "No hay usuarios en mora.");
        } else {
            StringBuilder sb = new StringBuilder("Usuarios en mora:\n");
            int index = 1;

            for (UsuarioMora usuario : UsuarioMora.listaUsuariosMora) { //recorremos la lista de usuarios morosos y los mostramos en el StringBuilder
                sb.append(index).append(". Nombre: ").append(usuario.nombre()) //obtenemos el nombre del usuario, el libro y la multa
                        .append(", Libro: ").append(usuario.tituloLibro())
                        .append(", Multa: $").append(usuario.multa()).append("\n");
                index++;
            }

            JOptionPane.showMessageDialog(null, sb.toString()); //mostramos la lista de morosos

            String input = JOptionPane.showInputDialog("Ingrese el número del usuario al que desea pagar la multa (o deje vacío para cancelar):");

            if (input != null && !input.trim().isEmpty()) { //verificamos que el campo de usuario al pagar la multa no sea vacio o nulo
                try {
                    int seleccion = Integer.parseInt(input); //convertimos el input a int para verificar que sea un numero entero
                    if (seleccion >= 1 && seleccion <= UsuarioMora.listaUsuariosMora.size()){ //verificamos que esa seleccion si este en el rango de usuarios existentes
                        UsuarioMora usuarioSeleccionado = UsuarioMora.listaUsuariosMora.get(seleccion - 1); //restamos 1 para que el indice empiece en 0 correspondiente con los ArrayList

                        int confirmar = JOptionPane.showConfirmDialog(null,
                                "¿Desea pagar la multa de $" + usuarioSeleccionado.multa() +
                                        " por el libro \"" + usuarioSeleccionado.tituloLibro() + "\"?",
                                "Confirmar pago", JOptionPane.YES_NO_OPTION);

                        if (confirmar == JOptionPane.YES_OPTION) { //si el usuario pago la multa, lo eliminamos y guardamos los cambios en el archivo
                            UsuarioMora.listaUsuariosMora.remove(usuarioSeleccionado);
                            UsuarioMora.guardarEnArchivoMora();  //Guardar cambios después del pago
                            JOptionPane.showMessageDialog(null, "Pago realizado con éxito. El usuario ya no está en mora.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Pago no realizado. El usuario sigue en mora.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Número de usuario no válido.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Entrada no válida.");
                }
            }
        }
    }
}
