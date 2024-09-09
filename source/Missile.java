public class Missile extends Sprite {

    private final int BOARD_WIDTH = 800;
    private static int MISSILE_SPEED = 5;
    private static String type;

    public Missile(int x, int y, String type) {
        super(x, y);
        
        initMissile(type);
    }
    
    private void initMissile(String type) {
        loadImage("images/bullets/" + type + "Missile.png");
        switch(type) {
            case "red":
                MISSILE_SPEED = 1;
            case "blue":
                MISSILE_SPEED = 4;
        }
        getImageDimensions();
    }

    public void move() {
        
        x += MISSILE_SPEED;
        
        if (x > BOARD_WIDTH)
            visible = false;
    }
}