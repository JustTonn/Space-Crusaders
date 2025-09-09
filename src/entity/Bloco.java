package entity;

import java.awt.Image;

public class Bloco {
    int x;
    int y;
    int largura;
    int altura;
    Image img;
    boolean vivo = true; // vamos usar isso apenas nos aliens
    boolean used = false; // usado para balas

    public Bloco(int x, int y, int largura, int altura, Image img) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.img = img;
    }

}
