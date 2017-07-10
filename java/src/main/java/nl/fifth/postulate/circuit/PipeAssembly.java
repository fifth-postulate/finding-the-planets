package nl.fifth.postulate.circuit;

import java.util.ArrayList;
import java.util.List;

public class PipeAssembly implements Pipe {
    public static Assembly with(Pipe pipe) {
        return new Assembly(pipe);
    }

    private final List<Pipe> pipes = new ArrayList<>();

    PipeAssembly(List<Pipe> pipes) {
        this.pipes.addAll(pipes);
    }

    @Override
    public TrappistData process(TrappistData trappistData) {
        TrappistData current = trappistData;
        for (Pipe pipe : pipes) {
            current = pipe.process(current);
        }
        return current;
    }
}

class Assembly {
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
