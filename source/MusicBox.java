import java.io.File;
import javax.swing.JOptionPane;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicBox {
    private AudioInputStream audioInput;
    private Clip clip;
    private boolean playing = false;

    public void playMusic(String musicLocation, String message, boolean loops) {
        try {
            File musicPath = new File("music/" + musicLocation + ".wav");

            if(musicPath.exists()) {
                audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                if(loops) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                playing = true;

                JOptionPane.showMessageDialog(null, message);
            }
            else {
                System.out.println("Can't find file!");
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopMusic() {
        clip.stop();
        playing = false;
    }

    public boolean isPlaying() {
        return playing;
    }
}
