import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


public class Board extends JPanel implements ActionListener {
    private MusicBox stateMusic = new MusicBox();
    private Timer timer;
    private SpaceShip spaceship;
    private Boss theBoss;
    private List<Alien> aliens;
    private List<Health> health;
    private boolean ingame, victory, paused, bossRound, nuked, infinite, achievementState;
    private final int ICRAFT_X = 20;
    private final int ICRAFT_Y = 60;
    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 600;
    private final int DELAY = 5;
    private double bossHP = 10;
    private double lives = 50, baseStability = 10, dmg = 1;
    private int rounds = 1;
    private ImageIcon healthBar, barColour;
    private ImageIcon bg, bS, rS, yS, fS, tfS;
    private ImageIcon iconW;
    private ImageIcon border1, borderB;
    private ImageIcon gameOverState, victoryState, nukeState, pauseState;
    private int rBG;
    private Pause pause;
    private int totalRounds;
    private JLabel achievement, name, current;
    private String[][] achievementList = {
        {"first", "The Beginning- Start the game", "guaranteed"}, 
        {"second", "Midway through- Complete 5 rounds", "common"}, 
        {"third", "Near the light- Complete 10 rounds", "common"}, 
        {"fourth", "Keep going?- Complete 15 rounds", "uncommon"}, 
        {"fifth", "Tiny bit sus- Complete 50 rounds", "uncommon"}, 
        {"sixth", "A tough battle- Complete 100 rounds", "rare"}, 
        {"seventh", "Never Gonna Give You Up!!- Complete 500 rounds", "rare"}, 
        {"eighth", "The Darkness Sees- Complete 1000 rounds", "epic"}, 
        {"ninth", "The True Final Battle- Reach [Max Integer] rounds", "epic"}, 
        {"tenth", "Syntax Overload- Fully finish the game", "impossible"}
    };
    
    private int curAchievement = 0;
    private static final int PANELWIDTH = 800, PANELHEIGHT=300;
    private JFrame frame;
    

    private static int[][] pos = {
        {1500, 55}, {1100, 59}, {1380, 89},
        {780, 109}, {580, 139}, {680, 239},
        {790, 259}, {760, 50}, {790, 150},
        {980, 209}, {560, 45}, {510, 70},
        {930, 159}, {590, 80}, {530, 60},
        {940, 59}, {990, 34}, {920, 200},
        {900, 259}, {660, 50}, {540, 90},
        {810, 220}, {860, 47}, {740, 180},
        {820, 128}, {490, 170}, {700, 47},
        {300, 58}, {576, 48}, {564, 267}, {593, 289},
        {2134, 59}, {1500, 510}, {1100, 349}, {1380, 492},
        {780, 443}, {580, 523}, {680, 239},
        {790, 520}, {760, 438}, {790, 356},
        {980, 313}, {560, 458}, {510, 435},
        {930, 321}, {590, 506}, {530, 314},
        {940, 453}, {990, 432}, {920, 521},
        {900, 341}, {660, 356}, {540, 342},
        {810, 368}, {860, 405}, {740, 419},
        {820, 231}, {490, 510}, {700, 312}
    };
    
    private static int[] bossPos = {
        60, 70, 80, 90, 100, 
        110, 120, 130, 140, 150, 160, 170, 180, 190, 
        200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 
        300, 310, 320, 330, 340, 350, 360, 370, 380, 390, 
        400, 410, 420, 430, 440, 450, 460, 470, 480, 490, 
        500, 510, 520, 530, 541
    };
    
    private static int[][] posHealth = {
        {800, 218}, {2000, 300}
    };
    
    public Board() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        ingame = true;
        victory = false;
        paused = true;
        achievementState = false;
        bossRound = true;
        nuked = false;
        infinite = false;
        
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        
        pause = new Pause();

        spaceship = new SpaceShip(ICRAFT_X, ICRAFT_Y);
        
        setLayout(new FlowLayout());
        JButton randomButton = new JButton("Nuke all");
        randomButton.addActionListener(new MegashotListener());
        add(randomButton);
        
        initHealth();
        
