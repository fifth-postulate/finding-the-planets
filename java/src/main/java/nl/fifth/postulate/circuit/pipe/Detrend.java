package nl.fifth.postulate.circuit.pipe;

import nl.fifth.postulate.circuit.ChainingPipe;
import nl.fifth.postulate.circuit.TrappistData;

public class Detrend extends ChainingPipe {
    public static Detrend detrend(String signal, String trend) {
        return new Detrend(signal, trend);
    }
    public static final String COLUMN_NAME = "DETRENDED";

    private final String signal;
    private final String trend;

    public Detrend(String signal, String trend) {
        super(COLUMN_NAME);
        this.signal = signal;
        this.trend = trend;
    }

    @Override
    public Object calculate(TrappistData trappistData) {
        float[] signals = (float[]) trappistData.dataFor(signal);
        float[] trends = (float[]) trappistData.dataFor(trend);

        float[] detrended = new float[signals.length];
        for (int row = 0, limit = signals.length; row < limit; row++) {
            float detrend = signals[row] - trends[row];

            detrended[row] = detrend;
        }
        return detrended;
    }
}
