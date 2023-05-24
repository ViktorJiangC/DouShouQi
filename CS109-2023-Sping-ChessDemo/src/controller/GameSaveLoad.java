package controller;

import view.ChessGameFrame;

import java.io.*;

public class GameSaveLoad {
    private static final String SAVE_FILE = "memory/save";

    // 存档
    public static void saveGame(GameController gameController, int i) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(SAVE_FILE+i));

        objectOutputStream.writeObject(gameController);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    // 读档
    public static GameController loadGame(int finalI) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(SAVE_FILE+finalI));
        GameController gameController = (GameController)objectInputStream.readObject();
        objectInputStream.close();
        return gameController;
    }

    // 示例：保存游戏并读取游戏
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String chessboardState = "当前棋盘状态";
        String playerInfo = "玩家信息";

        System.setProperty("file.encoding", "UTF-8");

        ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
        GameController gameController2 = loadGame(0);

        gameController2.setView(mainFrame.getChessboardComponent());
        GameController gameController = new GameController(mainFrame.getChessboardComponent(), gameController2.getModel());
        mainFrame.setVisible(true);
    }
}
