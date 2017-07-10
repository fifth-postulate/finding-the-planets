package nl.fifth.postulate.circuit;

public interface TrappistData {

    boolean hasColumn(String columnName);

    Object dataFor(String columnName);
}
