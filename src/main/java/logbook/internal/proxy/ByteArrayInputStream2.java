package logbook.internal.proxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 複数のバイト配列を1つのInputStreamとして扱うByteArrayInputStream
 *
 */
public class ByteArrayInputStream2 extends InputStream {

    private List<byte[]> l;
    private Iterator<byte[]> e;
    private InputStream in;

    public ByteArrayInputStream2(byte[] b) {
        this(Collections.singletonList(b));
    }

    public ByteArrayInputStream2(byte[]... b) {
        this(Arrays.asList(b));
    }

    public ByteArrayInputStream2(List<byte[]> l) {
        this.l = l;
        this.e = l.iterator();
        this.nextStream();
    }

    @Override
    public int available() throws IOException {
        if (this.in == null) {
            return 0; // no way to signal EOF from available()
        }
        return this.in.available();
    }

    @Override
    public void mark(int readlimit) {
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void reset() throws IOException {
        this.e = this.l.iterator();
        this.in = null;
        this.nextStream();
    }

    @Override
    public int read() throws IOException {
        while (this.in != null) {
            int c = this.in.read();
            if (c != -1) {
                return c;
            }
            this.nextStream();
        }
        return -1;
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        if (this.in == null) {
            return -1;
        } else if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        do {
            int n = this.in.read(b, off, len);
            if (n > 0) {
                return n;
            }
            this.nextStream();
        } while (this.in != null);
        return -1;
    }

    @Override
    public void close() throws IOException {
        // NOP
    }

    private void nextStream() {
        if (this.e.hasNext()) {
            this.in = new ByteArrayInputStream(this.e.next());
        } else {
            this.in = null;
        }
    }
}
