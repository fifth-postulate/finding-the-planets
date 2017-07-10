package nl.fifth.postulate.circuit;

interface TrappistData {

    boolean hasColumn(String columnName);

    Object dataFor(String columnName);
}
