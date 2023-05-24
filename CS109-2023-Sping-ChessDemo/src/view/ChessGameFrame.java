package view;

import Start.ImageComponent;
import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;

    private final int ONE_CHESS_SIZE;

    private ChessboardComponent chessboardComponent;
    public ChessGameFrame(int width, int height) {
        setTitle("Jungle Chess"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        addChessboard();
        addLabel();
        addHelloButton();
        addButton1();
        addSaveButton();
        addLoadButton();
        String path="D:\\桌面\\CS109-2023-Sping-ChessDemo\\pictures\\background.jpg";
        Image image=Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(1872,1000,Image.SCALE_FAST);
        JComponent imageComponent = new ImageComponent(image);
        imageComponent.setSize(1872, 1405);
        imageComponent.setLocation(0,0);
        add(imageComponent);
    }

    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }
    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE);
        chessboardComponent.setLocation(HEIGTH / 5, HEIGTH / 10);
        add(chessboardComponent);
    }
    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        JLabel statusLabel = new JLabel("选项");
        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("思源宋体", Font.BOLD, 20));
        add(statusLabel);
    }
    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */
    private void addHelloButton() {
        JButton button = new JButton("重置游戏");
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Let's start the game!"));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameController.resetGame();
            }
        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("思源宋体", Font.BOLD, 20));
        add(button);
    }
    private void addButton1(){
        JButton button = new JButton("悔棋");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(GameController.turn>=1) GameController.Huiqi();
            }
        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 500);
        button.setSize(200, 60);
        button.setFont(new Font("思源宋体", Font.BOLD, 20));
        add(button);
    }
    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.addActionListener((e) -> {
            new SaveDialog( chessboardComponent.getGameController());
//            try {
//                GameSaveLoad.saveGame( chessboardComponent.getGameController());
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }


    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.addActionListener((e) -> {
            LoadGameDialog loadGameDialog = new LoadGameDialog(this);
        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
//    private void addLoadButton() {
//        JButton button = new JButton("Load");
//        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
//        button.setSize(200, 60);
//        button.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(button);
//
//        button.addActionListener(e -> {
//            System.out.println("Click load");
//            String path = JOptionPane.showInputDialog(this,"Input Path here");
//            gameController.loadGameFromFile(path);
//        });
//    }
}
