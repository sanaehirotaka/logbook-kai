package logbook.proxy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

/**
 * internal package private class.
 *
 * @see ResponseMetaData
 */
class ResponseMetaDataWrapper implements ResponseMetaData {

    private static final long serialVersionUID = -5949586529255459874L;

    private int status;

    private String contentType;

    private Map<String, Collection<String>> headers;

    private transient Optional<InputStream> responseBody;

    @Override
    public int getStatus() {
        return this.status;
    }

    void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public Map<String, Collection<String>> getHeaders() {
        return this.headers;
    }

    void setHeaders(Map<String, Collection<String>> headers) {
        this.headers = headers;
    }

    @Override
    public Optional<InputStream> getResponseBody() {
        return this.responseBody;
    }

    void setResponseBody(Optional<InputStream> responseBody) {
        this.responseBody = responseBody;
    }

    static ResponseMetaData build(HttpServletResponse response) {
        return build(response, null);
    }

    static ResponseMetaData build(HttpServletResponse response, byte[] responseBody) {
        ResponseMetaDataWrapper meta = new ResponseMetaDataWrapper();
        // ContentType
        meta.setContentType(response.getContentType());
        // Headers
        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String name : headerNames) {
            Collection<String> values = new ArrayList<>(response.getHeaders(name));
            headers.put(name, Collections.unmodifiableCollection(values));
        }
        meta.setHeaders(Collections.unmodifiableMap(headers));
        // Status
        meta.setStatus(response.getStatus());
        // ResponseBody
        meta.setResponseBody(
                responseBody == null ? Optional.empty() : Optional.of(new ByteArrayInputStream(responseBody)));

        return meta;
    }

    /**
     * カスタム デシリアライズ.
     *
     * @param s ObjectInputStream
     * @throws Exception デシリアライズに失敗した場合
     */
    private void readObject(ObjectInputStream s) throws Exception {
        s.defaultReadObject();
        this.responseBody = Optional.empty();
    }
}
