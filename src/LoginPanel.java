/*package jogocalculadora;*/

import java.awt.*;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private Image loginImage;

    public LoginPanel(String imagePath) {
        loginImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        setLayout(new BorderLayout()); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(loginImage, 0, 0, getWidth(), getHeight(), this);
    }
}


