package entity;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class EfeitosSonoros {
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
        new Thread(() -> {
            try {
                URL url = EfeitosSonoros.class.getResource(caminho);
                if (url == null) {
                    System.err.println("Arquivo de música não encontrado: " + caminho);
                    return;
                }
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                if (loop)
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
