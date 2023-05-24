package view;

import controller.GameController;
import controller.GameSaveLoad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class LoadGameDialog extends JDialog {

    public LoadGameDialog(ChessGameFrame chessGameFrame) {
        setLocationRelativeTo(null); // Center the window.
        JLabel newMemory = new JLabel("读取存档");
        setSize(300, 400);
        newMemory.setOpaque(true);
        newMemory.setBackground(Color.ORANGE);
        add(newMemory);
        newMemory.setLocation(50, 50);
        newMemory.setSize(200, 60);
        File memory = new File("memory");
        File[] files = memory.listFiles();
        if (files!=null){
            for (int i = 0; i < files.length; i++) {
                JLabel jLabel = new JLabel("存档"+(i+1));
                jLabel.setOpaque(true);
                jLabel.setSize(200, 60);
                jLabel.setLocation(50, 120+i*70);
                jLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
                jLabel.setPreferredSize(new Dimension(200, 60));
                int finalI = i;
                jLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        jLabel.setBackground(Color.GRAY);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        jLabel.setBackground(null);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        try {
                            GameController gameController = GameSaveLoad.loadGame(finalI+1);
                            ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);

                            gameController.setView(mainFrame.getChessboardComponent());
                            new GameController(mainFrame.getChessboardComponent(), gameController.getModel());
                            mainFrame.setVisible(true);
                            chessGameFrame.setVisible(false);
                            setVisible(false);
                        } catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                });
                add(jLabel);
            }
        }
        setLayout(null);

        this.setVisible(true);
    }
}
