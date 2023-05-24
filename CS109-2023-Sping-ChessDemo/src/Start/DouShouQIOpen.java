package Start;

import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class DouShouQIOpen {
    public static void drawUI(){
        JFrame jf=new JFrame("斗兽棋");
        String path2="D:\\桌面\\CS109-2023-Sping-ChessDemo\\pictures\\lion.png";
        String path3="D:\\桌面\\CS109-2023-Sping-ChessDemo\\pictures\\wolf.png";
        Icon icon1=new ImageIcon(path2);
        Icon icon2=new ImageIcon(path3);
        jf.setSize(1872,1405);
        jf.setDefaultCloseOperation(3);
        jf.setLayout(null);
        JButton jButton1=new JButton(icon1);
        jButton1.setBounds(736,600,200,120);
        jButton1.setFocusPainted(false);
        jf.add(jButton1);
        JButton jButton2=new JButton(icon2);
        jButton2.setBounds(736,750,200,120);
        jButton2.setFocusPainted(false);
        jf.add(jButton2);
        String path1="D:\\桌面\\CS109-2023-Sping-ChessDemo\\pictures\\fpic9871.jpg";
        Image image=Toolkit.getDefaultToolkit().getImage(path1).getScaledInstance(1872,1000,Image.SCALE_FAST);
        JComponent imageComponent = new ImageComponent(image);
        imageComponent.setSize(1872, 1405);
        imageComponent.setLocation(0,0);
        jf.add(imageComponent);
        jf.setVisible(true);
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.setVisible(false);
                SwingUtilities.invokeLater(() -> {
                    ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
                    GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
                    mainFrame.setVisible(true);
                });
            }
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.setVisible(false);
                new levelChoice();
            }
        });
    }
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        DouShouQIOpen.drawUI();
    }
}
