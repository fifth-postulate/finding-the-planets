package nl.fifth.postulate.bin;

import nom.tam.fits.BinaryTableHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import org.jtransforms.fft.FloatFFT_1D;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AverageWithSmoothing {
    public static void main(String[] args) throws FitsException, IOException {
        String filename = args[0];
        String pathName = args[1];
        float alpha = Float.valueOf(args[2]);

        DataPointCollection dataPointCollection = calculateAverage(filename, alpha);
        dataPointCollection.calculateFFT(100);

        PrintStream output = new PrintStream(new FileOutputStream(pathName));
        int index = 0;
        for (AverageDataPoint dataPoint : dataPointCollection) {
            output.printf("%4d, %s\n", index++, dataPoint);
        }

    }

    private static DataPointCollection calculateAverage(String filename, float alpha) throws FitsException, IOException {
        Fits f = new Fits(filename);
        BinaryTableHDU hdu = (BinaryTableHDU) f.getHDU(1);
        double[] timeRows = (double[]) hdu.getColumn("TIME");
        float[][][] fluxRows = (float[][][]) hdu.getColumn("FLUX");

        DataPointCollection dataPointCollection = new DataPointCollection();
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

            dataPointCollection.add(new AverageDataPoint(timeRows[row], average, smoothed, average - smoothed));
        }

        return dataPointCollection;
    }
}

class AverageDataPoint {
    public final double time;
    public final float average;
    public final float smoothed;
    public final float detrended;
    private float norm;
    private float inverseFFT;

    public AverageDataPoint(double time, float average, float smoothed, float detrended) {
        this.time = time;
        this.average = average;
        this.smoothed = smoothed;
        this.detrended = detrended;
    }

    public String toString() {
        return String.format("%10.6f, %10.6f, %10.6f, %10.6f, %10.6f, %10.6f", time, average, smoothed, detrended, norm, inverseFFT);
    }

    public void setNorm(float norm) {
        this.norm = norm;
    }

    public void setInverseFFT(float inverseFFT) {
        this.inverseFFT = inverseFFT;
    }
}

class DataPointCollection implements Iterable<AverageDataPoint> {
    private static float[] convert(Float[] input) {
        float[] output = new float[input.length];
        for (int index = 0, limit = input.length; index < limit; index++) {
            output[index] = input[index].floatValue();
        }
        return output;

    }
    private final List<AverageDataPoint> dataPoints = new ArrayList<AverageDataPoint>();

    public void add(AverageDataPoint dataPoint){
        dataPoints.add(dataPoint);
    }


    @Override
    public Iterator<AverageDataPoint> iterator() {
        return dataPoints.iterator();
    }

    public void calculateFFT(int cutoff) {
        float[] input = convert(dataPoints.stream()
                .map(dp -> dp.detrended)
                .collect(Collectors.toList())
                .toArray(new Float[]{}));
        int n = input.length;
        final float[] coefficients = Arrays.copyOf(input, 2*n);
        FloatFFT_1D fft = new FloatFFT_1D(n);
        fft.realForwardFull(coefficients);
        float[] norms = calculateNorms(coefficients, n);

        final float[] output = Arrays.copyOf(coefficients, coefficients.length);
        for (int midpoint = coefficients.length/2, index = midpoint - 2*cutoff;
                index < midpoint+2*cutoff;
                index += 2) {
            output[index + 0] = 0.0f;
            output[index + 1] = 0.0f;
        }
        fft.realInverse(output, false);


        int index = 0;
        for (AverageDataPoint dataPoint : this.dataPoints) {
            dataPoint.setNorm(norms[index]);
            dataPoint.setInverseFFT(output[index]);
            index += 1;
        }
    }

    private float[] calculateNorms(float[] coefficients, int n) {
        float[] norms = new float[n];

        for (int index = 0; index < n; index++) {
            float u = coefficients[2*index + 0];
            float v = coefficients[2*index + 1];
            norms[index] = (float) Math.sqrt(u*u + v*v);
        }

        return norms;
    }
}