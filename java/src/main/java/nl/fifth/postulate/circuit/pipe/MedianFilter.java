package nl.fifth.postulate.circuit.pipe;

import nl.fifth.postulate.circuit.ChainingPipe;
import nl.fifth.postulate.circuit.TrappistData;

public class MedianFilter extends ChainingPipe {
    public static MedianFilter medianFilter(int window) {
        return new MedianFilter(window);
    }
    public static final String COLUMN_NAME = "MEDIANFILTER";
    private final int window;

    public MedianFilter(int window) {
        super(COLUMN_NAME);
        this.window = window;
    }

    @Override
    public Object calculate(TrappistData trappistData) {
        float[] data = (float[]) trappistData.dataFor(FFTFilter.COLUMN_NAME);

        MedianCollector collector = new MedianCollector(window, data);

        return collector.result();
    }
}

class MedianCollector {
    private final float[] data;
    private final int window;
    private final Median median;

    public MedianCollector(int window, float[] data) {
        this.window = window;
        this.data = data;
        this.median = new Median();
    }

    public float[] result() {
        float[] result = new float[data.length];

        for (int row = 0, limit = data.length; row < limit; row++) {
            result[row] = MedianAround(row);
        }

        return result;
    }

    private float MedianAround(int row) {
        float[] windowedData = windowedDataAround(row);
        return median.of(windowedData);
    }

    private float[] windowedDataAround(int row) {
        float[] data = new float[window];

        for (int index = 0, offset = window/2; index < window; index++) {
            int targetIndex = row + index - offset;
            int rowIndex = clampedIndex(targetIndex);
            data[index] = this.data[rowIndex];
        }
        return data;
    }

    private int clampedIndex(int index) {
        int clampIndex = index;
        if (clampIndex < 0) {
            return 0;
        }
        if (clampIndex >= data.length) {
            return data.length - 1;
        }
        return clampIndex;
    }

}