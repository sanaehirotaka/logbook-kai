package logbook.proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * internal package private class.
 *
 * @see ResponseMetaData
 */
class ResponseMetaDataWrapper implements ResponseMetaData {

    private int status;

    private String contentType;

    private Map<String, Collection<String>> headers;

    public int getStatus() {
        return status;
    }

    void setStatus(int status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, Collection<String>> getHeaders() {
        return headers;
    }

    void setHeaders(Map<String, Collection<String>> headers) {
        this.headers = headers;
    }

    static ResponseMetaData build(HttpServletResponse response) {
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

        return meta;
    }
}
