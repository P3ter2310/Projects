import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public static boolean upPressed = false;
    public static boolean downPressed = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int kCode = e.getKeyCode();


        if(Engine.gameState == Engine.menuState) {
            switch (kCode) {
                case KeyEvent.VK_ENTER -> Engine.gameState = Engine.runningState;
                case KeyEvent.VK_ESCAPE -> System.exit(0);
            }

        }else if(Engine.gameState == Engine.runningState) {
            switch (kCode) {
                case KeyEvent.VK_ESCAPE -> System.exit(0);
                case KeyEvent.VK_UP -> upPressed = true;
                case KeyEvent.VK_DOWN -> downPressed = true;
                case KeyEvent.VK_F1 -> Engine.debugOn = true;
                case KeyEvent.VK_SPACE -> {
                    Engine.slimeBalls.add(new SlimeBall());
                    Player.avatar = Player.slime2;
                }
            }

        }else if(Engine.gameState == Engine.gameOver) {
            switch(kCode) {
                case KeyEvent.VK_ENTER -> {
                    if(Map.points > Map.readHighScore()) {
                        Map.saveHighScore();
                    }
                    Engine.restart();
                    Engine.gameState = Engine.runningState;
                }
                case KeyEvent.VK_ESCAPE -> System.exit(0);
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int kCode = e.getKeyCode();

        if(Engine.gameState == Engine.menuState) {

        }else if(Engine.gameState == Engine.runningState) {
            switch (kCode) {
                case KeyEvent.VK_UP -> upPressed = false;
                case KeyEvent.VK_DOWN -> downPressed = false;
                case KeyEvent.VK_F1 -> Engine.debugOn = false;
                case KeyEvent.VK_SPACE -> Player.avatar = Player.slime1;
            }

        }else if(Engine.gameState == Engine.gameOver) {

        }
    }
}
