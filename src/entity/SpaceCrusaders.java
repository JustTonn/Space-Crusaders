package entity;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class SpaceCrusaders extends JPanel implements ActionListener, KeyListener {

    private EstadoDoJogo estado = EstadoDoJogo.JOGANDO; // gerencia o estado do jogo.

    int tileSize = 64; // tamanho de cada quadradinho da tela.
    int colunas = 20; // quantos quadradinhos tem na vertical.
    int linhas = 10; // quantos quadradinhos tem na horizontal
    int larguraQuadro = tileSize * colunas;
    int alturaQuadro = tileSize * linhas;

    Image naveImg; // sprite da nave.
    Image alienImg; // sprite dos inimigos.
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    ArrayList<Image> alienImagemArray;

    // nave

    int naveX = tileSize;
    int naveAltura = tileSize;
    int naveY = (alturaQuadro / 2) - (naveAltura / 2);
    int naveLargura = tileSize * 2;
    int naveVelocidadeY = 3;

    // Inimigos

    ArrayList<Alien> aliens;
    int alienAltura = tileSize;
    int alienLargura = tileSize;
    int alienX = 1280 - tileSize;
    int alienY = tileSize;

    int alienLinha = 3;
    int alienColunas = 1;
    int aliensDerrotados = 0;
    int pontosAlien = 1;
    int alienVelocidadeY = 1;

    public class Nave extends Bloco {
        public Nave(int x, int y, int largura, int altura, Image img) {
            super(x, y, largura, altura, img);
        }

    }

    public class Alien extends Bloco {
        public Alien(int x, int y, int largura, int altura, Image img) {
            super(x, y, largura, altura, img);
        }

    }

    public class Bala extends Bloco {
        public Bala(int x, int y, int largura, int altura, Image img) {
            super(x, y, largura, altura, null);
        }
    }

    public void atiraBala(TipoBala tipo) {
        Bala bala;

        switch (tipo) {
            case NORMAL:
                bala = new Bala(
                        nave.x + naveLargura,
                        nave.y + naveAltura / 2 - balaLargura / 6,
                        balaAltura,
                        balaLargura,
                        null);
                break;

            default:
                bala = new Bala(nave.x + naveLargura, nave.y, balaAltura, balaLargura, null);
        }

        balaArray.add(bala);
    }

    // declaração da nave
    Nave nave;

    // tiros
    ArrayList<Bala> balaArray;
    int balaLargura = tileSize / 2;
    int balaAltura = tileSize / 8;
    int balaVelocidadeX = +10; // velocidade de movimento do tiro

    Timer gameloop;

    SpaceCrusaders() {
        setPreferredSize(new Dimension(larguraQuadro, alturaQuadro));
        setBackground(Color.BLACK);
        setFocusable(true); // foca na tela do jogo
        addKeyListener(this); // pega o evento das teclas

        naveImg = new ImageIcon(getClass().getResource("/imgs/ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("/imgs/alien.png")).getImage();
        alienCyanImg = new ImageIcon(getClass().getResource("/imgs/alien-cyan.png")).getImage();
        alienMagentaImg = new ImageIcon(getClass().getResource("/imgs/alien-magenta.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("/imgs/alien-yellow.png")).getImage();

        alienImagemArray = new ArrayList<Image>();
        alienImagemArray.add(alienCyanImg);
        alienImagemArray.add(alienYellowImg);
        alienImagemArray.add(alienMagentaImg);
        alienImagemArray.add(alienImg);

        nave = new Nave(naveX, naveY, naveLargura, naveAltura, naveImg);
        aliens = new ArrayList<Alien>();
        balaArray = new ArrayList<Bala>();

        // Temporizador do jogo
        gameloop = new Timer(1000 / 60, this);
        criaAliens();
        gameloop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {

        // Desenha a nave
        g.drawImage(nave.img, nave.x, nave.y, nave.largura, nave.altura, null);

        // Desenha os inimigos
        for (int i = 0; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            if (alien.vivo) {
                g.drawImage(alien.img, alien.x, alien.y, alien.largura, alien.altura, null);
            }
        }
        // Desenha as balas
        g.setColor(Color.red);
        for (int i = 0; i < balaArray.size(); i++) {
            Bala bala = balaArray.get(i);
            if (!bala.used) {
                g.fillRect(bala.x, bala.y, balaLargura, balaAltura);

            }
        }

    }

    public void movimento() {
        // aliens
        for (int i = 0; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            if (alien.vivo) {
                // Movimento horizontal em direção à nave
                if (alien.x < nave.x) {
                    alien.x += alienVelocidadeY;
                } else if (alien.x > nave.x) {
                    alien.x -= alienVelocidadeY;
                }
            }

        }

        // bala
        for (int i = 0; i < balaArray.size(); i++) {
            Bala bala = balaArray.get(i);
            bala.x += balaVelocidadeX;
        }

    }

    public void criaAliens() {
        Random random = new Random();

        for (int l = 0; l < alienLinha; l++) {
            int espacamentoHorizontal = 50;
            int espacamentoVertical = 30;
            for (int c = 0; c < alienColunas; c++) {
                int indiceAleatorio = random.nextInt(alienImagemArray.size()); // um índice aleatório de uma imagem de
                                                                               // // // // alien.
                int posY = alienY + l * (alienAltura + espacamentoVertical) + random.nextInt(40);
                Alien alien = new Alien(alienX + c * (alienLargura + espacamentoHorizontal),
                        posY,
                        alienLargura, alienAltura,
                        alienImagemArray.get(indiceAleatorio));
                aliens.add(alien);
            }
        }
        aliensDerrotados = aliens.size();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movimento();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // movimentação da nave

    boolean cima = false;
    boolean baixo = false;
    boolean esquerda = false;
    boolean direta = false;

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            cima = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            baixo = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direta = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            esquerda = true;
        }

        if (cima && nave.y - naveVelocidadeY >= 0) {
            nave.y -= 3; // move para cima.
            if (direta && nave.x + nave.largura + naveVelocidadeY <= larguraQuadro) {
                nave.x += 3;
            }
            if (esquerda && nave.x - naveVelocidadeY >= 0) {
                nave.x -= 3;
            }
        }
        if (baixo && nave.y + nave.altura + naveVelocidadeY <= alturaQuadro) {
            nave.y += 3; // move para baixo.
            if (direta && nave.x + nave.largura + naveVelocidadeY <= larguraQuadro) {
                nave.x += 3;
            }
            if (esquerda && nave.x - naveVelocidadeY >= 0) {
                nave.x -= 3;
            }
        }
        if (direta && nave.x + nave.largura + naveVelocidadeY <= larguraQuadro) {
            nave.x += 3;
        }
        if (esquerda && nave.x - naveVelocidadeY >= 0) {
            nave.x -= 3;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            cima = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            baixo = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direta = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            esquerda = false;
        }

        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            atiraBala(TipoBala.NORMAL);
        }

    }

}
