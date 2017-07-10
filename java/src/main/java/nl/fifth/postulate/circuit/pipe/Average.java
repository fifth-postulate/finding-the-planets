package nl.fifth.postulate.circuit.pipe;

import nl.fifth.postulate.circuit.ChainingPipe;
import nl.fifth.postulate.circuit.TrappistData;

public class Average extends ChainingPipe {
    public static Average average() {
        return new Average();
    }

    public static final String COLUMN_NAME = "AVERAGE";

    private Average() {
        super(COLUMN_NAME);
    }

    @Override
    public Object calculate(TrappistData trappistData) {
        float[][][] fluxRows = (float[][][]) trappistData.dataFor("FLUX");

        float[] averages = new float[fluxRows.length];
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

            averages[row] = average;
        }
        return averages;
    }
}
