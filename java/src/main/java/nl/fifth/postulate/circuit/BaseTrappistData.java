package nl.fifth.postulate.circuit;

import nom.tam.fits.BinaryTableHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BaseTrappistData implements TrappistData {
    public static BaseTrappistData from(String filename){
        try {
            Fits f = new Fits(filename);
            BinaryTableHDU hdu = (BinaryTableHDU) f.getHDU(1);
            return new BaseTrappistData(hdu);
        } catch (FitsException | IOException e) {
            throw new BaseTrappistDataException(e);
        }

    }

    private static final Set<String> columnNames = new HashSet<>();

    static {
        columnNames.add("TIME");
        columnNames.add("FLUX");
    }

    private final BinaryTableHDU table;

    private BaseTrappistData(BinaryTableHDU table) {
        this.table = table;
    }

    @Override
    public boolean hasColumn(String columnName) {
        return columnNames.contains(columnName);
    }

    @Override
    public Object dataFor(String columnName) {
        try {
            return table.getColumn(columnName);
        } catch (FitsException e) {
            throw new BaseTrappistDataException(e);
        }
    }
}

class BaseTrappistDataException extends RuntimeException {
    public BaseTrappistDataException(Exception cause) {
        super(cause);
    }
}