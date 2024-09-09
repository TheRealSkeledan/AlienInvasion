import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Pause {
    private ImageIcon pause;
    private JLabel exitButton;
    
    public Pause() {
        pause = new ImageIcon("images/states/pauseMenu.png");
    }
    
    public void drawScreen(Graphics g) {
        g.drawImage(pause.getImage(), 35, 25, 750, 550, null);
    }
}