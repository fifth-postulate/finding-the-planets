package nl.fifth.postulate.circuit.pipe;

import nl.fifth.postulate.circuit.ChainingPipe;
import nl.fifth.postulate.circuit.TrappistData;

public class Smooth extends ChainingPipe {
    public static Smooth smooth(float alpha) {
        return new Smooth(alpha);
    }

    public static final String COLUMN_NAME = "SMOOTH";

    private final float alpha;

    public Smooth(float alpha) {
        super(COLUMN_NAME);
        this.alpha = alpha;
    }

    @Override
    public Object calculate(TrappistData trappistData) {
        float[] averages = (float[]) trappistData.dataFor(Average.COLUMN_NAME);

        float[] smootheds = new float[averages.length];
        float smoothed = 0f;
        for (int row = 0, rowLimit = averages.length; row < rowLimit; row++ ) {
            float average = averages[row];

            if (row != 0) {
                smoothed = alpha * average + (1 - alpha) * smoothed;
            } else {
                smoothed = average;
            }
            smootheds[row] = smoothed;
        }
        return smootheds;
    }
}
