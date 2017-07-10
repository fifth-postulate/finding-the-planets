package nl.fifth.postulate.circuit;

public class ChainedTrappistData implements TrappistData {
    private final TrappistData chain;
    private final String key;
    private final Object data;

    public ChainedTrappistData(TrappistData chain, String key, Object data) {
        this.chain = chain;
        this.key = key;
        this.data = data;
    }

    @Override
    public boolean hasColumn(String columnName) {
        return this.key.equals(columnName) || this.chain.hasColumn(columnName);
    }

    @Override
    public Object dataFor(String columnName) {
        if (this.key.equals(columnName)) {
            return this.data;
        } else {
            return this.chain.dataFor(columnName);
        }
    }
}
