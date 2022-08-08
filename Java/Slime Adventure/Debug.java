import java.awt.*;

public class Debug {
    public static String message = " ";

    Debug() {

    }

    public static void print(String s) {
        message = s;
    }
    protected void draw(Graphics2D g2D) {
        g2D.setColor(Color.WHITE);
        g2D.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        g2D.drawString(message, 20, 40);
    }
}
