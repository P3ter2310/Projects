import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Obstacle {
    private int x = Engine.SCREEN_WIDTH;

    private int obstacleY;
    public boolean destroyable;
    public boolean destroyed = false;
    private int y = Engine.SCREEN_HEIGHT / 2 - (Engine.TILE_SIZE / 2);

    BufferedImage wall1, wall2;


    Obstacle(int obstacleY, boolean destroyable) {
        this.destroyable = destroyable;
        this.obstacleY = obstacleY;
        this.y -= Engine.TILE_SIZE * this.obstacleY;
        try {
            wall1 = ImageIO.read(getClass().getResourceAsStream("/wall1.png"));
            wall2 = ImageIO.read(getClass().getResourceAsStream("/wall2.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int getX() {
        return this.x;
    }

    protected void update() {
        x -= Engine.GAME_SPEED;
    }

    protected void draw(Graphics2D g2D) {
        if (destroyable) {
            g2D.drawImage(wall2, this.x, this.y, null);
        }else {
            g2D.drawImage(wall1, this.x, this.y, null);
        }
    }

    public int getY() {
        return this.y;
    }
}