        bS = new ImageIcon("images/bosses/background/blueSun.png");
        rS = new ImageIcon("images/bosses/background/redSun.png");
        yS = new ImageIcon("images/bosses/background/yellowSun.png");
        fS = new ImageIcon("images/bosses/background/levelFinal.png");
        tfS = new ImageIcon("images/bosses/background/finale.png");
        
        rBG = (int) (Math.random() * 4 + 1);
        bg = new ImageIcon("images/assets/level" + rBG + ".png");
        
        border1 = new ImageIcon("images/assets/borderOne.png");
        borderB = new ImageIcon("images/assets/borderBoss.png");
        
        if(!bossRound)
            initAliens();
        
        if(bossRound) {
            aliens = new ArrayList<>();
            
            theBoss = new Boss(500, 100);
        }

        System.out.println("[W]/[A] to move Up/Down");
        System.out.println("[Space] to shoot");
        System.out.println("[Shift] to do a barrier shot(Unavailable during bosses!!)");
        System.out.println("[I] to toggle between normal and infinite mode");
        System.out.println("Press [P] or [Esc] to pause and view controls!");
        System.out.println("Press [A] to view your achievements! [WARNING! THIS WILL END YOUR GAME!!!]");
        System.out.println("Goodluck commander 🫡!");
        System.out.println("Please keep this window open!! :)");
        
        playOST("firstStretch", "Defend your city!", true);

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void initAliens() {
        
        aliens = new ArrayList<>();
        
        if(bossRound) {
            for(int y : bossPos) {
                aliens.add(new Alien(700, y));
            }
        }
        if(!bossRound) {
            for (int[] p : pos) {
                aliens.add(new Alien(p[0], p[1]));
            }
        }
        
    }
    
    public void initHealth() {
        health = new ArrayList<>();
        
        for(int[] p : posHealth) {
            health.add(new Health(p[0], p[1]));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(baseStability <= 0) {
            lives -= 0.01;
        }
        
        if(baseStability > 10) {
            baseStability = 10;
        }
        
        if(lives <= 0) {
            baseStability -= 0.005;
        }
        
        if(rounds > 10 && !infinite) {
            victory = true;
        }
        
        if(infinite && rounds == Integer.MIN_VALUE) {
            victory = true;
        }
        
        g.drawImage(bg.getImage(), 0, 0, B_WIDTH, B_HEIGHT, null);
        
        if(bossRound) {
            if(rounds == 1) {
                g.drawImage(yS.getImage(), 0, 0, B_WIDTH, B_HEIGHT, null);
            }
            
            if(rounds == Integer.MAX_VALUE) {
                if(bossHP > 50) {
                    theBoss = new Boss(400, 100);
                    theBoss.setFinalBoss(true);
                }

                if(bossHP <= 50) {
                    theBoss = new Boss(300, -200);
                    theBoss.setFinalBoss(false);
                }

                if(bossHP < 100)
                    bossHP += 0.02;
                if(baseStability < 10)
                    baseStability += 0.01;
            }
            
            else if(rounds % 1000 == 0) {
                theBoss.set1000Boss();
            }
            
            else if(rounds % 500 == 0) {
                theBoss.set500Boss();
            }
            
            else if(rounds % 100 == 0 ) {
                theBoss.set100Boss();
                if(bossHP < 100)
                    bossHP += 0.01;
            }
            
            else if(rounds % 50 == 0) {
                theBoss.set50Boss();
            }
            
            else if(rounds % 10 == 0 ) {
                g.drawImage(bS.getImage(), 0, 0, B_WIDTH, B_HEIGHT, null);
            }
            
            else if(rounds % 5 == 0) {
                g.drawImage(rS.getImage(), 0, 0, B_WIDTH, B_HEIGHT, null);
            }
        }
        
        if(bossRound && (rounds % 100 == 0 || rounds == Integer.MAX_VALUE)) {
            g.drawImage(tfS.getImage(), 0, 0, B_WIDTH, B_HEIGHT, null);
        }
        else if(rounds % 50 == 0) {
            g.drawImage(fS.getImage(), 0, 0, B_WIDTH, B_HEIGHT, null);
        }
        
        g.setColor(Color.red);
        
        if(baseStability > 2)
            g.setColor(new Color(3, 252, 248));
        
        g.fillRect(0, 0, ICRAFT_X + 10, B_HEIGHT);
        
        g.setColor(new Color(42, 39, 53));
        g.fillRect(0, 0, ICRAFT_X, B_HEIGHT);

        if (ingame) {

            drawObjects(g);

        }
        
        g.setColor(Color.black);
        
        if(!ingame) {

            drawGameOver(g);
        }
        if(victory) {
            drawVictory(g);
        }
        
        if(paused) {
            pause.drawScreen(g);
            
            timer.stop();
        }
        
        if(nuked) {
            drawNuke(g);
        }
        
        if(spaceship.doingMegashot()) {
            spaceship.fireRed();
        }
    }

    private void drawObjects(Graphics g) {
        if (spaceship.isVisible()) {
            g.drawImage(spaceship.getImage(), spaceship.getX(), spaceship.getY(),
                    this);
        }
        
        if(bossRound) {
            g.drawImage(theBoss.getImage(), theBoss.getX(), theBoss.getY(), this);
        }

        List<Missile> ms = spaceship.getMissiles();

        for (Missile missile : ms) {
            if (missile.isVisible()) {
                g.drawImage(missile.getImage(), missile.getX(), 
                        missile.getY(), this);
            }
        }

        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }
        }
        
