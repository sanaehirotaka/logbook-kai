package logbook.internal.proxy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CaptureHolder {

    private List<byte[]> req = new ArrayList<>();

    private List<byte[]> res = new ArrayList<>();

    public void putRequest(byte[] data) {
        this.req.add(data);
    }

    public InputStream getRequest() {
        return new ByteArrayInputStream2(this.req);
    }

    public void putResponse(byte[] data) {
        this.res.add(data);
    }

    public InputStream getResponse() {
        return new ByteArrayInputStream2(this.res);
    }

    public void clear() {
        this.req = null;
        this.res = null;
    }
}
