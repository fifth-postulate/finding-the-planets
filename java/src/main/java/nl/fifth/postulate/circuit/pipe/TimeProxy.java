package nl.fifth.postulate.circuit.pipe;

import nl.fifth.postulate.circuit.TrappistData;

public class TimeProxy implements TrappistData {
    public static TimeProxy floatTime(TrappistData chain) {
        return new TimeProxy(chain);
    }

    private final TrappistData chain;
    private float[] proxy = null;

    private TimeProxy(TrappistData chain) {
        this.chain = chain;
    }

    @Override
    public boolean hasColumn(String columnName) {
        return "TIME".equals(columnName) || chain.hasColumn(columnName);
    }

    @Override
    public Object dataFor(String columnName) {
        if ("TIME".equals(columnName)) {
            if (proxy == null) {
                double[] original = (double[]) chain.dataFor(columnName);
                proxy = new float[original.length];
                for (int row = 0, limit = original.length; row < limit; row++) {
                    proxy[row] = (float) original[row];
                }
            }
            return proxy;
        } else {
            return chain.dataFor(columnName);
        }
    }
}
