package project.game;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GameIO
{
	// Чтение рекордов
	static ArrayList<Record> readRecords(File file) throws Exception {
		FileInputStream stream = new FileInputStream(file);
		ObjectInputStream objectReader = new ObjectInputStream(stream);
		return (ArrayList<Record>) objectReader.readObject();
	}

	// Запись рекордов
	static void writeRecords(ArrayList<Record> records, File file) throws IOException {
		FileOutputStream stream = new FileOutputStream(file);
		ObjectOutputStream objectWriter = new ObjectOutputStream(stream);
		objectWriter.writeObject(records);
	}

	// Чтение изображений
	static Image[] readImages() throws IOException, ClassNotFoundException {
		ObjectInputStream objectReader = new ObjectInputStream(GameIO.class.getResourceAsStream("images.bin"));
		ImageIcon[] icons = (ImageIcon[]) objectReader.readObject();
		Image[] images = new Image[icons.length];
		for (int i = 0; i < images.length; i++) {
			images[i] = icons[i].getImage();
			if (images[i].getWidth(null) != Block.SIZE || images[i].getHeight(null) != Block.SIZE) {
				throw new IOException("Некорректный размер изображения");
			}
		}
		return images;
	}

	// Запись изображений
	public static void writeImages(ImageIcon[] images, File file) throws IOException {
		FileOutputStream stream = new FileOutputStream(file);
		ObjectOutputStream objectWriter = new ObjectOutputStream(stream);
		objectWriter.writeObject(images);
	}
}