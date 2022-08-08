import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player {
    public static int screenPositionX;
    public static int screenPositionY;
    public static int DefaultScreenX = Engine.SCREEN_WIDTH / 2 - (Engine.TILE_SIZE / 2);
    public static int DefaultScreenY = Engine.SCREEN_HEIGHT / 2 - (Engine.TILE_SIZE / 2);

    private int height = Engine.TILE_SIZE;
    private int width = Engine.TILE_SIZE;

    public static boolean inAnimation;
    public static int animationState;
    public static final int normalState = 0;
    private final int jumpState = 1;
    private final int fallState = 2;
    private final int crawlState = 3;
    public static int animationCounter;
    public static BufferedImage slime1, slime2, slime3, avatar;

    Player() {
        setStartValues();
        try {
            slime1 = ImageIO.read(getClass().getResourceAsStream("/slime1.png"));
            slime2 = ImageIO.read(getClass().getResourceAsStream("/slime2.png"));
            slime3 = ImageIO.read(getClass().getResourceAsStream("/slime3.png"));
            avatar = slime1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setStartValues() {
        screenPositionX = DefaultScreenX;
        screenPositionY = DefaultScreenY;
        inAnimation = false;
        animationState = normalState;
        animationCounter = 0;
    }

    protected void draw(Graphics2D g2D) {
        g2D.setColor(Color.WHITE);
        g2D.drawImage(avatar, this.screenPositionX, this.screenPositionY, null);
    }

    private int jumpSpeed = 6;
    protected void update() {
        checkCollision();
        if(!this.inAnimation) {
            if(KeyHandler.upPressed) {
                this.inAnimation = true;
                this.animationState = this.jumpState;
                this.animationCounter = 60;
                this.screenPositionY = this.DefaultScreenY;
            }
            if(KeyHandler.downPressed) {
                this.inAnimation = true;
                this.animationState = this.crawlState;
                this.animationCounter = 80;
            }
        } else {
            if(this.animationState == this.crawlState) {
                avatar = slime3;
                this.screenPositionY = this.DefaultScreenY + Engine.TILE_SIZE / 2;
                this.height = Engine.TILE_SIZE / 2;
                this.animationCounter--;
            }
            if(this.animationState == this.jumpState) {
                if(this.animationCounter == 50) {
                    jumpSpeed = 5;
                }
                if(this.animationCounter == 30) {
                    jumpSpeed = 4;
                }
                if(this.animationCounter == 10) {
                    jumpSpeed = 3;
                }
                this.screenPositionY -= jumpSpeed;
                this.animationCounter--;
            }
            if(this.animationState == this.fallState) {
                if(this.animationCounter == 50) {
                    jumpSpeed = 4;
                }
                if(this.animationCounter == 30) {
                    jumpSpeed = 5;
                }
                if(this.animationCounter == 10) {
                    jumpSpeed = 6;
                }
                this.screenPositionY += jumpSpeed;
                this.animationCounter--;
            }
            if (this.animationCounter <= 0) {
                if(this.animationState == this.jumpState) {
                    this.animationState = this.fallState;
                    this.animationCounter = 60;
                }else {
                    avatar = slime1;
                    this.animationState = this.normalState;
                    this.screenPositionY = this.DefaultScreenY;
                    this.height = Engine.TILE_SIZE;
                    this.inAnimation = false;
                }
            }
        }
    }

    private void checkCollision() {
        for(Obstacle o: Engine.obstacles) {
            if(this.screenPositionX + Engine.TILE_SIZE >= o.getX()
                    && this.screenPositionX <= o.getX() + Engine.TILE_SIZE
                    && this.screenPositionY + Engine.TILE_SIZE >= o.getY()
                    && this.screenPositionY <= o.getY() + Engine.TILE_SIZE) {
                Engine.gameState = Engine.gameOver;
            }
        }
    }
}
