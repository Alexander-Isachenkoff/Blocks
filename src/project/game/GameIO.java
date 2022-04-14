package project.game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GameIO {
	
	private static final String IMAGES_ZIP_FILE = "/images";
	private static final String TEMP_IMAGES_DIR = "temp_images";
	
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
	static Image[] readImages() throws IOException {
		File tempImgDir = new File(TEMP_IMAGES_DIR);
		try {
			unzip(GameIO.class.getResourceAsStream(IMAGES_ZIP_FILE), Paths.get(TEMP_IMAGES_DIR));
			File[] files = tempImgDir.listFiles();
			if (files == null) {
				throw new IOException("files is null");
			}
			Image[] images = new Image[files.length];
			for (int i = 0; i < files.length; i++) {
				BufferedImage image = ImageIO.read(files[i]);
				images[i] = image;
				if (image.getWidth(null) != Block.SIZE || image.getHeight(null) != Block.SIZE) {
					throw new IOException("Некорректный размер изображения");
				}
			}
			return images;
		} finally {
			deleteDir(tempImgDir);
		}
	}
	
	// TODO 14.04.2022: заменить на import org.apache.commons.io.FileUtils;
	// FileUtils.deleteDirectory(new File(destination));
	static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDir(f);
			}
		}
		file.delete();
	}
	
	public static void unzip(InputStream is, Path targetDir) throws IOException {
		targetDir = targetDir.toAbsolutePath();
		try (ZipInputStream zipIn = new ZipInputStream(is)) {
			for (ZipEntry ze; (ze = zipIn.getNextEntry()) != null; ) {
				Path resolvedPath = targetDir.resolve(ze.getName()).normalize();
				if (!resolvedPath.startsWith(targetDir)) {
					throw new RuntimeException("Entry with an illegal path: " + ze.getName());
				}
				if (ze.isDirectory()) {
					Files.createDirectories(resolvedPath);
				} else {
					Files.createDirectories(resolvedPath.getParent());
					Files.copy(zipIn, resolvedPath);
				}
			}
		}
	}
}