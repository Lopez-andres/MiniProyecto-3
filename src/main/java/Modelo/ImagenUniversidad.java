package Modelo;

import javax.swing.*;
import java.awt.*;

//sirve para mostrar una imagen dentro de un panel grafico en una interfaz con swing
public class ImagenUniversidad extends JPanel {
    private transient final Image imagen;

     /*Constructor que inicializa el panel con una imagen desde la ruta especificada.
     Establece un tamaño preferido para el panel.*/

    public ImagenUniversidad(String rutaImagen) {
        ImageIcon icono = new ImageIcon(rutaImagen);
        this.imagen = icono.getImage();
        setPreferredSize(new Dimension(250, 250));
    }

     /*Metodo sobrescrito para dibujar la imagen en el panel.
     Si la imagen no se encuentra, muestra un mensaje de error en rojo.

     @param g Contexto gráfico en el que se dibuja*/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 20, 15, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.RED);
            g.drawString("Imagen no encontrada", 10, 20);
        }
    }
}
