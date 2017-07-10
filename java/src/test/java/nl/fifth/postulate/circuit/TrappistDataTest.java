package nl.fifth.postulate.circuit;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TrappistDataTest {
    public static final String ANY_COLUMN = "ANY KEY";
    public static final Object ANY_DATA = new Object();

    private TrappistData base;

    @Before
    public void createBaseTrappistData() {
        this.base = new TestTrappistData();
    }

    @Test
    public void chainedTrappistDataShouldDelegateUnKnownToChain() {
        TrappistData data = new ChainedTrappistData(base, ANY_COLUMN, ANY_DATA);

        assertTrue(data.hasColumn(ANY_COLUMN));
        assertThat(data.dataFor(ANY_COLUMN), is(ANY_DATA));

        String unknownColumnName = "UNKNOWN";
        assertTrue(data.hasColumn(unknownColumnName));
        assertThat(data.dataFor(unknownColumnName), is(TestTrappistData.DATA));
    }
}

class TestTrappistData implements TrappistData {
    public static final Object DATA = new Object();

    @Override
    public boolean hasColumn(String columnName) {
        return true;
    }

    @Override
    public Object dataFor(String columnName) {
        return DATA;
    }
}