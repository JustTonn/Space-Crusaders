package entity;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;

public class SpaceCrusaders extends JPanel {
    int tileSize = 64;
    int colunas = 20;
    int linhas = 20;
    int larguraQuadro = tileSize * colunas;
    int alturaQuadro = tileSize * linhas;

    SpaceCrusaders() {
        setPreferredSize(new Dimension(larguraQuadro, alturaQuadro));
        setBackground(Color.BLACK);
    }

}
