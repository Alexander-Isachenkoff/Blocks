package project.components;

import javax.swing.*;
import java.awt.*;

public class ExtendedLabel extends JPanel {
	
    private final JLabel point = new JLabel();
    private final JLabel shadow = new JLabel();
    private final byte SHADOW_OFFSET = 2;
    
    public ExtendedLabel(String text, Color mainColor, Color shadowColor, Font font) {
        setOpaque(false);
        setLayout(null);
        point.setFont(font);
        shadow.setFont(font);
        point.setForeground(mainColor);
        shadow.setForeground(shadowColor);
        setText(text);
        point.setHorizontalAlignment(SwingConstants.CENTER);
        shadow.setHorizontalAlignment(SwingConstants.CENTER);
        //setBorder(BorderFactory.createLineBorder(Color.GREEN));
        point.setLocation(-SHADOW_OFFSET / 2, -SHADOW_OFFSET / 2);
        shadow.setLocation(SHADOW_OFFSET / 2, SHADOW_OFFSET / 2);
        add(point);
        add(shadow);
    }
    
    public void setText(String text) {
        point.setText(text);
        shadow.setText(text);
        point.setSize(point.getFontMetrics(point.getFont()).stringWidth(point.getText()) + SHADOW_OFFSET, point.getFontMetrics(point.getFont()).getHeight());
        shadow.setSize(point.getSize());
        setSize(point.getSize());
    }
    
    @Override
    public void setSize(int width, int height) {
        point.setSize(width, height);
        shadow.setSize(width, height);
        super.setSize(width, height);
    }
    
    public void setColor(Color color) {
        point.setForeground(color);
    }
}
