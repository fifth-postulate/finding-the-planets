package nl.fifth.postulate.circuit;

import java.util.ArrayList;
import java.util.List;

public class PipeAssembly implements Pipe {
    private final List<Pipe> pipes = new ArrayList<>();

    PipeAssembly(List<Pipe> pipes) {
        this.pipes.addAll(pipes);
    }

    @Override
    public Result process() {
        for (Pipe pipe : pipes) {
            pipe.process();
        }
        return null;
    }
}

class Assembly {
    public static Assembly with(Pipe pipe) {
        return new Assembly(pipe);
    }

    private List<Pipe> pipes = new ArrayList<>();

    private Assembly(Pipe pipe) {
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
