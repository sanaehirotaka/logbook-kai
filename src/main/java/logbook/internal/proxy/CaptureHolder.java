package logbook.internal.proxy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CaptureHolder {

    private List<byte[]> req = new ArrayList<>();

    private List<byte[]> res = new ArrayList<>();

    public void putRequest(byte[] data) {
        if (this.req.size() > 0 && Arrays.equals(this.req.get(this.req.size()-1), data)) {
            // if the data is the same as the last one in this.req array, no need to add this to req as it is duplicated
            return;
        }
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
