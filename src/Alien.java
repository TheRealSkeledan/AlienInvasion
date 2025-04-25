public class Alien extends Sprite {

    private final int INITIAL_X = 400;
    private final String[] enemies = {"Banana", "Black", "Blue", "Brown", "Coral", "Cyan", "Gray", "Green", "Lime", "Maroon", "Orange", "Pink", "Purple", "Red", "Rose", "Tan", "White", "Yellow"};
    private int speed;

    public Alien(int x, int y) {
        super(x, y);
        speed = (int)(Math.random() * 5 + 1);

        initAlien();
    }

    private void initAlien() {
        loadImage("images/enemies/" + enemies[(int)(Math.random() * enemies.length)] + ".png");
        getImageDimensions();
    }

    public void move() {

        if (x < 0) {
            x = INITIAL_X;
        }

        x -= 2;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}