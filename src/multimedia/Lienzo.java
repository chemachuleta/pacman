package multimedia;

import java.awt.*;

public interface Lienzo {
	public int getTamX();
	public int getTamY();
	public void redimensionar(int tamX, int tamY);

	public void limpiar();
	public void marcarPixel(int x, int y, Color color);
    public void dibujarImagen(int x, int y, Image imagen);
    public void escribirTexto(int x, int y, String texto, Color color);
	public void volcar();
}