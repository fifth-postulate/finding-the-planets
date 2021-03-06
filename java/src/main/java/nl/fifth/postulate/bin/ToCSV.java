package nl.fifth.postulate.bin;

import nom.tam.fits.BinaryTableHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;

import java.io.IOException;
import java.io.PrintStream;

public class ToCSV {
    public static void main(String[] args) throws FitsException, IOException {
        String filename = args[0];
        Fits f = new Fits(filename);
        BinaryTableHDU hdu = (BinaryTableHDU) f.getHDU(1);
        double[] time = (double[]) hdu.getColumn("TIME");
        float[][][] rows = (float[][][]) hdu.getColumn("FLUX");

        PrintStream out = new PrintStream("output.csv");
        for (int row = 0, rowLimit = rows.length; row < rowLimit; row++ ) {

            out.printf("%f", time[row]);
            float[][] inputImage = rows[row];
            for (int y = 0, ylimit = inputImage.length; y < ylimit; y++) {
                for (int x = 0, xlimit = inputImage[0].length; x < xlimit; x++) {
                    float value = inputImage[y][x];
                    out.printf(",%f", value);
                }
            }
            out.printf("\n");
        }
        out.flush();

    }
}
