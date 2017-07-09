package nl.fifth.postulate.circuit;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PipeAssemblyTest {
    @Test
    public void shouldCallPipes() {
        Pipe mockPipe = mock(Pipe.class);
        PipeAssembly pipeAssembly = Assembly.with(mockPipe).build();

        pipeAssembly.process();

        verify(mockPipe).process();
    }
}
