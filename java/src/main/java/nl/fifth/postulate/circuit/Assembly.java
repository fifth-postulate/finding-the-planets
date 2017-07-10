package nl.fifth.postulate.circuit;

import java.util.ArrayList;
import java.util.List;

public class Assembly {
    private List<Pipe> pipes = new ArrayList<>();

    Assembly(Pipe pipe) {
        pipes.add(pipe);
    }

    public Assembly andThen(Pipe pipe) {
        pipes.add(pipe);
        return this;
    }

    public PipeAssembly build() {
        return new PipeAssembly(pipes);
    }
}
