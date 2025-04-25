import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;

public class Invasion extends JFrame {
    private static final int PANELWIDTH = 300, PANELHEIGHT = 300;
    private JButton credits, play, quit;
    
    public Invasion() {
        initUI();
    }
    
    private void initUI() {
        add(new Board());
        
        setResizable(false);
        pack();
        
        setTitle("SpaceDefenders");
        setLocationRelativeTo(null);
        setLocation(0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Invasion ex = new Invasion();
            ex.setVisible(true);
        });
    }
}