        for(Health h : health) {
            if(h.isVisible()) {
                g.drawImage(h.getImage(), h.getX(), h.getY(), this);
            }
        }
        
        drawUI(g);
    }
    
    private void drawUI(Graphics g) {
        g.drawImage(border1.getImage(), 0, 0, 640, 41, null);
        
        g.drawImage(border1.getImage(), 0, 559, 500, 41, null);
        
        g.setColor(Color.WHITE);
        if(rounds == Integer.MAX_VALUE)
            g.drawString("Aliens left: ∞", 55, 15);
        else
            g.drawString("Aliens left: " + aliens.size(), 55, 15);
            
        
        g.setColor(Color.GREEN);
        if(rounds == Integer.MAX_VALUE) {
            g.drawString("∞ Lives left", 55, 28);
        }
        else {
            if(lives != 1)
                g.drawString((int)lives + " Lives Left", 55, 28);
                
            if(lives <= 1) {
                g.setColor(Color.RED);
                g.drawString((int)lives + " Life Left", 55, 28);
            }
        }
        
        
        if(lives <= 0 && baseStability <= 0)
            ingame = false;
        
        g.setColor(Color.yellow);
        if(!infinite) {
            g.drawString(rounds + "/10 Rounds completed", 35, B_HEIGHT - 20);
        }
        
        if(infinite && rounds == Integer.MAX_VALUE) {
            g.drawString("∞/∞ Rounds completed", 35, B_HEIGHT - 20);
        }
        
        else if(infinite) {
            g.drawString(rounds + "/∞ Rounds completed", 35, B_HEIGHT - 20);
        }
        
        g.setColor(Color.red);
        
        if(spaceship.isAvailable()) {
            g.drawString(spaceship.getRedAmount() + " barrier shots left", 200, B_HEIGHT - 20);
        }
        
        if(!spaceship.isAvailable() && rounds != Integer.MAX_VALUE) {
            g.drawString("[Barrier shots is unavailable]", 200, B_HEIGHT - 20);
        }
        
        drawHealthBars(g);
    }
    
    private void drawHealthBars(Graphics g) {
        int healthTotal;
        if(rounds == 1) {
            healthTotal = 30;
        }
        else if(rounds == 5) {
            healthTotal = 15;
        }
        else if(rounds == 10) {
            healthTotal = 6;
        }
        else {
            healthTotal = 3;
        }
        
        
        if(bossRound) {
            g.drawImage(borderB.getImage(), 380, 45, 420, 45, null);
            
            g.setFont(new Font("Serif", Font.BOLD, 20));
            if(rounds == Integer.MAX_VALUE) {
                g.setColor(new Color(252, 32, 3));
                g.drawString("[Black Parasite], the Final ∞", 400, 69);
            }
            else if(rounds % 1000 == 0) {
                g.setColor(new Color(10, 255, 149));
                g.drawString("[The Lurker], lurks in the shadows", 400, 69);
            }
                
            else if(rounds % 500 == 0) {
                g.setColor(new Color(255, 143, 15));
                g.drawString("[Rick Astely], rickrolling in", 400, 69);
            }
                
            else if(rounds % 100 == 0) {
                g.setColor(new Color(56, 39, 84));
                g.drawString("[Black Parasite], the Parasitic Monstrosity", 400, 69);
            }
                
            else if(rounds % 50 == 0) {
                g.setColor(new Color(255, 182, 25));
                g.drawString("[Black Imposter], the First Form", 400, 69);
            }
            
            else if(theBoss.getName().equals("henry")) {
                g.drawString("[Henry], the α Σ male", 520, 69);
            }
                
            else if(theBoss.getName().equals("nora")) {
                g.drawString("[Nora], the α Σ female", 515, 69);
            }
                
            else if(theBoss.getName().equals("sunny")) {
                g.drawString("[Sunny], the Roblox pro", 520, 69);
            }
                
            else if(theBoss.getName().equals("sriyan")) {
                g.drawString("[Sriyan], the Ohio Rizzler", 500, 69);
            }
            
            barColour = new ImageIcon("images/healthBar/greyBar.png");
            g.drawImage(barColour.getImage(), 500, 69, 300, 31, this);
            
            barColour = new ImageIcon("images/healthBar/redBar.png");
            g.drawImage(barColour.getImage(), 500, 69, (int)bossHP * healthTotal, 31, null);
            
            healthBar = new ImageIcon("images/healthBar/healthBar.png");
            g.drawImage(healthBar.getImage(), 500, 69, 300, 31, this);
            
            if(rounds % 100 == 0 && (theBoss.getName().equals("blackParasite") || theBoss.getName().equals("blackParasiteAngry"))) {
                if(bossHP >= 50) {
                    iconW = new ImageIcon("images/bosses/icons/neutral.png");
                    g.drawImage(iconW.getImage(), 620, 55, 64, 84, this);
                }
                if(bossHP < 50) {
                    iconW = new ImageIcon("images/bosses/icons/lose.png");
                    g.drawImage(iconW.getImage(), 620, 50, 64, 98, this);
                }
            }
            if(rounds == Integer.MAX_VALUE) {
                if(bossHP >= 50) {
                    iconW = new ImageIcon("images/bosses/icons/neutral.gif");
                    g.drawImage(iconW.getImage(), 620, 55, 64, 84, this);
                }
                if(bossHP < 50) {
                    iconW = new ImageIcon("images/bosses/icons/lose.gif");
                    g.drawImage(iconW.getImage(), 620, 50, 64, 98, this);
                }
            }
        }
        
        g.setColor(Color.red);
        
        if(baseStability > 2) {
            g.setColor(Color.green);
        }
        
        g.setFont(new Font("Serif", Font.BOLD, 20));
        if(rounds == Integer.MAX_VALUE) {
            g.drawString("∞%", 455, 25);
        }
        else {
            g.drawString(((int)(baseStability * 10) + "%"), 455, 25);
        }
        
        
        barColour = new ImageIcon("images/healthBar/greyBar.png");
        g.drawImage(barColour.getImage(), 150, 0, 300, 31, this);
        
        if(rounds != Integer.MAX_VALUE) {
            if(baseStability > 2)
                barColour = new ImageIcon("images/healthBar/greenBar.png");
            else
                barColour = new ImageIcon("images/healthBar/redBar.png");
        }
        
        if(baseStability > 0 && rounds != Integer.MAX_VALUE)
            g.drawImage(barColour.getImage(), 150, 0, (int)(baseStability * 10) * 3, 31, null);
        
        healthBar = new ImageIcon("images/healthBar/healthBar.png");
        g.drawImage(healthBar.getImage(), 150, 0, 300, 31, this);
    }
    
    private void drawGameOver(Graphics g) {
        gameOverState = new ImageIcon("images/states/gameOver.png");
        g.drawImage(gameOverState.getImage(), 0, 0, 800, 600, this);
        
        g.setFont(new Font("Serif", Font.BOLD, 30));
        g.drawString(rounds + " Rounds completed...", 200, 550);

        if(stateMusic.isPlaying())
            stateMusic.stopMusic();
    }
    
    private void drawVictory(Graphics g) {
        victoryState = new ImageIcon("images/states/victory.png");
        g.drawImage(victoryState.getImage(), 0, 0, 800, 600, this);
        
        g.setFont(new Font("Serif", Font.BOLD, 30));
        if(rounds == Integer.MIN_VALUE) {
            g.drawString("∞ Rounds completed", 200, 500);
        }
        else
            g.drawString(rounds + " Rounds completed!", 200, 500);
        
        if(stateMusic.isPlaying())
            stateMusic.stopMusic();
    }
    
    private void drawNuke(Graphics g) {
        nukeState = new ImageIcon("images/states/nuked.png");
        g.drawImage(nukeState.getImage(), 0, 0, 800, 600, this);
        
        g.setFont(new Font("Serif", Font.BOLD, 30));
        g.drawString(rounds + " Rounds completed...", 200, 500);

        if(stateMusic.isPlaying())
            stateMusic.stopMusic();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(bossRound) {
            spaceship.setMissileSpread(800);
            spaceship.setAvailability(false);
        }
        
        if(!bossRound) {
            spaceship.setMissileSpread(1);
            spaceship.setAvailability(true);
        }
        
        inGame();

        updateShip();
        updateMissiles();
        
        updateAliens();
        
        updateHealthCrate();

        checkCollisions();

        repaint();
    }
    
    private void inGame() {
        if (!ingame || victory || paused || nuked) {
            if(!ingame) {
                playOST("gameOver", "You were invaded...", false);
            }
            if(victory) {
                playOST("victory", "You won!", false);
            }
            if(nuked) {
                playOST("nuked", "You nuked everyone...", false);
            }

            timer.stop();
        }
    }

    private void updateShip() {

        if (spaceship.isVisible()) {
            spaceship.move();
        }
    }

    private void updateMissiles() {
        List<Missile> ms = spaceship.getMissiles();

        for (int i = 0; i < ms.size(); i++) {

            Missile m = ms.get(i);

            if (m.isVisible()) {
                m.move();
            } else {
                ms.remove(i);
            }
        }
    }
    
    private void updateAliens() {

        if (aliens.isEmpty() && !bossRound) {
            updateRound();
            return;
        }
        if(aliens.isEmpty() && bossRound) {
            initAliens();
        }

        for (int i = 0; i < aliens.size(); i++) {

            Alien a = aliens.get(i);
            
            if (a.isVisible()) {
                a.move();
            } else {
                aliens.remove(i);
            }
        }
    }
    
    private void updateHealthCrate() {
        for (int i = 0; i < health.size(); i++) {
            Health h = health.get(i);
            
            if (h.isVisible()) {
                h.move();
            } else {
                health.remove(i);
            }
        }
    }
    
    public void checkCollisions() {

        Rectangle r3 = spaceship.getBounds();
        
        Rectangle r7 = theBoss.getBounds();

        for (Alien alien : aliens) {
            
            Rectangle r2 = alien.getBounds();

            if (r3.intersects(r2)) {
                alien.setVisible(false);
                lives -= dmg;
            }
        }
        
        for(Alien alien : aliens) {
            if(alien.getX() == 20) {
                alien.setVisible(false);
                baseStability -= 0.5;
            }
        }

        List<Missile> ms = spaceship.getMissiles();
        
        for(Health h : health) {
            if(h.getX() == 20) {
                if(baseStability < 10)
                    baseStability++;
            }
        }
        
        for (Missile m : ms) {
            Rectangle r1 = m.getBounds();
            
            int randomise = (int)(Math.random() * 20);

            for (Alien alien : aliens) {

                Rectangle r2 = alien.getBounds();

                if (r1.intersects(r2)) {
                    m.setVisible(false);
                        
                    if(randomise != 5)
                        alien.setVisible(false);
                }
            }
            
            if(r1.intersects(r7) && theBoss.isVisible()) {
                m.setVisible(false);
                bossHP--;
                if(bossHP <= 0) {
                    updateRound();
                }
            }
            
            for(Health h : health) {
                Rectangle r4 = h.getBounds();
                
                if(r1.intersects(r4)) {
                    h.setVisible(false);
                }
            }
        }
    }

    public void playOST(String location, String msg, boolean loops) {
        if (stateMusic.isPlaying()) {
            stateMusic.stopMusic();
        }
        stateMusic = new MusicBox();
        stateMusic.playMusic(location, msg, loops);
    }
    
    public void updateRound() {
        initHealth();
        rounds++;
        
        if(rounds == 1 || rounds % 5 == 0 || rounds % 10 == 0 || rounds % 50 == 0 || rounds % 100 == 0 || rounds == Integer.MAX_VALUE) {
            bossHP = 4 * rounds + 10;
            if(rounds >= 50)
                bossHP = 100;
                
            theBoss = new Boss(500, 100);
            
            baseStability = 10;
            theBoss.initBoss();
            
            spaceship.resetMissile();
            
            bossRound = true;
            theBoss.setVisible(true);
            
            spaceship.setAvailability(false);
            
            if(rounds == Integer.MAX_VALUE) {
                paused = true;
                
                playOST("finale", "You're a persistent little shrimp, aren't you?", true);
                JOptionPane.showMessageDialog(null, "Well then...");
                JOptionPane.showMessageDialog(null, "LET'S HAVE A LITTLE REMATCH, SHALL WE?");
            }
        } else {
            bossRound = false;
            theBoss.setVisible(false);
        }
        
        initAliens();
        
        if(baseStability < 5)
            baseStability = 7;
        
        if(rounds > 10)
            spaceship.setRedAmount(Math.abs(rounds/10 * 14));
            
        if((int)(40/rounds) > 0) {
            lives = (int)(40 / rounds);
        }
        else {
            lives = 1;
        }

        if(rounds == Integer.MAX_VALUE) {
            lives = 500;
        }
        
        dmg *= 1.5;
    }
    
    public void hideAchievement(int achievementNum) {
        achievementList[achievementNum][0] = "mystery";
        achievementList[achievementNum][1] = "Unlock to view!";
    } 
    
    public void analyse() {
        if(rounds != Integer.MIN_VALUE) {
            hideAchievement(9);
        }
        if(rounds != Integer.MIN_VALUE) {
            if(rounds != Integer.MAX_VALUE) {
                hideAchievement(8);
            }
            if(rounds <= 1000) {
                hideAchievement(7);
            }
            if(rounds <= 500) {
                hideAchievement(6);
            }
            if(rounds <= 100) {
                hideAchievement(5);
            }
            if(rounds <= 50) {
                hideAchievement(4);
            }
            if(rounds <= 15) {
                hideAchievement(3);
            }
            if(rounds <= 10) {
                hideAchievement(2);
            }
            if(rounds <= 5) {
                hideAchievement(1);
            }
            if(rounds < 1) {
                hideAchievement(0);
            }
        }
    }
    
    private class leftListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(curAchievement > 0) {
                curAchievement--;
            }
            else {
                curAchievement = achievementList.length - 1;
            }
            
            achievement.setIcon(new ImageIcon("images/assets/achievements/" + achievementList[curAchievement][0] + ".png"));
            if(achievementList[curAchievement][2].equals("guaranteed")) {
                name.setForeground(Color.black);
            }
            else if(achievementList[curAchievement][2].equals("common")) {
                name.setForeground(Color.green);
            }
            else if(achievementList[curAchievement][2].equals("uncommon")) {
                name.setForeground(new Color(255, 161, 20));
            }
            else if(achievementList[curAchievement][2].equals("rare")) {
                name.setForeground(Color.red);
            }
            else if(achievementList[curAchievement][2].equals("epic")) {
                name.setForeground(Color.blue);
            }
            else if(achievementList[curAchievement][2].equals("impossible")) {
                name.setForeground(new Color(144, 0, 255));
            }
            
            name.setText(achievementList[curAchievement][1]);
            current.setText((curAchievement + 1) + "/" + achievementList.length);
        }
    }
    
    private class rightListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(curAchievement < (achievementList.length) - 1) {
                curAchievement++;
            }
            else {
                curAchievement = 0;
            }
            
            achievement.setIcon(new ImageIcon("images/assets/achievements/" + achievementList[curAchievement][0] + ".png"));
            
            if(achievementList[curAchievement][2].equals("guaranteed")) {
                name.setForeground(Color.black);
            }
            else if(achievementList[curAchievement][2].equals("common")) {
                name.setForeground(Color.green);
            }
            else if(achievementList[curAchievement][2].equals("uncommon")) {
                name.setForeground(new Color(255, 161, 20));
            }
            else if(achievementList[curAchievement][2].equals("rare")) {
                name.setForeground(Color.red);
            }
            else if(achievementList[curAchievement][2].equals("epic")) {
                name.setForeground(Color.blue);
            }
            else if(achievementList[curAchievement][2].equals("impossible")) {
                name.setForeground(new Color(144, 0, 255));
            }
            name.setText(achievementList[curAchievement][1]);
            current.setText((curAchievement + 1) + "/" + achievementList.length);
        }
    }
    
    public void achievementEnter() {
        frame = new JFrame("achievementList");
        frame.setSize(PANELWIDTH+frame.getX(), PANELHEIGHT+frame.getY());
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false);
        
        if(frame.isVisible()) {
            frame.setVisible(false);
        }
        else {
            frame.setVisible(true);
        }
        
        analyse();

        achievement = new JLabel();
        achievement.setIcon(new ImageIcon("images/assets/achievements/" + achievementList[curAchievement][0] + ".png"));
        
        name = new JLabel(achievementList[curAchievement][1]);
        name.setFont(new Font("Serif", Font.BOLD, 20));
        name.setForeground(Color.black);
        
        current = new JLabel("1/" + achievementList.length);
        current.setFont(new Font("Serif", Font.BOLD, 20));
        current.setForeground(Color.black);
        
        JButton left = new JButton("Previous");
        left.addActionListener(new leftListener());
        
        JButton right = new JButton("Next");
        right.addActionListener(new rightListener());
        
        timer.stop();
        
        frame.setLayout(new FlowLayout());
        
        frame.add(left);
        frame.add(achievement);
        frame.add(name);
        frame.add(current);
        frame.add(right);
        
        
    }
    
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            spaceship.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            spaceship.keyPressed(e);
            
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
                if(!paused) {
                    paused = true;
                    
                }
                else if(paused) {
                    paused = false;
                    timer.start();
                }
            }
            
            if(key == KeyEvent.VK_I) {
                if(infinite == false) {
                    infinite = true;
                }
                else if(infinite) {
                    infinite = false;
                }
            }
            
            // Dev Tools
            
            if(key == KeyEvent.VK_ENTER) {
                rounds = Integer.MIN_VALUE;
            }
            
            if(key == KeyEvent.VK_5) {
                rounds = 4;
                updateRound();
            }
            
            if(key == KeyEvent.VK_6 && rounds % 5 == 0) {
                rounds += 4;
                updateRound();
            }
            if(key == KeyEvent.VK_7 && rounds % 10 == 0) {
                rounds += 9;
                updateRound();
            }
            if(key == KeyEvent.VK_8 && rounds % 100 == 0) {
                rounds += 99;
                updateRound();
            }
            if(key == KeyEvent.VK_9 && rounds % 1000 == 0) {
                rounds += 999;
                updateRound();
            }
            if(key == KeyEvent.VK_0 && rounds > 1000) {
                rounds = Integer.MAX_VALUE - 2;
                updateRound();
            }
            
            if(key == KeyEvent.VK_A) {
                achievementEnter();
            }
        }
    }
    
    private class MegashotListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            nuked = true;
        }
    }
}