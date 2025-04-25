import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;

public class Boss extends Sprite {

    private final int INITIAL_X = 400;
    private List<bossMissile> missiles;
    private String name = "";
    private String[] bosses = {"henry", "nora", "sunny", "sriyan", "blackImposter", "blackParasite", "rickAstely", "theLurker"};

    public Boss(int x, int y) {
        super(x, y);
        initBoss();
    }
    
    public Boss(int x, int y, String name) {
        super(x, y);
        this.name = name;
        initBoss();
    }

    public void initBoss() {
        if(name.equals("")) {
            int curBoss = (int)(Math.random() * 4);
            name = bosses[curBoss];
        }
        loadImage("images/bosses/" + name + ".png");
        getImageDimensions();
    }
    
    public void fire() {
        missiles.add(new bossMissile(500, (int)(Math.random() * 600)));
    }
    
    public List<bossMissile> getMissiles() {
        return missiles;
    }
    
    public void set50Boss() {
        name = bosses[4];
        loadImage("images/bosses/blackImposter.png");
        getImageDimensions();
    }
    
    public void set100Boss() {
        name = bosses[5];
        loadImage("images/bosses/blackParasite.png");
        getImageDimensions();
    }
    
    public void set500Boss() {
        name = bosses[6];
        loadImage("images/bosses/rickAstely.png");
        getImageDimensions();
    }
    
    public void set1000Boss() {
        name = bosses[7];
        loadImage("images/bosses/theLurker.png");
        getImageDimensions();
    }

    public void setFinalBoss(boolean winning) {
        name = bosses[5];
        if(winning) {
            loadImage("images/bosses/finalblackParasite.gif");
        }
        if(!winning) {
            loadImage("images/bosses/finalangryBlack.gif");
        }
        getImageDimensions();
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public String getName() {
        return name;
    }
}