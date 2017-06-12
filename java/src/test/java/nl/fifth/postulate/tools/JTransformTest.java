package nl.fifth.postulate.tools;

import org.jtransforms.fft.DoubleFFT_1D;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class JTransformTest {
    private static final double EPSILON = 0.01;

    @Test
    public void shouldDoAFFTTransform() {
        final double[] input = new double[]{ 1.0f, 1.5f, 0.5f, 2.0f, 2.0f, 0.5f, 1.5f, 1.0f };
        final double[] output = Arrays.copyOf(input, input.length);
        DoubleFFT_1D fft = new DoubleFFT_1D(output.length);

        fft.realForward(output);
 
        assertThat(output[0], is(closeTo(10.00f, EPSILON)));
        assertThat(output[1], is(closeTo( 0.00f, EPSILON)));
        assertThat(output[2], is(closeTo(-1.00f, EPSILON)));
        assertThat(output[3], is(closeTo(-0.41f, EPSILON)));
        assertThat(output[4], is(closeTo( 1.00f, EPSILON)));
        assertThat(output[5], is(closeTo( 1.00f, EPSILON)));
        assertThat(output[6], is(closeTo(-1.00f, EPSILON)));
        assertThat(output[7], is(closeTo(-2.41f, EPSILON)));
    }
}
