package project.game;

import project.components.ExtendedLabel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class RecordsTable extends JLabel {
	
    private static final Font headFont = new Font(GamePanel.fontName, Font.BOLD, 16);
    private static final Font tableFont = new Font(GamePanel.fontName, Font.PLAIN, 14);
    private final int COLL_HEIGHT = 240;
    private final ExtendedLabel tableTitle;
    private final ExtendedLabel numberTitle;
	private final ExtendedLabel nameTitle;
	private final ExtendedLabel scoreTitle;
    private final JLabel numbers = new JLabel();
    private final JLabel names;
    private final JLabel score;
    
    RecordsTable() {
        tableTitle = new ExtendedLabel("Таблица рекордов", Color.LIGHT_GRAY, GamePanel.SHADOW, new Font(GamePanel.fontName, Font.BOLD, 20)) {
            @Override
            public void paintComponent(Graphics g) {
                setLocation((getParent().getWidth() - getWidth()) / 2, 0);
            }
        };
        
        // Номера
        numberTitle = new ExtendedLabel("№", GamePanel.YELLOWISH_GREEN, GamePanel.SHADOW, headFont);
        numberTitle.setLocation(0, tableTitle.getHeight() + 5);
        numbers.setBounds(0, numberTitle.getY() + numberTitle.getHeight(), 35, COLL_HEIGHT);
        
        // Счёт
        score = new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBounds(getParent().getWidth() - getWidth(), numberTitle.getY() + numberTitle.getHeight(), 40, COLL_HEIGHT);
            }
        };
        score.setBounds(100, numberTitle.getY() + numberTitle.getHeight(), 40, COLL_HEIGHT);
        scoreTitle = new ExtendedLabel("Счёт", GamePanel.YELLOWISH_GREEN, GamePanel.SHADOW, headFont) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setLocation(score.getX(), tableTitle.getHeight() + 5);
            }
        };
        
        // Имена
        nameTitle = new ExtendedLabel("Имя", GamePanel.YELLOWISH_GREEN, GamePanel.SHADOW, headFont) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setLocation(numbers.getWidth(), tableTitle.getHeight() + 5);
            }
        };
        nameTitle.setLocation(numbers.getWidth(), tableTitle.getHeight() + 5);
        names = new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBounds(numbers.getWidth(), nameTitle.getY() + nameTitle.getHeight(), getParent().getWidth() - (numbers.getWidth() + score.getWidth()), COLL_HEIGHT);
            }
        };
        names.setBounds(numbers.getWidth(), nameTitle.getY() + nameTitle.getHeight(), 130, COLL_HEIGHT);
        
        // Шрифт, цвет, выравнивание
        numbers.setForeground(Color.WHITE);
        names.setForeground(Color.WHITE);
        score.setForeground(Color.WHITE);
        numbers.setFont(tableFont);
        names.setFont(tableFont);
        score.setFont(tableFont);
        numbers.setVerticalAlignment(TOP);
        names.setVerticalAlignment(TOP);
        score.setVerticalAlignment(TOP);
        
        setSize(tableTitle.getWidth(), tableTitle.getHeight() + numberTitle.getHeight() + COLL_HEIGHT);
        
        add(tableTitle);
        add(numberTitle);
        add(numbers);
        add(scoreTitle);
        add(score);
        add(nameTitle);
        add(names);
    }
    
    void fill(ArrayList<Record> records) {
        StringBuilder s1 = new StringBuilder("<html>");
        StringBuilder s2 = new StringBuilder("<html>");
        StringBuilder s3 = new StringBuilder("<html>");
        for (int i = 0; i < records.size(); i++) {
            s1.append(i + 1).append(".<br>");
            s2.append(records.get(i).name).append("<br>");
            s3.append(records.get(i).score).append("<br>");
        }
        s1.append("</html>");
        s2.append("</html>");
        s3.append("</html>");
        numbers.setText(s1.toString());
        names.setText(s2.toString());
        score.setText(s3.toString());
    }
}
