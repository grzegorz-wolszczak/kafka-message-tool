package application.logging;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.Iterator;

public class CyclicStringBuffer {
    public static final int DEFAULT_BUFFER_SIZE = 10000;
    private CircularFifoQueue<String> buffer;

    public CyclicStringBuffer(){
        this(DEFAULT_BUFFER_SIZE);
    }

    public CyclicStringBuffer(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be grater than zero but is " + size);
        }
        buffer = new CircularFifoQueue<>(size);
    }

    public void appendText(String text) {
        buffer.add(text);
    }

    public String getContent() {
        final Iterator<String> iterator = buffer.iterator();
        final StringBuilder b = new StringBuilder();

        while (iterator.hasNext()) {
            b.append(iterator.next());
        }
        return b.toString();
    }

    public void resize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be grater than zero but is " + size);
        }
        CircularFifoQueue<String> old = buffer;
        buffer = new CircularFifoQueue<>(size);
        copyElementsFrom(old);
    }

    private void copyElementsFrom(CircularFifoQueue<String> old) {
        final Iterator<String> iterator = old.iterator();
        while (iterator.hasNext()) {
            buffer.add(iterator.next());
        }
    }

    public void clear() {
        buffer.clear();
    }
}
