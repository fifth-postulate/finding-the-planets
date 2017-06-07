package nl.fifth.postulate.bin;

import nom.tam.fits.BinaryTableHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;

import java.io.*;

public class Average {
    public static void main(String[] args) throws FitsException, IOException {
        String filename = args[0];
        String pathName = args[1];

        PrintStream output = new PrintStream(new FileOutputStream(pathName));


        float[] average = calculateAverage(filename);

        for (int index = 0, limit = average.length; index < limit; index++) {
            output.printf("%4d, %f\n", index, average[index]);
        }

    }

    private static float[] calculateAverage(String filename) throws FitsException, IOException {
        Fits f = new Fits(filename);
        BinaryTableHDU hdu = (BinaryTableHDU) f.getHDU(1);
        float[][][] rows = (float[][][]) hdu.getColumn("FLUX");

        float[] average = new float[rows.length];
        for (int row = 0, rowLimit = rows.length; row < rowLimit; row++ ) {
            float sum = 0.0f;
            int count = 0;
            float[][] inputImage = rows[row];
            for (int y = 0, ylimit = inputImage.length; y < ylimit; y++) {
                for (int x = 0, xlimit = inputImage[0].length; x < xlimit; x++) {
                    sum += inputImage[y][x];
                    count += 1;
                }
            }
            average[row] = sum / count;
        }

        return average;
    }
}
