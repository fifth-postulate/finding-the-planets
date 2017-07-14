package nl.fifth.postulate.circuit.pipe;

import java.util.Arrays;


public class Median {
    public float of(float[] input) {
        float[] data = Arrays.copyOf(input, input.length);
        Arrays.sort(data);
        if (input.length % 2 == 1) {
            int medianIndex = data.length / 2;
            return data[medianIndex];
        } else {
            int leftMedianIndex = data.length/2, rightMedianIndex = leftMedianIndex + 1;
            return (data[leftMedianIndex] + data[rightMedianIndex])/2;
        }
    }
}
