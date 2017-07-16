package nl.fifth.postulate.circuit.pipe;

import nl.fifth.postulate.circuit.ChainingPipe;
import nl.fifth.postulate.circuit.TrappistData;

public class MADFilter extends ChainingPipe {
    public static MADFilter madFilter(int window) {
        return new MADFilter(window);
    }
    public static final String COLUMN_NAME = "MADFILTER";
    private final int window;

    public MADFilter(int window) {
        super(COLUMN_NAME);
        this.window = window;
    }

    @Override
    public Object calculate(TrappistData trappistData) {
        float[] data = (float[]) trappistData.dataFor(FFTFilter.COLUMN_NAME);

        MADCollector collector = new MADCollector(window, data);

        return collector.result();
    }
}

class MADCollector {
    private final float[] data;
    private final int window;
    private final Median median;

    public MADCollector(int window, float[] data) {
        this.window = window;
        this.data = data;
        this.median = new Median();
    }

    public float[] result() {
        float[] result = new float[data.length];

        for (int row = 0, limit = data.length; row < limit; row++) {
            result[row] = MADaround(row);
        }

        return result;
    }

    private float MADaround(int row) {
        float[] windowedData = windowedDataAroun(row);
        float globalMedian = median.of(windowedData);
        for (int index = 0, limit = windowedData.length; index < limit; index++) {
            windowedData[index] = java.lang.Math.abs(windowedData[index] - globalMedian);
        }
        return median.of(windowedData);
    }

    private float[] windowedDataAroun(int row) {
        float[] data = new float[window];

        for (int index = 0, offset = window/2; index < window; index++) {
            int rowIndex = rowIndex(index, offset);
            data[index] = data[rowIndex];
        }
        return data;
    }

    private int rowIndex(int index, int offset) {
        int rowIndex = index - offset;
        if (rowIndex < 0) {
            return 0;
        }
        if (rowIndex >= data.length) {
            return data.length - 1;
        }
        return rowIndex;
    }

}