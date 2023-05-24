package view;

import controller.GameController;
import controller.GameSaveLoad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class SaveDialog extends JDialog {

    public SaveDialog(GameController gameController) {
        setLocationRelativeTo(null); // Center the window.
        JButton newMemory = new JButton("新增存档");
        setSize(300, 400);
        add(newMemory);
//        add(newMemory);
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
                            GameSaveLoad.saveGame(gameController, finalI +1);
                            setVisible(false);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                add(jLabel);
            }
        }
        setLayout(null);


        this.setVisible(true);

        newMemory.addActionListener(e->{
            int index = files.length;
            if (index>=3){
                JOptionPane.showMessageDialog(this,"最多只能存在3个存档");
                return;
            }
            JLabel jLabel = new JLabel("存档"+(index+1));
            jLabel.setOpaque(true);
            jLabel.setSize(200, 60);
            jLabel.setLocation(50, 120+index*70);
            jLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            jLabel.setPreferredSize(new Dimension(200, 60));
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
                        GameSaveLoad.saveGame(gameController,index+1);
                        setVisible(false);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            add(jLabel);
            doLayout();
            repaint();
//            this.setVisible(false);
        });
    }
}
