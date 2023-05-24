package view;

import javax.swing.*;
import java.awt.*;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on ChessboardComponent
 */

public class CellComponent extends JPanel {
    private Color background;
    private ImageIcon icon;
    private boolean isValidMoveTo;
    public CellComponent(Color background, Point location, int size) {
        setLayout(new GridLayout(1,1));
        setLocation(location);
        setSize(size, size);
        this.background = background;
    }

    public CellComponent(Color background, Point location, int size,String path) {
        setLayout(new GridLayout(1,1));
        setLocation(location);
        setSize(size, size);
        this.background = background;
        icon =new ImageIcon(path);
    }
    public boolean isValidMoveTo(){return isValidMoveTo;}
    public void setIsValidMoveTo(boolean isValidMoveTo){this.isValidMoveTo=isValidMoveTo;}
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.setColor(background);
        g.fillRect(1, 1, this.getWidth()-1, this.getHeight()-1);
        g.drawImage(icon.getImage(),1,1,this);
        if(isValidMoveTo()){
            g.setColor(Color.RED);
            g.drawOval(8,8,getWidth()-16,getHeight()-16);
        }
    }
}
