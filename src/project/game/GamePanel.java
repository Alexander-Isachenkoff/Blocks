package project.game;

import project.components.ExtendedLabel;
import project.generation.Generator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel
{
	static final String fontName = Font.SANS_SERIF;
	static final Color YELLOWISH_GREEN = new Color(147, 199, 99);
	static final Color SHADOW = new Color(32, 32, 32);
	private static final File saveFile = new File("records.bin");
	private static final int PADDING = Block.SIZE / 2;
	private static final Color SOFT_RED = new Color(205, 40, 40);
	private static final Color gridBaseBackColor = new Color(40, 40, 40);
	private static final Color gridBaseNetColor = new Color(92, 92, 92);
	private final int maxBlockWidth, maxBlockHeight;
	private final int blocksAtOnes = 4;
	private Component window;
	private Image[] images;
	private JPanel glassPanel;
	private JPanel infoPanel;
	private ExtendedLabel newGame, exit;
	private RecordsTable table;
	private ArrayList<Record> records;
	// Очки
	private int scoreValue;
	private ExtendedLabel scoreTextBox;
	private ExtendedLabel scoreCountBox;
	// Сетка
	private Grid grid;
	private SmartBlock[] blocks;
	
	public GamePanel(int sizeX, int sizeY, int maxBlockWidth, int maxBlockHeight) throws IOException, ClassNotFoundException {
		try {
			records = GameIO.readRecords(saveFile);
		} catch (Exception e) {
			records = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				records.add(new Record("Пусто", 0));
			}
			File file = new File(saveFile.getPath());
			file.createNewFile();
			try {
				GameIO.writeRecords(records, saveFile);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}

		images = GameIO.readImages();
		this.maxBlockHeight = maxBlockHeight;
		this.maxBlockWidth = maxBlockWidth;
		setLayout(null);
		setBackground(Color.DARK_GRAY);

		grid = new Grid(sizeX, sizeY, Block.SIZE, gridBaseBackColor, gridBaseNetColor);

		/* glassPanel */
		glassPanel = new JPanel(null)
		{
			@Override
			public void paintComponent(Graphics g) {
				glassPanel.setBounds(0, 0, grid.getWidth() + PADDING * 2, grid.getHeight() + 3 * PADDING + (2 * ((GamePanel) getParent()).maxBlockHeight + 1) * (Block.SIZE + 1) + Block.SIZE);
				grid.setLocation(PADDING, PADDING);
				g.drawRect(grid.getX(), grid.getY() + grid.getHeight() + PADDING, grid.getWidth(), grid.getHeight());
			}
		};

		glassPanel.addContainerListener(new ContainerListener()
		{
			@Override
			public void componentAdded(ContainerEvent e) {
				e.getComponent().repaint();
			}

			@Override
			public void componentRemoved(ContainerEvent e) {
				e.getComponent().repaint();
			}
		});

		glassPanel.setOpaque(false);
		glassPanel.setBounds(0, 0, grid.getWidth() + PADDING * 2, grid.getHeight() + 3 * PADDING + (2 * maxBlockHeight + 1) * (Block.SIZE + 1) + Block.SIZE);

		// Панель информации
		final int border = 2;
		infoPanel = new JPanel(null);
		infoPanel.setOpaque(false);
		infoPanel.setLocation(glassPanel.getWidth(), -border);
		infoPanel.setBorder(BorderFactory.createLineBorder(grid.getBackColor(), border));
		scoreTextBox = new ExtendedLabel("СЧЁТ", YELLOWISH_GREEN, SHADOW, new Font(fontName, Font.BOLD, 48));
		scoreCountBox = new ExtendedLabel(String.valueOf(scoreValue), Color.WHITE, SHADOW, new Font(fontName, Font.BOLD, 36))
		{
			@Override
			public void paintComponent(Graphics g) {
				scoreCountBox.setLocation((infoPanel.getWidth() - scoreCountBox.getWidth()) / 2, scoreTextBox.getY() + scoreTextBox.getHeight());
			}
		};

		exit = new ExtendedLabel("Выход", Color.WHITE, SHADOW, new Font(fontName, Font.BOLD, 28));

		MouseAdapter colorChanger = new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e) {
				((ExtendedLabel) e.getComponent()).setColor(SOFT_RED);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				((ExtendedLabel) e.getComponent()).setColor(Color.WHITE);
			}
		};

		newGame = new ExtendedLabel("Новая игра", Color.WHITE, SHADOW, new Font(fontName, Font.BOLD, 28));
		newGame.addMouseListener(colorChanger);
		newGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onNewGame();
			}
		});
		exit.addMouseListener(colorChanger);
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onExit();
			}
		});

		table = new RecordsTable();
		table.fill(records);

		infoPanel.setSize(table.getWidth() + 2 * PADDING, glassPanel.getHeight() + border * 2);

		scoreTextBox.setLocation((infoPanel.getWidth() - scoreTextBox.getWidth()) / 2, PADDING);
		scoreCountBox.setLocation((infoPanel.getWidth() - scoreCountBox.getWidth()) / 2, scoreTextBox.getY() + scoreTextBox.getHeight());
		table.setLocation(PADDING, scoreCountBox.getY() + scoreCountBox.getHeight() + 2 * PADDING);
		exit.setLocation((infoPanel.getWidth() - exit.getWidth()) / 2, infoPanel.getHeight() - exit.getHeight() - PADDING);
		newGame.setLocation((infoPanel.getWidth() - newGame.getWidth()) / 2, exit.getY() - newGame.getHeight());

		infoPanel.add(table);
		infoPanel.add(newGame);
		infoPanel.add(exit);
		infoPanel.add(scoreCountBox);
		infoPanel.add(scoreTextBox);
		setPreferredSize(new Dimension(glassPanel.getWidth() + infoPanel.getWidth() - border, glassPanel.getHeight()));
		add(glassPanel);
		add(infoPanel);
		add(grid);
	}
	
	private void onNewGame() {
		if (JOptionPane.showConfirmDialog(window, "Начать новую игру?", "Новая игра", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			start();
		}
	}
	
	private void onExit() {
		if (JOptionPane.showConfirmDialog(window, "Выйти из игры?", "Выход", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	void addScore(int value) {
		setScore(scoreValue += value);
	}
	
	private void setScore(int value) {
		scoreCountBox.setText(String.valueOf(scoreValue = value));
	}
	
	Grid getGrid() {return grid;}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		getParent().setSize(getSize());
	}

	void addBlocks() {
		for (int i = 0; i < blocks.length; i++)
			if (blocks[i] == null) {
				blocks[i] = new SmartBlock(Generator.getInt(1, 9), maxBlockHeight, maxBlockWidth, images[Generator.getInt(0, images.length - 1)], this);
				blocks[i].setStartPos(
						PADDING + Block.SIZE / 2 + (i / 2) * (Block.SIZE + 1) * (maxBlockWidth + 1) + 1,
						PADDING + Block.SIZE / 2 + (i % 2) * (Block.SIZE + 1) * (maxBlockHeight + 1) + 1 + grid.getY() + grid.getHeight());
				blocks[i].setLocation(blocks[i].getStartPos());
				glassPanel.add(blocks[i]);
			}


		if (!grid.canPlace(blocks)) {
			if (scoreValue > records.get(records.size() - 1).score) {
				String name = JOptionPane.showInputDialog(window, "Введите своё имя:", "Игра окончена", JOptionPane.PLAIN_MESSAGE);
				if (name != null && !name.isEmpty()) {
					for (int i = 0; i < records.size(); i++) {
						if (scoreValue > records.get(i).score) {
							records.add(i, new Record(name, scoreValue));
							records.remove(records.size() - 1);
							break;
						}
					}
					try {
						GameIO.writeRecords(records, saveFile);
					} catch (IOException ex) {
						System.out.println(ex.getMessage());
					}
				}
				table.fill(records);
			} else {
				JOptionPane.showMessageDialog(window, "Игра окончена", "Игра окончена", JOptionPane.INFORMATION_MESSAGE);
			}
			start();
		}
	}

	SmartBlock[] getBlocks() { return blocks; }

	private void start() {
		window = getTopLevelAncestor();
		if (blocks != null) {
			for (int i = 0; i < blocks.length; i++) {
				glassPanel.remove(blocks[i]);
				blocks[i] = null;
			}
		}
		blocks = new SmartBlock[blocksAtOnes];
		grid.clear();
		setScore(0);
		addBlocks();
	}
}