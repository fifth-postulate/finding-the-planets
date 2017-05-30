package nl.fifth.postulate.bin;

import nom.tam.fits.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.floor;

public class FirstImage {
    public static void main(String[] args) throws FitsException, IOException {
        Fits f = new Fits("../k2-trappist1-unofficial-tpf-long-cadence.fits");
        BinaryTableHDU hdu = (BinaryTableHDU) f.getHDU(1);
        hdu.info(System.out);
        float[][][] rows = (float[][][]) hdu.getColumn("FLUX");

        float[][] inputImage = rows[0];
        BufferedImage outputImage = new BufferedImage(inputImage[0].length, inputImage.length, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = outputImage.getRaster();

        float maximum = Float.MIN_VALUE;
        for (int y = 0, ylimit = inputImage.length; y < ylimit; y++) {
            for (int x = 0, xlimit = inputImage[0].length; x < xlimit; x++) {
                float value = inputImage[y][x];

                if (value > maximum) {
                    maximum = value;
                }
            }
        }

        for (int y = 0, ylimit = inputImage.length; y < ylimit; y++) {
            for (int x = 0, xlimit = inputImage[0].length; x < xlimit; x++) {
                float value = inputImage[y][x];
                int index = Double.valueOf(floor(255 * value/maximum)).intValue();
                System.out.format("(%2d, %2d): %15.10f\n", x, y, value);
                raster.setPixel(x, y, new int[]{ index });
            }
        }

        File output = new File("src/main/resources/first-image.png");
        ImageIO.write(outputImage, "png", output);

    }
}
