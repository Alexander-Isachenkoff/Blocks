package project;

import project.game.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class Main
{
	public static void main(String[] args) {
		setSystemTheme();
		setRussianButtonsText();
		try {
			runGame();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void runGame() throws IOException, ClassNotFoundException {
		JFrame gameFrame = new JFrame("Блоки");
		gameFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		gameFrame.setResizable(false);
		gameFrame.setIconImage(ImageIO.read(Main.class.getResourceAsStream("/icon.png")));
		GamePanel gamePanel = new GamePanel(10, 10, 4, 4);
		gameFrame.add(gamePanel);
		gameFrame.pack();
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setVisible(true);
	}
	
	private static void setSystemTheme() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	private static void setRussianButtonsText() {
		UIManager.put("OptionPane.yesButtonText", "Да");
		UIManager.put("OptionPane.noButtonText", "Нет");
		UIManager.put("OptionPane.cancelButtonText", "Отмена");
	}
}
