package nl.fifth.postulate.circuit;

import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.inOrder;

public class PipeAssemblyTest {
    @Test
    public void shouldCallPipes() {
        Pipe mockPipe = mock(Pipe.class);
        PipeAssembly pipeAssembly = Assembly.with(mockPipe).build();

        pipeAssembly.process();

        verify(mockPipe).process();
    }

    @Test
    public void shouldCallMultiplePipesInARow() {
        Pipe firstMockPipe = mock(Pipe.class);
        Pipe secondMockPipe = mock(Pipe.class);
        PipeAssembly pipeAssembly = Assembly
                .with(firstMockPipe)
                .andThen(secondMockPipe)
                .build();
        InOrder inOrder = inOrder(firstMockPipe, secondMockPipe);

        pipeAssembly.process();

        inOrder.verify(firstMockPipe).process();
        inOrder.verify(secondMockPipe).process();
    }
}
