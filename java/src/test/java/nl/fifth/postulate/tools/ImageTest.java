package nl.fifth.postulate.tools;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ImageTest {
    @Test
    public void createAnPngImage() throws IOException {
        int m = 5;
        int n = 5;
        BufferedImage image = new BufferedImage(m, n, BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster raster = image.getRaster();

        for (int x = 0; x < m; x++) {
            for (int y = 0; y < m; y++) {
                int index = (x + y) % 2 == 0 ? 1: 0;
                raster.setPixel(x, y, new int[]{ index });
            }
        }

        File output = new File("src/test/resources/png-test.png");
        ImageIO.write(image, "png", output);
    }
}
