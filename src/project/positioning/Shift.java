package project.positioning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Shift
{
	private static final float NANO = 1E-9F;
	private float vX, vY, x, y;
	private int finalX, finalY;
	private long lastFrameTime;
	private Point startPos;
	private Component obj;
	private Timer timer;
	private ActionListener listener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (obj.getX() == finalX && obj.getY() == finalY) {
				((Timer) e.getSource()).stop();
			}

			long currentTime = System.nanoTime();
			double dt = (currentTime - lastFrameTime) * NANO;

			if (startPos.x != finalX) {
				x += vX * dt;
				if (x > finalX && vX > 0 || x < finalX && vX < 0) {
					x = finalX;
				}
			}

			if (startPos.y != finalY) {
				y += vY * dt;
				if (y > finalY && vY > 0 || y < finalY && vY < 0) {
					y = finalY;
				}
			}

			if (obj.getX() != (int) x || obj.getY() != (int) y) {
				obj.setLocation((int) x, (int) y);
			}

			lastFrameTime = currentTime;
		}
	};

	public Shift(Component component, int x, int y, float vX, float vY) {
		finalX = x;
		finalY = y;
		this.vX = vX;
		this.vY = vY;
		obj = component;
		timer = new Timer(10, listener);
	}

	public Shift(Component component, int x, int y, float v) {
		this(component, x, y, 0, 0);
		int dx = Math.abs(x - component.getX());
		int dy = Math.abs(y - component.getY());
		double z = Math.sqrt(dx * dx + dy * dy);
		vX = (float) (dx * v / z);
		vY = (float) (dy * v / z);
	}

	public void start() {
		startPos = new Point(obj.getLocation());
		if ((x = startPos.x) > finalX) {
			vX = -vX;
		}
		if ((y = startPos.y) > finalY) {
			vY = -vY;
		}
		lastFrameTime = System.nanoTime();
		timer.start();
	}

	public boolean isRunning() {
		return timer.isRunning();
	}

	public void stop() {
		timer.stop();
	}
}
