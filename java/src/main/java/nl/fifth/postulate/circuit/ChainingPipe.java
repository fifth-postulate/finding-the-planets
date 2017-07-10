package nl.fifth.postulate.circuit;

public abstract class ChainingPipe implements Pipe {
    private final String columnName;

    public ChainingPipe(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public TrappistData process(TrappistData trappistData) {
        return new ChainedTrappistData(trappistData, columnName, calculate(trappistData));
    }

    public abstract Object calculate(TrappistData trappistData);
}