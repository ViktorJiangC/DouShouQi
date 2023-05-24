package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class notYourTurn extends JDialog {
    public notYourTurn() {
        this.setBounds(100, 100, 500, 200);
        this.setVisible(true);
        this.setTitle("Wrong");
        setLocationRelativeTo(null);
        JLabel jLabel = new JLabel("  It is not your turn.");
        jLabel.setBounds(100, 300, 100, 200);
        add(jLabel);
        JPanel jp=new JPanel();
        jp.setPreferredSize(new Dimension(100,100));
        add(jp,BorderLayout.SOUTH);
        JButton jButton=new JButton("确定");
        jButton.setBounds(0,50,50,30);
        jp.add(jButton);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }
}