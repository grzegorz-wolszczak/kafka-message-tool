import application.logging.CyclicStringBuffer;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CyclicStringBufferSpecifications {
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldNotCreateBufferWithZeroSize() {
        // WHEN/THEN

        CyclicStringBuffer b = createBuffer(0);

    }

    @Test
    public void shouldCreateBufferWithSizeOne() {
        // GIVEN
        final int bufferSize = 1;
        CyclicStringBuffer b = createBuffer(bufferSize);

        // WHEN
        b.appendText("abc");

        // THEN
        assertThat(b.getContent()).isEqualTo("abc");
    }

    @Test
    public void shouldOverwriteBufferWithSizeOne() {
        // GIVEN
        final int bufferSize = 1;
        CyclicStringBuffer b = createBuffer(bufferSize);
        b.appendText("abc");

        // WHEN
        b.appendText("bcd");

        // THEN
        assertThat(b.getContent()).isEqualTo("bcd");
    }

    private CyclicStringBuffer createBuffer(int bufferSize) {
        return new CyclicStringBuffer(bufferSize);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldNotResizeBufferToValueLessThanZero() {
        // GIVEN
        CyclicStringBuffer b = createBuffer(1);
        // WHEN
        b.resize(0);
        // THEN
    }

    @Test
    public void shouldResizeToBiggerSize() {
        final int bufferSize = 1;
        CyclicStringBuffer b = createBuffer(bufferSize);
        b.appendText("abc");

        // WHEN
        b.resize(2);
        b.appendText("bcd");

        // THEN
        assertThat(b.getContent()).isEqualTo("abcbcd");
    }

    @Test
    public void shouldResizeToSmallerSize() {
        final int bufferSize = 3;
        CyclicStringBuffer b = createBuffer(bufferSize);
        b.appendText("abc");
        b.appendText("bcd");
        b.appendText("xyz");

        // WHEN
        b.resize(2);

        // THEN
        assertThat(b.getContent()).isEqualTo("bcdxyz");
    }
}
