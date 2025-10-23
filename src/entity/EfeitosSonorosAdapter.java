package entity;

public class EfeitosSonorosAdapter implements ReprodutorAudio {
    @Override
    public void tocarEfeito(String caminho) {
        EfeitosSonoros.tocarEfeito(caminho);
    }

    @Override
    public void tocarMusica(String caminho, boolean loop) {
        EfeitosSonoros.tocarMusica(caminho, loop);
    }

    @Override
    public void pararMusica() {
        EfeitosSonoros.pararMusica();
    }
}
