package entity;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public class EfeitosSonoros {

    private static Clip musicaAtual; // armazena a musica que esta tocando aqui

    public static void tocarEfeito(String caminho) {
        try {
            URL url = EfeitosSonoros.class.getResource(caminho);
            if (url == null) {
                System.err.println("Arquivo de som não encontrado: " + caminho);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void tocarMusica(String caminho, boolean loop) {
        pararMusica();
        new Thread(() -> {
            try {
                URL url = EfeitosSonoros.class.getResource(caminho);
                if (url == null) {
                    System.err.println("Arquivo de música não encontrado: " + caminho);
                    return;
                }
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                musicaAtual = AudioSystem.getClip();
                musicaAtual.open(audioStream);
                if (loop)
                    musicaAtual.loop(Clip.LOOP_CONTINUOUSLY);
                musicaAtual.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void pararMusica() {
        if (musicaAtual != null && musicaAtual.isRunning()) {
            musicaAtual.stop();
            musicaAtual.close();
            musicaAtual = null;
        }
    }
}
