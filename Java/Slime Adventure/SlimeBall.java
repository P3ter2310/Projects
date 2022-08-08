import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SlimeBall {
    private int x = Engine.SCREEN_WIDTH / 2;

    private int y;
    public boolean destroyed = false;
    BufferedImage slimeBall;


    SlimeBall() {
        y = Player.screenPositionY;
        try {
            slimeBall = ImageIO.read(getClass().getResourceAsStream("/slimeball.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int getX() {
        return this.x;
    }

    protected void update() {
        x += (Engine.GAME_SPEED * 2);
        checkCollision();
    }

    private void checkCollision() {
        for(Obstacle o: Engine.obstacles) {
            if(this.x + Engine.TILE_SIZE / 4 >= o.getX()
                    && this.x <= o.getX() + Engine.TILE_SIZE
                    && this.y + Engine.TILE_SIZE / 4 >= o.getY()
                    && this.y + 2 <= o.getY() + Engine.TILE_SIZE) {
                if(o.destroyable) {
                    o.destroyed = true;
                }
                this.destroyed = true;
            }
        }
    }

    protected void draw(Graphics2D g2D) {
        g2D.setColor(Color.BLUE);
        g2D.drawImage(slimeBall, this.x, this.y, null);
    }
}
