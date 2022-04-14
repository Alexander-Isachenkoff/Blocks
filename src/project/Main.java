package project;

import project.game.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main
{
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

	public static void main(String[] args) {
		setSystemTheme();
		setRussianButtonsText();
		JFrame gameFrame = new JFrame("Блоки");
		gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
		GamePanel gamePanel;
		try {
			gameFrame.setIconImage(ImageIO.read(Main.class.getResourceAsStream("icon.png")));
			gamePanel = new GamePanel(10, 10, 4, 4);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
			return;
		}
		gameFrame.add(gamePanel);
		gameFrame.pack();
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setVisible(true);
	}
}
