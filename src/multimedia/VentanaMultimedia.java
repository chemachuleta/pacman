package multimedia;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

// NOTA: los códigos de los colores están en forma de constantes en Color.ZZZ
// pero se pueden crear en el momento mediante "new Color(int, int, int)".

public class VentanaMultimedia extends JFrame implements Lienzo {
	private static final long serialVersionUID = 1L;

	private static final int PIXELES_EXTRA_ALTO_VENTANA = 28;
	private static final int PIXELES_EXTRA_ANCHO_VENTANA = 6;

	private int ancho, alto;
	private int tamPixel;
	private Color colorFondo;

	private GraphicsConfiguration configuracionGraficaSistema;

	private Canvas canvas;

	private BufferedImage imagenEnBuffer;
	private Graphics2D imagenG2D;

	private Teclado teclado = new Teclado();

    public Graphics2D getGraphics2D() {
        return this.imagenG2D;
    }

	public VentanaMultimedia(String tituloVentana, int ancho, int alto, int tamPixel, Color colorFondo) {
		super(tituloVentana);

		this.ancho = ancho;
		this.alto = alto;
		this.tamPixel = tamPixel;
		this.colorFondo = colorFondo;

        // Se obtiene la configuración gráfica del sistema y se guarda,
        // ya que será necesaria para algunas cuestiones.
        GraphicsEnvironment entornoGrafico = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice adaptadorGrafico = entornoGrafico.getDefaultScreenDevice();
        configuracionGraficaSistema = adaptadorGrafico.getDefaultConfiguration();

        // A continuación vienen aspectos de "ventanas Java".

        // Se impide redimensionar la ventana.
        setResizable(false);

        // Se le dice que al cerrar la ventana se termine la aplicación.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Se crea un nuevo componente (java.awt.Canvas) de tipo lienzo (Canvas).
        canvas = new Canvas();
        add(canvas); // Y se añade a este JFrame.

        ajustarTamannoMarco();

        // Se pide ignorar el repintado. Necesario al usar doble búffer.
        setIgnoreRepaint(true);
        canvas.setIgnoreRepaint(true);

        // Se ordena al JFrame que procese todo lo que le hemos dicho.
        pack();

        // Se indica que la ventana deberá ser visible a partir de este momento.
        setVisible(true);

        // Se crea una estrategia de doble búffer, para poder actualizar la
        // pantalla sin parpadeos (solo se modifica la imagen visible cuando
        // se tiene listo el "siguiente fotograma").
        canvas.createBufferStrategy(2);

        // Se "engancha" nuestro teclado al JFrame y al Canvas, para que ambos
        // informen de las teclas pulsadas que les lleguen.
        // Es necesario engancharlo a AMBOS. Al JFrame, para cuando está activo él
        // (=tenga el "foco") y al Canvas para cuando sí está activo el Canvas.
        this.addKeyListener(teclado);
        canvas.addKeyListener(teclado);
	}



    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public Teclado getTeclado() {
        return teclado;
    }



    private void ajustarTamannoMarco() {
        // TODO Para poner un área arriba y otra abajo para texto, es aquí donde hay que tocar.

        // Se calcula e indica la posición X, Y así como el tamaño X, Y del JFrame.
        // La posición se calcula para que la ventana quede centrada en la pantalla.
        // Se suman algunos píxeles adicionales para tener en cuenta que el JFrame
        // tiene un título y unos bordes que también ocupan algunos píxeles.
        int posXMarco = (configuracionGraficaSistema.getBounds().width - (ancho*tamPixel + PIXELES_EXTRA_ANCHO_VENTANA)) / 2;
        int posYMarco = (configuracionGraficaSistema.getBounds().height - (alto*tamPixel + PIXELES_EXTRA_ALTO_VENTANA)) / 2;
        setBounds(posXMarco, posYMarco, ancho*tamPixel + PIXELES_EXTRA_ANCHO_VENTANA, alto*tamPixel + PIXELES_EXTRA_ALTO_VENTANA);

        // Se le establece el tamaño al lienzo.
        canvas.setSize(ancho*tamPixel, alto*tamPixel);
    }

    public void limpiar() {
        imagenEnBuffer = configuracionGraficaSistema.createCompatibleImage(ancho*tamPixel, alto*tamPixel);

        imagenG2D = imagenEnBuffer.createGraphics();
        imagenG2D.setColor(colorFondo);
        imagenG2D.fillRect(0, 0, ancho*tamPixel, alto*tamPixel);
    }

    // Ejecutar este método requiere haber llamado previamente a limpiar().
    public void marcarPixel(int x, int y, Color color) {
        int col = x;
        int fil = y;

        // Se pinta el pixel sobre el Graphics2D de la imagen que tenemos en el buffer.
        imagenG2D.setColor(color);
        imagenG2D.fillRect(col*tamPixel, fil*tamPixel, tamPixel, tamPixel);
    }

    public void dibujarImagen(int x, int y, Image imagen) {
        int col = x;
        int fil = y;

        imagenG2D.drawImage(imagen, col*tamPixel, fil*tamPixel, null);
    }

    public void escribirTexto(int x, int y, String texto, Color color) {
        int col = x;
        int fil = y;

        // Actualmente, la altura del texto puede sobrepasar la altura
        // de un pixel y, en este momento, no sé cómo se ajusta.
        // Habrá que hacer un imagenG2D.getFont(), modificar el font
        // y finalmente un setFont(), pero no sé cómo modificar el font.

        imagenG2D.setColor(color);

        // TODO Esto puede ser interesante.
        // imagenG2D.setFont(new Font("Arial", Font.BOLD, (int)(tamPixel*0.7)));

        // Se realiza un ajuste ya que la posición indicada corresponde al
        // punto inferior izquierdo del texto (no al superior izquierdo).
        imagenG2D.drawString(texto, col*tamPixel, (fil+1)*tamPixel);
    }

    // Ejecutar este método requiere haber llamado previamente a limpiar(),
    // y, si se quiere representar algo, también a setPixel(), claro.
    public void volcar() {
        BufferStrategy buffer = canvas.getBufferStrategy();
        Graphics graphics = buffer.getDrawGraphics();

        graphics.drawImage(imagenEnBuffer, 0, 0, null);

        // Este if es por cosas de Windows.
        if (!buffer.contentsLost()) buffer.show();
    }

    public int getTamX() {
        return ancho;
    }

    public int getTamY() {
        return alto;
    }

    public void redimensionar(int tamX, int tamY) {
        ancho = tamX;
        alto = tamY;

        ajustarTamannoMarco();
    }
}
