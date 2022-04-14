package project;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class MainTest {
    
    @Disabled
    @Test
    void packImages() throws IOException {
        List<String> imgFilePaths = Arrays.asList(
                "images/blocks/blue.png",
                "images/blocks/red.png",
                "images/blocks/green.png",
                "images/blocks/yellow.png"
        );
        File f = new File("res/images");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
        for (String imgFilePath : imgFilePaths) {
            String name = new File(imgFilePath).getName();
            ZipEntry e = new ZipEntry(name);
            out.putNextEntry(e);
            byte[] bytes = Files.readAllBytes(Paths.get(imgFilePath));
            out.write(bytes, 0, bytes.length);
            out.closeEntry();
        }
        out.close();
    }
    
}