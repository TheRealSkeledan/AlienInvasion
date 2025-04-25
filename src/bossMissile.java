public class bossMissile extends Sprite {

    private final int BOARD_WIDTH = 800;
    private static int MISSILE_SPEED = 2;

    public bossMissile(int x, int y) {
        super(x, y);
        
        initMissile();
    }
    
    private void initMissile() {
        loadImage("bullets/bossMissile.png");
        getImageDimensions();
    }

    public void move() {
        
        x -= MISSILE_SPEED;
        
        if (x < 0)
            visible = false;
    }
}