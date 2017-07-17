package nl.fifth.postulate.circuit.pipe;

import nl.fifth.postulate.circuit.TrappistData;
import org.junit.Test;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MedianFilterTest {
    @Test
    public void shouldDetermineMAD() {
        TrappistData data = mock(TrappistData.class);
        when(data.dataFor(FFTFilter.COLUMN_NAME)).thenReturn(new float[]{ 1.0f, 2.0f, 3.0f, 4.0f });

        MedianFilter filter = MedianFilter.medianFilter(3);
        float[] result = (float[]) filter.calculate(data);

        assertThat((double)result[0], closeTo(1.0, 0.01));
        assertThat((double)result[1], closeTo(2.0, 0.01));
        assertThat((double)result[2], closeTo(3.0, 0.01));
        assertThat((double)result[3], closeTo(4.0, 0.01));
    }
}
