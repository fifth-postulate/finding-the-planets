package nl.fifth.postulate.circuit.pipe;


import nl.fifth.postulate.circuit.ChainingPipe;
import nl.fifth.postulate.circuit.TrappistData;
import org.jtransforms.fft.FloatFFT_1D;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FFTFilter extends ChainingPipe {
    public static FFTFilter fftFilter(int cutoff) {
        return new FFTFilter(cutoff);
    }

    public static final String COLUMN_NAME = "FFTFILTERED";

    private final int cutoff;

    private FFTFilter(int cutoff) {
        super(COLUMN_NAME);
        this.cutoff = cutoff;
    }

    @Override
    public Object calculate(TrappistData trappistData) {
        float[] input = (float[]) trappistData.dataFor(Detrend.COLUMN_NAME);
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
        fft.realInverse(output, true);

        return output;
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
