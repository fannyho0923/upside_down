package game.controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageController {
    private static ImageController imageController;
    private Map<String, Image> imageMap;

    private ImageController() {
        imageMap = new HashMap<>();
    }

    public static ImageController getInstance() {
        if (imageController == null) {
            imageController = new ImageController();
        }
        return imageController;
    }

    public void clear() {
        imageMap.clear();
    }

    public Image tryGet(final String path) {
        if (imageMap.containsKey(path)) {
            return imageMap.get(path);
        }
        return add(path);
    }

    private Image add(final String path) {
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource(path));
            imageMap.put(path, img);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}
