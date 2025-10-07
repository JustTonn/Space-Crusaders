package entity;

import java.awt.Image;
import java.util.List;
import entity.SpaceCrusaders.Nave;

public class AlienNormal extends AlienTemplate {

    public AlienNormal(int x, int y, int largura, int altura, Image img, int velocidade) {
        super(x, y, largura, altura, img, velocidade);
    }

    @Override
    protected void mover(Nave nave) {
        // se move horizontalmente em direção à nave
        if (x > nave.x)
            x -= velocidade;
        else if (x < nave.x)
            x += velocidade;
    }

    @Override
    protected void Atirar(List<SpaceCrusaders.Bala> balas) {
        if (random.nextDouble() < 0.01) { // 1% chance
            SpaceCrusaders.Bala bala = new SpaceCrusaders.Bala(
                    x - 10,
                    y + altura / 2,
                    8,
                    4,
                    null);
            balas.add(bala);
        }
    }
}
