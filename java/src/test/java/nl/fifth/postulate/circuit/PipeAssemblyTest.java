package nl.fifth.postulate.circuit;

import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.inOrder;

public class PipeAssemblyTest {
    @Test
    public void shouldCallPipes() {
        TrappistData data = mock(TrappistData.class);
        Pipe mockPipe = mock(Pipe.class);
        PipeAssembly pipeAssembly = PipeAssembly.with(mockPipe).build();

        pipeAssembly.process(data);

        verify(mockPipe).process(data);
    }

    @Test
    public void shouldCallMultiplePipesInARow() {
        TrappistData data = mock(TrappistData.class);
        Pipe firstMockPipe = mock(Pipe.class);
        Pipe secondMockPipe = mock(Pipe.class);
        PipeAssembly pipeAssembly = PipeAssembly
                .with(firstMockPipe)
                .andThen(secondMockPipe)
                .build();
        InOrder inOrder = inOrder(firstMockPipe, secondMockPipe);

        pipeAssembly.process(data);

        inOrder.verify(firstMockPipe).process(data);
        inOrder.verify(secondMockPipe).process(any(TrappistData.class));
    }
}
