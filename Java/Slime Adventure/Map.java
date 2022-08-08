import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Map {

    public static int mapPosition = 0;
    public static int obstacleCounter = 0;

    public static final int GROUND_LEVEL = Engine.SCREEN_HEIGHT / 2 - (Engine.TILE_SIZE / 2) + Engine.TILE_SIZE;
    public static int points;

    BufferedImage groundImage;


    Random rand = new Random();
    Map() {
        try {
            groundImage = ImageIO.read(getClass().getResourceAsStream("/ground.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void update() {
        if(mapPosition % Engine.TILE_SIZE == 0) {
            obstacleCounter++;
            if(obstacleCounter == 3) {
                addObstacle(rand.nextInt(6));
                obstacleCounter = 0;
            }
        }
        mapPosition += Engine.GAME_SPEED;
        points = mapPosition / 10;
    }

    private void addObstacle(int type) {
        switch(type) {
            case 0 -> Engine.obstacles.add(new Obstacle(0, false));
            case 1 -> {
                Engine.obstacles.add(new Obstacle(0, false));
                Engine.obstacles.add(new Obstacle(1, false));
            }
            case 2 -> {
                Engine.obstacles.add(new Obstacle(0, false));
                Engine.obstacles.add(new Obstacle(1, false));
                Engine.obstacles.add(new Obstacle(7, false));
                Engine.obstacles.add(new Obstacle(8, false));
            }
            case 3 -> {
                Engine.obstacles.add(new Obstacle(1, false));
                Engine.obstacles.add(new Obstacle(2, false));
            }
            case 4 -> Engine.obstacles.add(new Obstacle(0, true));
            case 5 -> {
                Engine.obstacles.add(new Obstacle(0, true));
                Engine.obstacles.add(new Obstacle(1, true));
            }
        }
    }

    protected void draw(Graphics2D g2D) {
        drawGround(g2D);
        drawPoints(g2D);
    }

    private void drawPoints(Graphics2D g2D) {
        g2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
        g2D.setColor(Color.WHITE);
        g2D.drawString("Points: " + points, Engine.getCenteredXForString("Points: " + points, g2D), 2 * Engine.TILE_SIZE);
        g2D.drawString("High Score: " + Engine.highScore, Engine.getCenteredXForString("High Score: " + Engine.highScore, g2D), 3 * Engine.TILE_SIZE);
    }

    private void drawGround(Graphics g2D) {
        g2D.setColor(Color.GREEN);
        for(int i = 0; i <= Engine.TILES_LEFT_RIGHT; i++) {
            //g2D.drawRect( i * Engine.TILE_SIZE - (mapPosition % Engine.TILE_SIZE), GROUND_LEVEL, Engine.TILE_SIZE, Engine.TILE_SIZE);
            g2D.drawImage(groundImage, i * Engine.TILE_SIZE - (mapPosition % Engine.TILE_SIZE), GROUND_LEVEL, null);
        }
    }

    public static void saveHighScore() {
        File f = new File("highscore.txt");
        try {
            FileWriter fw = new FileWriter(f);
            fw.write("" + points);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int readHighScore() {
        File f = new File("highscore.txt");
        try {
            Scanner sc = new Scanner(f);
            if (sc.hasNextLine()) {
                sc.close();
                return Integer.parseInt(sc.nextLine());
            }
            sc.close();
            return 0;
        } catch (FileNotFoundException e) {
            return 0;
        }
    }
}
