package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RedWin extends JDialog {
    public RedWin(){
        this.setBounds(100, 100, 500, 200);
        this.setVisible(true);
        this.setTitle("");
        setLocationRelativeTo(null);
        JLabel jLabel = new JLabel("The Red Is Winner!");
        jLabel.setBounds(100, 300, 100, 200);
        add(jLabel,BorderLayout.CENTER);
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
