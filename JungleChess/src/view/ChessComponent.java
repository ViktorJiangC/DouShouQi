package view;


import model.ChessPiece;
import model.Chessboard;
import model.ChessboardPoint;
import model.PlayerColor;
import javax.swing.*;
import java.awt.*;

/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
public class ChessComponent extends JComponent {
    private PlayerColor owner;
    private String name;

    private boolean selected;

    public ChessComponent(PlayerColor owner, int size, String name) {
        this.owner = owner;
        this.selected = false;
        this.name = name;
        setSize(size/2, size/2);
        setLocation(0,0);
        setVisible(true);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("楷体", Font.PLAIN, getWidth() / 2);
        g2.setFont(font);
        g2.setColor(owner.getColor());
        g2.drawString(this.name, getWidth() / 4, getHeight() * 5 / 8); //FIXME: Use library to find the correct offset.
        if (isSelected()) { //Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(3, 3 ,getWidth()-6 , getHeight()-6);
        }
    }
}