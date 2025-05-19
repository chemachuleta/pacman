package juego;

import multimedia.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Coordinador implements Dibujable {
    private List<Nivel> niveles;
    private int nivelActual;
    private EstadoJuego estado;
    private Mapa mapa;
    private Pacman pacman;
    private ArrayList<Fantasma> fantasmas = new ArrayList<>();
    private Lienzo lienzo;
    private Teclado teclado;

    public Coordinador(Lienzo lienzo, Teclado teclado) {
        this.lienzo = lienzo;
        this.teclado = teclado;
        this.niveles = Nivel.crearNiveles();
        this.nivelActual = 0;

        iniciarNivel(niveles.get(nivelActual));
    }

    public void setLienzo(Lienzo lienzo) {
        this.lienzo = lienzo;
    }

    private void iniciarNivel(Nivel nivel) {
        int puntuacionActual = estado != null ? estado.getPuntuacion() : 0;
        estado = new EstadoJuego(lienzo);
        estado.setPuntuacion(puntuacionActual);

        mapa = new Mapa(lienzo);
        mapa.cargarNivel(nivel);
        situarActores();
    }

    private void situarActores() {
        // Posicionar Pacman en una ubicación válida
        Posicion posicionPacman = obtenerPosicionValidaParaPacman();
        pacman = new Pacman(this, lienzo, teclado, mapa, estado);
        pacman.getPosicion().setX(posicionPacman.getX());
        pacman.getPosicion().setY(posicionPacman.getY());

        // Posicionar fantasmas
        fantasmas.clear();
        Nivel nivelActual = niveles.get(this.nivelActual);
        for (int i = 0; i < nivelActual.getNumeroFantasmas(); i++) {
            Posicion posicionFantasma = obtenerPosicionValidaParaFantasma();
            Fantasma fantasma = new Fantasma(this, lienzo, pacman, mapa);
            fantasma.getPosicion().setX(posicionFantasma.getX());
            fantasma.getPosicion().setY(posicionFantasma.getY());
            fantasmas.add(fantasma);
        }
    }

    private Posicion obtenerPosicionValidaParaPacman() {
        // Posición central garantizada como válida en la mayoría de niveles
        int centroX = mapa.getAncho() / 2;
        int centroY = mapa.getAlto() / 2;

        // Verificar que la posición central sea transitable
        if (mapa.esTransitable(new Posicion(centroX, centroY))) {
            return new Posicion(centroX, centroY);
        }

        // Si el centro no es válido, buscar una posición alternativa
        return buscarPosicionTransitableAleatoria();
    }

    private Posicion obtenerPosicionValidaParaFantasma() {
        // Buscar posición alejada de Pacman
        Posicion posicion;
        do {
            posicion = buscarPosicionTransitableAleatoria();
        } while (posicion.distanciaA(pacman.getPosicion()) < 5); // Mínima distancia de 5 celdas

        return posicion;
    }

    private Posicion buscarPosicionTransitableAleatoria() {
        Random random = new Random();
        Posicion posicion;
        do {
            posicion = new Posicion(
                    random.nextInt(mapa.getAncho()),
                    random.nextInt(mapa.getAlto())
            );
        } while (!mapa.esTransitable(posicion));

        return posicion;
    }

    private boolean estaLibre(Posicion posicion) {
        if (!mapa.esTransitable(posicion)) return false;

        if (posicion.equals(pacman.getPosicion())) return false;

        for (Fantasma fantasma : fantasmas) {
            if (posicion.equals(fantasma.getPosicion())) return false;
        }

        return true;
    }

    public EstadoJuego getEstado() {
        return this.estado;
    }

    public Posicion obtenerPosicionVaciaAleatoria() {
        Posicion posicion;

        do {
            posicion = new Posicion(mapa);
        } while (!estaLibre(posicion));

        return posicion;
    }

    public void tick() throws PacmanComidoException, SalirDelJuegoException,
            NivelCompletadoException, JuegoCompletadoException {

        // Primero verifica colisiones antes de mover
        for (Fantasma fantasma : fantasmas) {
            if (fantasma.getPosicion().equals(pacman.getPosicion())) {
                throw new PacmanComidoException("¡Game Over!");
            }
        }

        pacman.tick();

        // Verificación después de mover Pacman
        for (Fantasma fantasma : fantasmas) {
            if (fantasma.getPosicion().equals(pacman.getPosicion())) {
                throw new PacmanComidoException("¡Game Over!");
            }
        }

        // Lógica de puntos y niveles...
        if (mapa.hayPunto(pacman.getPosicion())) {
            estado.incrementarPuntuacion();
            mapa.retirarPunto(pacman.getPosicion());

            if (!mapa.quedanMonedas()) {
                if (nivelActual + 1 < niveles.size()) {
                    throw new NivelCompletadoException("¡Nivel completado!");
                } else {
                    throw new JuegoCompletadoException("¡Juego completado!");
                }
            }
        }

        // Mover fantasmas y verificar colisiones nuevamente
        for (Fantasma fantasma : fantasmas) {
            fantasma.tick(); // Esto puede lanzar PacmanComidoException
        }
    }

    public void dibujar() {
        lienzo.limpiar();

        mapa.dibujar();
        pacman.dibujar();

        for (Fantasma fantasma : fantasmas) {
            fantasma.dibujar();
        }

        estado.dibujar();

        lienzo.volcar();
    }

    public int getNivelActual() {
        return this.nivelActual;
    }

    public void pasarSiguienteNivel() {
        nivelActual++;  // Primero incrementamos
        if (nivelActual < niveles.size()) {  // Luego verificamos
            iniciarNivel(niveles.get(nivelActual));
            situarActores();
        } else {
            // Opcional: reiniciar el juego o mostrar mensaje de finalización
            System.out.println("¡Has completado todos los niveles!");
        }
    }

}
