package nl.fifth.postulate.bin;

import nom.tam.fits.BinaryTableHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;

import java.io.*;

public class AverageWithSmoothing {
    public static void main(String[] args) throws FitsException, IOException {
        String filename = args[0];
        String pathName = args[1];
        float alpha = Float.valueOf(args[2]);

        AverageDataPoint[] average = calculateAverage(filename, alpha);

        PrintStream output = new PrintStream(new FileOutputStream(pathName));
        for (int index = 0, limit = average.length; index < limit; index++) {
            output.printf("%4d, %s\n", index, average[index]);
        }

    }

    private static AverageDataPoint[] calculateAverage(String filename, float alpha) throws FitsException, IOException {
        Fits f = new Fits(filename);
        BinaryTableHDU hdu = (BinaryTableHDU) f.getHDU(1);
        double[] timeRows = (double[]) hdu.getColumn("TIME");
        float[][][] fluxRows = (float[][][]) hdu.getColumn("FLUX");

        AverageDataPoint[] averageDataPoint = new AverageDataPoint[fluxRows.length];
        float smoothed = 0.0f;
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

            if (row != 0) {
                smoothed = alpha * average + (1 - alpha) * smoothed;
            } else {
                smoothed = average;
            }

            averageDataPoint[row] = new AverageDataPoint(timeRows[row], average, smoothed, average - smoothed);
        }

        return averageDataPoint;
    }
}

class AverageDataPoint {
    private final double time;
    private final float average;
    private final float smoothed;
    private final float detrended;

    public AverageDataPoint(double time, float average, float smoothed, float detrended) {
        this.time = time;
        this.average = average;
        this.smoothed = smoothed;
        this.detrended = detrended;
    }

    public String toString() {
        return String.format("%10.6f, %10.6f, %10.6f, %10.6f", time, average, smoothed, detrended);
    }
}