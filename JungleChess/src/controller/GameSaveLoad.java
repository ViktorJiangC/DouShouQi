package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GameSaveLoad {
    private static final String SAVE_FILE = "save.txt";

    // 存档
    public static void saveGame(String chessboardState, String playerInfo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            writer.write(chessboardState);
            writer.newLine();
            writer.write(playerInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读档
    public static void loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String chessboardState = reader.readLine();
            String playerInfo = reader.readLine();

            // 将读取到的数据重新加载到游戏中
            // TODO: 这里需要根据你的具体游戏实现来恢复游戏状态
            System.out.println("棋盘状态：" + chessboardState);
            System.out.println("玩家信息：" + playerInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 示例：保存游戏并读取游戏
    public static void main(String[] args) {
        String chessboardState = "当前棋盘状态";
        String playerInfo = "玩家信息";

        // 存档
        saveGame(chessboardState, playerInfo);

        // 读档
        loadGame();
    }
}
