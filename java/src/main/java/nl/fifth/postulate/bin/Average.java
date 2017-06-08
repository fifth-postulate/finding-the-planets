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


        AverageDataPair[] average = calculateAverage(filename);

        for (int index = 0, limit = average.length; index < limit; index++) {
            output.printf("%4d, %s\n", index, average[index]);
        }

    }

    private static AverageDataPair[] calculateAverage(String filename) throws FitsException, IOException {
        Fits f = new Fits(filename);
        BinaryTableHDU hdu = (BinaryTableHDU) f.getHDU(1);
        double[] timeRows = (double[]) hdu.getColumn("TIME");
        float[][][] fluxRows = (float[][][]) hdu.getColumn("FLUX");

        AverageDataPair[] averageDataPair = new AverageDataPair[fluxRows.length];
        for (int row = 0, rowLimit = fluxRows.length; row < rowLimit; row++ ) {
            float sum = 0.0f;
            int count = 0;
            float[][] inputImage = fluxRows[row];
            for (int y = 0, ylimit = inputImage.length; y < ylimit; y++) {
                for (int x = 0, xlimit = inputImage[0].length; x < xlimit; x++) {
                    sum += inputImage[y][x];
                    count += 1;
                }
            }
            float average = sum / count;

            averageDataPair[row] = new AverageDataPair(timeRows[row], average);
        }

        return averageDataPair;
    }
}

class AverageDataPair {
    private final double time;
    private final float average;

    public AverageDataPair(double time, float average) {
        this.time = time;
        this.average = average;
    }

    public String toString() {
        return String.format("%10.6f, %10.6f", time, average);
    }
}