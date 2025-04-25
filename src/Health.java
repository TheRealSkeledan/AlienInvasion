import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Health extends Sprite {
    private final int INITIAL_X = 400;
    private Timer timer;

    public Health(int x, int y) {
        super(x, y);

        initHealth();
        timer = new Timer(15, new healthTimer());
    }

    private void initHealth() {
        loadImage("images/assets/healthCrate.png");
        getImageDimensions();
    }

    public void move() {
        if (x < 0) {
            x = INITIAL_X;
        }

        x -= 1;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    private class healthTimer implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            move();
        }
    }
}