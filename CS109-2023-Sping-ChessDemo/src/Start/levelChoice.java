package Start;

import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class levelChoice extends JDialog {
    public levelChoice(){
        this.setBounds(100, 100, 400, 200);
        this.setVisible(true);
        this.setTitle("难度选择");
        setLocationRelativeTo(null);
        JPanel jp=new JPanel();
        jp.setPreferredSize(new Dimension(100,100));
        add(jp,BorderLayout.SOUTH);
        JButton jButton1=new JButton("简单");
        jButton1.setBounds(0,50,50,30);
        jp.add(jButton1);
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);SwingUtilities.invokeLater(() -> {
                    ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
                    GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
                    mainFrame.setVisible(true);
                    gameController.setMode(1);
                });
            }
        });
        JButton jButton2=new JButton("中等");
        jButton2.setBounds(0,50,50,30);
        jp.add(jButton2);
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                SwingUtilities.invokeLater(() -> {
                    ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
                    GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
                    mainFrame.setVisible(true);
                    gameController.setMode(2);
                });
            }
        });
        JButton jButton3=new JButton("困难");
        jButton3.setBounds(0,50,50,30);
        jp.add(jButton3);
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                SwingUtilities.invokeLater(() -> {
                    ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
                    GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
                    mainFrame.setVisible(true);
                    gameController.setMode(2);
                });
            }
        });
    }
}
