package entity;

import javax.swing.JFrame;

public class Janela {
    int tileSize = 64;
    int colunas = 20;
    int linhas = 12;
    int larguraQuadro = tileSize * colunas;
    int alturaQuadro = tileSize * linhas;

    public Janela() {
        JFrame frame = new JFrame("Space Crusaders");
        frame.setSize(larguraQuadro, alturaQuadro);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpaceCrusaders spaceCrusaders = new SpaceCrusaders();
        frame.add(spaceCrusaders);
        frame.pack();
        frame.setVisible(true);

    }
}
