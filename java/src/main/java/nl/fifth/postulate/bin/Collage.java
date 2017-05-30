package nl.fifth.postulate.bin;

import nom.tam.fits.BinaryTableHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.floor;

public class Collage {
    public static void main(String[] args) throws FitsException, IOException {
        Fits f = new Fits("../k2-trappist1-unofficial-tpf-long-cadence.fits");
        BinaryTableHDU hdu = (BinaryTableHDU) f.getHDU(1);
        float[][][] rows = (float[][][]) hdu.getColumn("FLUX");


        float maximum = Float.MIN_VALUE;
        for (int row = 0, rowLimit = rows.length; row < rowLimit; row++ ) {
            float[][] inputImage = rows[row];
            for (int y = 0, ylimit = inputImage.length; y < ylimit; y++) {
                for (int x = 0, xlimit = inputImage[0].length; x < xlimit; x++) {
                    float value = inputImage[y][x];

                    if (value > maximum) {
                        maximum = value;
                    }
                }
            }
        }

        // 3599 = (60 - 1) * (60 + 1) = 59 * 61
        int size = 11;
        int htile = 61;
        int vtile = 59;
        int width = htile * size;
        int height = vtile * size;
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = outputImage.getRaster();


        for (int row = 0, rowLimit = rows.length; row < rowLimit; row++ ) {
            float[][] inputImage = rows[row];
            int xoffset = (row % htile) * size;
            int yoffset = (row / htile) * size;
            for (int y = 0, ylimit = inputImage.length; y < ylimit; y++) {
                for (int x = 0, xlimit = inputImage[0].length; x < xlimit; x++) {
                    float value = inputImage[y][x];
                    int index = Double.valueOf(floor(255 * value / maximum)).intValue();
                    raster.setPixel(x + xoffset, y + yoffset, new int[]{index});
                }
            }
        }
        File output = new File("src/main/resources/collage.png");
        ImageIO.write(outputImage, "png", output);

    }
}
