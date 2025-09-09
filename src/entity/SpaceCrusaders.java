package entity;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class SpaceCrusaders extends JPanel {
    int tileSize = 64;
    int colunas = 20;
    int linhas = 20;
    int larguraQuadro = tileSize * colunas;
    int alturaQuadro = tileSize * linhas;

    Image shipImg;
    Image alienImg;
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    ArrayList<Image> alienImagemArray;

    SpaceCrusaders() {
        setPreferredSize(new Dimension(larguraQuadro, alturaQuadro));
        setBackground(Color.BLACK);

        shipImg = new ImageIcon(getClass().getResource("./imgs/ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("./imgs/alien.png")).getImage();
        alienCyanImg = new ImageIcon(getClass().getResource("./imgs/alien-cyan.png")).getImage();
        alienMagentaImg = new ImageIcon(getClass().getResource("./imgs/alien-magenta.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("./imgs/alien-yellow.png")).getImage();

    }

}
