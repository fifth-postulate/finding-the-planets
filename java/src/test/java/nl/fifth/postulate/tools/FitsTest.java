package nl.fifth.postulate.tools;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageData;
import nom.tam.fits.ImageHDU;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class FitsTest {
    @Test
    public void shouldReadFitsFile() throws FitsException, IOException {
        Fits f = new Fits("../k2-trappist1-unofficial-tpf-long-cadence.fits");
        ImageHDU hdu = (ImageHDU) f.getHDU(2);
        ImageData imageData = (ImageData) hdu.getData();
        int[][] image = (int[][]) imageData.getData();

        assertThat(hdu, is(not(nullValue())));
        assertThat(imageData, is(not(nullValue())));
        assertThat(image, is(not(nullValue())));
        assertThat(image.length, is(11));
        assertThat(image[0].length, is(11));
    }
}
