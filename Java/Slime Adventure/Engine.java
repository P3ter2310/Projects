import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Engine extends JPanel implements Runnable {

    public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final int TILE_SIZE = 64;
    public static final int TILES_LEFT_RIGHT = (SCREEN_WIDTH / TILE_SIZE);
    public static int GAME_SPEED = 3;

    public static boolean debugOn = false;

    private static final int FPS = 60;
    private static final double DRAW_INTERVAL = 1000000000 / FPS;

    public static boolean running;

    public static int gameState = 0;
    public static final int menuState = 0;
    public static final int runningState = 1;
    public static final int gameOver = 2;
    public static int highScore;

    public static ArrayList<Obstacle> obstacles = new ArrayList<>();
    public static ArrayList<SlimeBall> slimeBalls = new ArrayList<>();
    Player player = new Player();
    Map map = new Map();
    KeyHandler kHandler = new KeyHandler();

    Debug debug = new Debug();

    Thread mainThread;
    public Engine() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(kHandler);
        this.setFocusable(true);
    }

    protected void startGameThread() {
        mainThread = new Thread(this);
        mainThread.start();
        running = true;
        highScore = Map.readHighScore();
    }
    public static void restart() {
        Map.mapPosition = 0;
        Map.obstacleCounter = 0;
        obstacles.clear();
        slimeBalls.clear();
        Player.setStartValues();
        highScore = Map.readHighScore();
    }

    @Override
    public void run() {

        double nextDrawTime = System.nanoTime() + DRAW_INTERVAL;

        while(running) {
            if(gameState == runningState) {
                update();
            }
            repaint();

            try {
                double remainingTime = (nextDrawTime - System.nanoTime()) / 1000000;

                if(remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += DRAW_INTERVAL;
            } catch (InterruptedException e) {
                System.out.println("Something interrupted main thread");
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;


        if(gameState == menuState) {
            drawMenu(g2D);
        }else if(gameState == runningState) {
            drawGame(g2D);
        }else if(gameState == gameOver) {
            drawGameOver(g2D);
        }

        g2D.dispose();
    }

    private void update() {
        if(obstacles.size() != 0) {
            if(obstacles.get(0).getX() < -Engine.TILE_SIZE) {
                obstacles.remove(0);
            }
        }
        if(slimeBalls.size() != 0) {
            if(slimeBalls.get(0).getX() > Engine.SCREEN_WIDTH) {
                slimeBalls.remove(0);
            }
        }
        for(Obstacle o: obstacles) {
            if(o.destroyed) {
                obstacles.remove(o);
                break;
            }
        }
        for(SlimeBall s: slimeBalls) {
            if(s.destroyed) {
                slimeBalls.remove(s);
                break;
            }
        }
        map.update();
        for (Obstacle o: obstacles) {
            o.update();
        }
        for (SlimeBall s: slimeBalls) {
            s.update();
        }
        player.update();
    }

    private void drawMenu(Graphics2D g2D) {
        g2D.setFont(new Font(Font.MONOSPACED, Font.BOLD, 80));
        g2D.setColor(Color.WHITE);
        g2D.drawString("Press Enter to start", getCenteredXForString("Press Enter to start", g2D), 300);

        g2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 50));
        g2D.drawString("Arrow up - Jump", getCenteredXForString("Arrow up - Jump", g2D), 500);
        g2D.drawString("Arrow down - Crouch", getCenteredXForString("Arrow down - Crouch", g2D), 600);
        g2D.drawString("Space - Shoot", getCenteredXForString("Space - Shoot", g2D), 700);
        g2D.drawString("ESC - Quit", getCenteredXForString("ESC - Quit", g2D), 800);
    }

    public static int getCenteredXForString(String s, Graphics2D g2D) {
        return SCREEN_WIDTH/2 - ((int)g2D.getFontMetrics().getStringBounds(s,g2D).getWidth()) / 2;
    }

    private void drawGame(Graphics2D g2D) {
        Color color1 = Color.BLUE;
        Color color2 = Color.BLACK;
        GradientPaint gp = new GradientPaint(SCREEN_WIDTH / 2, 0, color1, SCREEN_WIDTH / 2, SCREEN_HEIGHT, color2);
        g2D.setPaint(gp);
        g2D.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        for (Obstacle o: obstacles) {
            o.draw(g2D);
        }
        if(debugOn) {
            debug.draw(g2D);
        }
        player.draw(g2D);
        map.draw(g2D);
        for (SlimeBall s: slimeBalls) {
            s.draw(g2D);
        }
    }
    private void drawGameOver(Graphics2D g2D) {
        g2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 80));
        g2D.setColor(Color.RED);
        g2D.drawString("Game Over", getCenteredXForString("Game Over", g2D), 200);
        g2D.setColor(Color.WHITE);
        g2D.drawString("Your Points: " + Map.points, getCenteredXForString("Your Points:" + Map.points, g2D), 350);
        if(Map.points > Map.readHighScore()) {
            g2D.drawString("Congratulations! New High Score", getCenteredXForString("Congratulations! New High Score", g2D), 500);
        }
        g2D.drawString("Press Enter to play again...", getCenteredXForString("Press Enter to play again...", g2D), 800);
    }
}
