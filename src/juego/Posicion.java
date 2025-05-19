package juego;

import java.util.Random;

public class Posicion {
    private Random random = new Random();

    private int x;
    private int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Posicion(Mapa mapa) {
        this.x = random.nextInt(mapa.getAncho());
        this.y = random.nextInt(mapa.getAlto());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Posicion otra) {
        return this.x == otra.x && this.y == otra.y;
    }

    public Posicion desplazar(Direccion dir) {
        return switch (dir) {
            case ARR -> new Posicion(x  , y-1);
            case ABA -> new Posicion(x  , y+1);
            case IZD -> new Posicion(x-1, y  );
            case DCH -> new Posicion(x+1, y  );
        };
    }

    public double distanciaA(Posicion otra) {
        return Math.sqrt(Math.pow(this.x - otra.x, 2) + Math.pow(this.y - otra.y, 2));
    }
}