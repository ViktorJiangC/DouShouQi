package Start;

import javax.swing.*;
import java.awt.*;

public class ImageComponent extends JComponent {
    Image paintImage;

    public ImageComponent(Image image) {
        this.setLayout(null);
        this.setFocusable(true);//Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        this.paintImage = image;
        repaint();//execute the paintComponent method
    }

    // the method describes how to paint, and being invoked by repaint() method.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(paintImage, 0, 0, paintImage.getWidth(this), paintImage.getHeight(this), this);
    }
}
