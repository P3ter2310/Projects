import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame Window = new JFrame();
        Window.setUndecorated(true);

        Engine e = new Engine();

        Window.add(e);

        Window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Window.setResizable(false);
        Window.setTitle("Slime Adventure");
        Window.setVisible(true);

        e.startGameThread();
    }
}