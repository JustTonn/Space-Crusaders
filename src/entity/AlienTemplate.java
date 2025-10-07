package entity;

import java.awt.Image;
import java.util.Random;

import entity.SpaceCrusaders.Nave;

public abstract class AlienTemplate extends Bloco {

    protected boolean vivo = true;
    protected int velocidade;
    protected Random random = new Random();

    public AlienTemplate(int x, int y, int largura, int altura, Image img, int velocidade) {
        super(x, y, largura, altura, img);
        this.velocidade = velocidade;
    }

    public final void acaoDoAlien(Nave nave, java.util.List<AlienTemplate> todosAliens,
            java.util.List<SpaceCrusaders.Bala> balas) {
        mover(nave);
        Atirar(balas);
        verificarColisao(nave);
    }

    // Abstração das ações
    protected abstract void mover(Nave nave);

    protected abstract void Atirar(java.util.List<SpaceCrusaders.Bala> balas);

    // Talvez seja implementado
    protected void verificarColisao(Nave nave) {
    }

    public boolean vivo() {
        return vivo;
    }

    public void morrer() {
        vivo = false;
    }
}
