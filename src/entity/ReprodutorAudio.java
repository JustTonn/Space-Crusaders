package entity;

public interface ReprodutorAudio {
    void tocarEfeito(String caminho);

    void tocarMusica(String caminho, boolean loop);

    void pararMusica();
}
