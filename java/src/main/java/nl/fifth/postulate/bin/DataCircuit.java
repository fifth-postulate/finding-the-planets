package nl.fifth.postulate.bin;

import nl.fifth.postulate.circuit.BaseTrappistData;
import nl.fifth.postulate.circuit.PipeAssembly;
import nl.fifth.postulate.circuit.TrappistData;
import nl.fifth.postulate.circuit.pipe.Average;
import nl.fifth.postulate.circuit.pipe.Detrend;
import nl.fifth.postulate.circuit.pipe.FFTFilter;
import nl.fifth.postulate.circuit.pipe.Smooth;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static nl.fifth.postulate.circuit.pipe.Average.average;
import static nl.fifth.postulate.circuit.pipe.Detrend.detrend;
import static nl.fifth.postulate.circuit.pipe.FFTFilter.fftFilter;
import static nl.fifth.postulate.circuit.pipe.Smooth.smooth;
import static nl.fifth.postulate.circuit.pipe.TimeProxy.floatTime;

public class DataCircuit {
    public static void main(String[] args) throws FileNotFoundException {
        String filename = args[0];
        String pathName = args[1];

        TrappistData result = process(floatTime(BaseTrappistData.from(filename)));


        PrintStream output = new PrintStream(new FileOutputStream(pathName));
        Formatter.format(output, result, "%4d, %10.6f, %10.6f, %10.6f, %10.6f, %10.6f\n",
                "TIME",
                Average.COLUMN_NAME,
                Smooth.COLUMN_NAME,
                Detrend.COLUMN_NAME,
                FFTFilter.COLUMN_NAME
        );
    }

    private static TrappistData process(TrappistData baseData) {
        PipeAssembly assembly = PipeAssembly
                .with(average())
                .andThen(smooth(0.6f))
                .andThen(detrend(Average.COLUMN_NAME, Smooth.COLUMN_NAME))
                .andThen(fftFilter(1500))
                .build();

        return assembly.process(baseData);
    }

}

class Formatter {
    public static void format(PrintStream stream, TrappistData data, String template, String... columnNames) {
        Map<String, float[]> dataPoints = new HashMap<>();
        for (String columnName : columnNames) {
            dataPoints.put(columnName, (float[]) data.dataFor(columnName));
        }

        for (int row = 0, limit = dataPoints.get(columnNames[1]).length; row < limit; row++) {
            Object[] dataPoint = new Object[1 + columnNames.length];
            dataPoint[0] = row;
            for (int index = 0; index < columnNames.length; index++) {
                String columnName = columnNames[index];
                float value = (float) dataPoints.get(columnName)[row];
                dataPoint[1 + index] = value;
            }
            stream.format(template, dataPoint);
        }
    }

}