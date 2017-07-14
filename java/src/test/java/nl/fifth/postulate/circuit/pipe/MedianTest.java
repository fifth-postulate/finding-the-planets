package nl.fifth.postulate.circuit.pipe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class MedianTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> data = new ArrayList<>();
        data.add(new Object[]{new float[]{ 1.0f, 2.0f, 3.0f }, 2.0});
        data.add(new Object[]{new float[]{ 1.0f, 2.5f, 3.0f }, 2.5});
        data.add(new Object[]{new float[]{ 1.0f, 2.0f, 2.0f, 3.0f }, 2.5});
        data.add(new Object[]{new float[]{ 1.0f, 3.0f, 2.0f }, 2.0});
        data.add(new Object[]{new float[]{ 1.0f, 3.0f, 2.0f, 2.0f }, 2.5});
        return data;
    }

    private static final double EPSILON = 0.0001;
    private Median median;

    private final double expectedMedian;
    private final float[] data;

    public MedianTest(float[] data, double expectedMedian) {
        this.data = data;
        this.expectedMedian = expectedMedian;
    }

    @Before
    public void createMedian() {
        median = new Median();
    }

    @Test
    public void shouldCalculateTheCorrectMedian() {
        float actualMedian = median.of(data);

        assertThat((double) actualMedian, is(closeTo(expectedMedian, EPSILON)));
    }
}

