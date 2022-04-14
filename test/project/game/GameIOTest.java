package project.game;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameIOTest {
    
    @Test
    void readImages() throws IOException {
        Image[] images = GameIO.readImages();
        assertEquals(4, images.length);
    }
}