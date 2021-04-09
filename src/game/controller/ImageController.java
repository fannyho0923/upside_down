package game.controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ImageController {
    private static ImageController imageController;
    private final ArrayList<KeyPair> keyPairs;

    private ImageController() {
        this.keyPairs = new ArrayList<>();
    }

    public static ImageController getInstance() {
        if (imageController == null) {
            imageController = new ImageController();
        }
        return imageController;
    }

    public void clear() {
        this.keyPairs.clear();
    }

    public Image tryGet(final String path) {
        for (int i = 0; i < this.keyPairs.size(); i++) {
            if (this.keyPairs.get(i).path.equals(path)) {
                return this.keyPairs.get(i).img;
            }
        }
        return add(path);
    }

    private Image add(final String path) {
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource(path));
            this.keyPairs.add(new KeyPair(path, img));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    private static class KeyPair {
        private final String path;
        private final Image img;

        public KeyPair(final String path, final Image img) {
            this.path = path;
            this.img = img;
        }
    }
}
