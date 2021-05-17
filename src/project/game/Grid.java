package project.game;

import project.generation.BoolMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.ArrayList;

public class Grid extends JPanel
{
	private int sizeX, sizeY, squareSize;

	private Color backColor;
	private Color netColor;
	private Block[][] blocks;

	Grid(int sizeX, int sizeY, int squareSize, Color backColor, Color netColor) {
		this.backColor = backColor;
		this.netColor = netColor;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.squareSize = squareSize;
		blocks = new Block[sizeY][sizeX];

		setLayout(null);
		setBackground(backColor);
		setSize((squareSize + 1) * sizeX + 1, (squareSize + 1) * sizeY + 1);

		addContainerListener(new ContainerListener()
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

		//setBorder(BorderFactory.createLineBorder(Color.BLUE));
	}

	Color getBackColor() {
		return backColor;
	}

	private boolean containsSegment(int i, int j) {
		if (i >= blocks.length || j >= blocks[0].length || i < 0 || j < 0) {
			return false;
		}
		return blocks[i][j] != null;
	}

	void add(Block block, int x, int y) {
		for (int i = 0; i < block.getSegmentsY(); i++) {
			for (int j = 0; j < block.getSegmentsX(); j++) {
				if (block.exists(i, j)) {
					Block segment = new Block(1, 1, 1, block.getImage());
					blocks[y + i][x + j] = segment;
					segment.setLocation((x + j) * (Block.SIZE + 1) + 1, (y + i) * (Block.SIZE + 1) + 1);
					add(segment);
				}
			}
		}
	}

	void clear() {
		for (int i = 0; i < blocks.length; i++) {
			removeRow(i);
		}
	}

	private void remove(int x, int y) {
		if (blocks[y][x] != null) {
			remove(blocks[y][x]);
			blocks[y][x] = null;
		}
	}

	private void removeRow(int i) {
		for (int j = 0; j < blocks[i].length; j++) {
			remove(j, i);
		}
	}

	private void removeColumn(int j) {
		for (int i = 0; i < blocks.length; i++) {
			remove(j, i);
		}
	}

	private void removeRows(ArrayList<Integer> rows) {
		for (Integer row : rows) {
			removeRow(row);
		}
	}

	private void removeColumns(ArrayList<Integer> columns) {
		for (Integer column : columns) {
			removeColumn(column);
		}
	}

	private ArrayList<Integer> getFilledRows() {
		ArrayList<Integer> rows = new ArrayList<>();
		for (int i = 0; i < blocks.length; i++) {
			boolean full = true;
			for (int j = 0; j < blocks[i].length; j++) {
				if (blocks[i][j] == null) {
					full = false;
					break;
				}
			}
			if (full) {
				rows.add(i);
			}
		}
		return rows;
	}

	private ArrayList<Integer> getFilledColumns() {
		ArrayList<Integer> columns = new ArrayList<>();
		for (int j = 0; j < blocks[0].length; j++) {
			boolean full = true;
			for (Block[] block : blocks) {
				if (block[j] == null) {
					full = false;
					break;
				}
			}
			if (full) {
				columns.add(j);
			}
		}
		return columns;
	}

	void removeFilledLines() {
		ArrayList<Integer> rows = getFilledRows();
		ArrayList<Integer> columns = getFilledColumns();
		removeColumns(columns);
		removeRows(rows);
	}

	boolean canPlace(Block[] blocks) {
		for (Block block : blocks) {
			for (int y = 0; y < sizeY; y++) {
				for (int x = 0; x < sizeX; x++) {
					for (int r = 0; r <= 3; r++) {
						if (canPlace(block, x, y, r)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	boolean canPlace(Block block, int x, int y, int rotation) {
		BoolMap struct = block.getStructure();
		for (int r = 0; r < rotation; r++) {
			struct = struct.rotate().shiftUp().shiftLeft();
		}
		int i, j;
		for (i = 0; i < (rotation % 2 == 0 ? block.getSegmentsY() : block.getSegmentsX()); i++) {
			for (j = 0; j < (rotation % 2 != 0 ? block.getSegmentsY() : block.getSegmentsX()); j++) {
				if (y + i >= blocks.length || x + j >= blocks[0].length || y + i < 0 || x + j < 0) {
					return false;
				}
				if (struct.get(i, j) && containsSegment(y + i, x + j)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println("Рисую сетку");
		g.setColor(netColor);
		for (int i = 0; i <= sizeX; i++) {
			g.drawLine(i * squareSize + i, 0, i * squareSize + i, getHeight());
		}
		for (int i = 0; i <= sizeY; i++) {
			g.drawLine(0, i * squareSize + i, getWidth(), i * squareSize + i);
		}
	}
}
