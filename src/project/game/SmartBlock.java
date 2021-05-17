package project.game;

import project.positioning.Shift;

import java.awt.*;
import java.awt.event.*;

class SmartBlock extends Block
{
	private GamePanel gameField;

	private Point startPos;
	private boolean leftMousePressed;
	private Point clickPos;
	private Point relativePos;
	private Point estimatedPos;
	private Shift shift;
	SmartBlock(int quantity, int maxHeight, int maxWidth, Image image, GamePanel gamePanel) {
		super(quantity, maxHeight, maxWidth, image);
		gameField = gamePanel;
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentMoved(ComponentEvent e) {
				estimatedPos = new Point(
						(getX() - gameField.getGrid().getX() + SIZE / 2) / (SIZE + 1),
						(getY() - gameField.getGrid().getY() + SIZE / 2) / (SIZE + 1));
				repaintCircuit();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent e) {
				if (leftMousePressed) {
					if (shift != null && shift.isRunning()) {
						shift.stop();
					}
					if ((relativePos = e.getComponent().getParent().getMousePosition()) != null) {
						e.getComponent().setLocation(relativePos.x - clickPos.x, relativePos.y - clickPos.y);
					}
				}
			}
		});

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1) {
					leftMousePressed = true;
					clickPos = e.getPoint();
					relativePos = e.getComponent().getParent().getMousePosition();
				} else if (e.getButton() == 3 && leftMousePressed) {
					clickPos.x = e.getComponent().getHeight() - e.getY();
					clickPos.y = e.getX();
					e.getComponent().setLocation(
							e.getComponent().getX() + e.getX() - clickPos.x,
							e.getComponent().getY() + e.getY() - e.getComponent().getWidth() + (e.getComponent().getWidth() - clickPos.y));
					rotate();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1) {
					leftMousePressed = false;
					if (gameField.getGrid().canPlace((SmartBlock) e.getComponent(), estimatedPos.x, estimatedPos.y, 0)) {
						e.getComponent().getParent().remove(getCircuit());
						e.getComponent().getParent().remove(e.getComponent());
						gameField.getGrid().add((SmartBlock) e.getComponent(), estimatedPos.x, estimatedPos.y);
						gameField.addScore(getValue());
						gameField.getGrid().removeFilledLines();

						for (int i = 0; i < gameField.getBlocks().length; i++){
							if (e.getComponent() == gameField.getBlocks()[i]) {
								gameField.getBlocks()[i] = null;
							}
						}

						gameField.addBlocks();
					} else {
						final float returnSpeed = 2000f;
						shift = new Shift(e.getComponent(), startPos.x, startPos.y, returnSpeed);
						shift.start();
					}
				}
			}
		});
	}

	Point getStartPos() {
		return startPos;
	}

	void setStartPos(int x, int y) {
		startPos = new Point(x, y);
	}

	private void repaintCircuit() {
		if (gameField.getGrid().canPlace(this, estimatedPos.x, estimatedPos.y, 0)) {
			getCircuit().setLocation(
					estimatedPos.x * (SIZE + 1) + gameField.getGrid().getX(),
					estimatedPos.y * (SIZE + 1) + gameField.getGrid().getY());
			getParent().add(getCircuit());
		} else {
			getParent().remove(getCircuit());
		}
	}
}