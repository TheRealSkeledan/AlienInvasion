import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SpaceShip extends Sprite {
    private MusicBox soundFX;
    private int dx;
    private int dy;
    private int speed = 4;
    private int redAmount = 7;
    private List<Missile> missiles;
    boolean megaShot = false, availibility = true;
    private int missileSpread = 1;

    public SpaceShip(int x, int y) {
        super(x, y);
        
        initCraft();
    }

    private void initCraft() {
        missiles = new ArrayList<>();
        loadImage("images/assets/craft.png");
        getImageDimensions();
        
    }
    
    public void move() {

        x += dx;
        y += dy;

        if (x < 1) {
            x = 1;
        }

        if (y < 1) {
            y = 1;
        }
    }

    public List<Missile> getMissiles() {
        return missiles;
    }
    
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_SPACE) {
            fire();
        }
        
        if(key == KeyEvent.VK_SHIFT && redAmount > 0) {
            fireRed();
        }

        if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && y - 1 > 41) {
            dy = -(speed);
        }

        if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && y + 1 < 559) {
            dy = speed;
        }
    }
    
    public void fire() {
        missiles.add(new Missile(x + width, y + height / 2, "blue"));
    }
    
    public void fireRed() {
        if(isAvailable()) {
            for(int y = 35; y <= 800; y += missileSpread)
                missiles.add(new Missile(20, y, "red"));
            redAmount--;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            dy = 0;
        }
        
        if(y + 32 > 559) {
            y = 527;
        }
        
        if(y < 40) {
            y = 41;
        }
    }
    
    public int getRedAmount() {
        return redAmount;
    }
    
    public void setRedAmount(int rA) {
        redAmount = rA;
    }
    
    public void resetMissile() {
        missiles.clear();
    }
    
    public int getY() {
        return y;
    }
    
    public boolean doingMegashot() {
        return megaShot;
    }
    
    public void setMegashot(boolean ms) {
        megaShot = ms;
    }
    
    public void setMissileSpread(int mss) {
        missileSpread = mss;
    }
    
    public boolean isAvailable() {
        return availibility;
    }
    
    public void setAvailability(boolean a) {
        availibility = a;
    }
}