package project.game;

import project.generation.BoolMap;

import javax.swing.*;
import java.awt.*;

public class Block extends JComponent {
    
    static final byte SIZE = 24;
    private final Image image;
    private final int value;
    private final Circuit circuit;
    private BoolMap structure;
    private int width, height;
    
    Block(int quantity, int maxHeight, int maxWidth, Image image) {
        value = quantity;
        this.image = image;
        structure = new BoolMap(maxHeight, maxWidth, quantity).shiftUp().shiftLeft();
        setOpaque(false);
        circuit = new Circuit();
        update();
    }
    
    BoolMap getStructure() {
        return structure;
    }
    
    int getValue() {
        return value;
    }
    
    Circuit getCircuit() {
        return circuit;
    }
    
    boolean exists(int i, int j) {
        return structure.get(i, j);
    }
    
    int getSegmentsX() {
        return width;
    }
    
    int getSegmentsY() {
        return height;
    }
    
    Image getImage() {
        return image;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        for (int i = 0; i < structure.getHeight(); i++) {
            for (int j = 0; j < structure.getWidth(); j++) {
                if (structure.get(i, j))
                    g.drawImage(image, j * SIZE + j, i * SIZE + i, null);
            }
        }
        g.setColor(Color.DARK_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (structure.get(y, x)) {
                    if (y != 0 && structure.get(y - 1, x))
                        g.drawLine(x * (SIZE + 1), y * (SIZE + 1) - 1, (x + 1) * SIZE + x - 1, y * (SIZE + 1) - 1);
                    
                    if (x != 0 && structure.get(y, x - 1))
                        g.drawLine(x * (SIZE + 1) - 1, y * (SIZE + 1), x * (SIZE + 1) - 1, (y + 1) * SIZE - 1 + y);
                    
                    if (x > 0 && y > 0 && structure.get(y - 1, x - 1) && structure.get(y - 1, x) && structure.get(y, x - 1))
                        g.drawRect((SIZE + 1) * x - 1, (SIZE + 1) * y - 1, 0, 0);
                }
            }
        }
    }
    
    void rotate() {
        structure = structure.rotate().shiftUp().shiftLeft();
        update();
        repaint();
    }
    
    private void update() {
        width = height = 0;
        for (int i = 0; i < structure.getHeight(); i++) {
            for (int j = 0; j < structure.getWidth(); j++) {
                if (structure.get(i, j)) {
                    if (j + 1 > width)
                        width = j + 1;
                    if (i + 1 > height)
                        height = i + 1;
                }
            }
        }
        setSize(width * SIZE + width, height * SIZE + height);
        setSize(width * SIZE + width, height * SIZE + height);
        circuit.setSize(getWidth() + 2, getHeight() + 2);
    }
    
    /* Контур блока */
    public class Circuit extends JPanel {
        private final Color color = Color.LIGHT_GRAY;
        
        private Circuit() {
            setOpaque(false);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (structure.get(y, x)) {
                        if (y == 0 || !structure.get(y - 1, x))
                            g.drawLine(x * (SIZE + 1), y * (SIZE + 1), (x + 1) * SIZE + x, y * (SIZE + 1));
                        
                        if (x == 0 || !structure.get(y, x - 1))
                            g.drawLine(x * (SIZE + 1), y * (SIZE + 1), x * (SIZE + 1), (y + 1) * SIZE + y);
                        
                        if (y == structure.getHeight() - 1 || !structure.get(y + 1, x))
                            g.drawLine((x + 1) * SIZE + x + 1, (y + 1) * SIZE + y + 1, x * (SIZE + 1), (y + 1) * SIZE + y + 1);
                        
                        if (x == structure.getWidth() - 1 || !structure.get(y, x + 1))
                            g.drawLine((x + 1) * SIZE + x + 1, (y + 1) * SIZE + y + 1, (x + 1) * SIZE + x + 1, y * (SIZE + 1));
                    }
                }
            }
        }
    }
}