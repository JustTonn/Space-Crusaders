package entity;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class SpaceCrusaders extends JPanel implements  ActionListener, KeyListener{

    int tileSize = 64; //tamanho de cada quadradinho da tela.
    int colunas = 20; //quantos quadradinhos tem na vertical.
    int linhas = 12; //quantos quadradinhos tem na horizontal
    int larguraQuadro = tileSize * colunas;
    int alturaQuadro = tileSize * linhas;

    Image naveImg; //sprite da nave.
    Image alienImg; //sprite dos inimigos.
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

    //Inimigos

    ArrayList<Bloco> aliens;
    int alienAltura = tileSize;
    int alienLargura = tileSize;
    int alienX = 1280 - tileSize;
    int alienY = tileSize;

    int alienLinha = 3;
    int alienColunas = 1;
    int aliensDerrotados = 0;
    int pontosAlien = 1;

    public class Nave extends Bloco {
        public Nave(int x, int y, int largura, int altura, Image img) {
            super(x, y, largura, altura, img);
        }

    }

    Nave nave;

    Timer gameloop;

    SpaceCrusaders() {
        setPreferredSize(new Dimension(larguraQuadro, alturaQuadro));
        setBackground(Color.BLACK);
        setFocusable(true); //foca na tela do jogo
        addKeyListener(this); //pega o evento das teclas

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
        aliens = new ArrayList<Bloco>();

        //Temporizador do jogo
        gameloop = new Timer(1000/60,this);
        criaAliens();
        gameloop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {

        //nave
        g.drawImage(nave.img, nave.x, nave.y, nave.largura, nave.altura, null);

        // inimigos
        for(int i = 0; i<aliens.size();i++){
            Bloco alien = aliens.get(i);
            if(alien.vivo){
                g.drawImage(alien.img,alien.x,alien.y,alien.largura,alien.altura,null);
            }
        }
    }

    public void criaAliens(){
        Random random = new Random();

        for(int l = 0; l< alienLinha; l++){
            for (int c = 0;c<alienColunas;c++){
                int indiceAleatorio = random.nextInt(alienImagemArray.size()); //um índice aleatório de uma imagem de alien.
                Bloco alien = new Bloco(alienX + c*alienLargura,alienY + l*alienAltura,alienLargura,alienAltura,alienImagemArray.get(indiceAleatorio));
                aliens.add(alien);
            }
        }
        aliensDerrotados = aliens.size();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && nave.y - naveVelocidadeY >= 0){
            nave.y -= 3; // move para cima.
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && nave.y + naveVelocidadeY <= alturaQuadro) {
            nave.y += 3; //move para baixo.
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
