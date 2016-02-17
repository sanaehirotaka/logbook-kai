package logbook.proxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

/**
 * internal package private class.
 *
 * @see RequestMetaData
 */
class RequestMetaDataWrapper implements RequestMetaData {

    private static final long serialVersionUID = 8302943521163115949L;

    private String contentType;

    private Collection<Cookie> cookies;

    private Map<String, Collection<String>> headers;

    private String method;

    private Map<String, List<String>> parameterMap;

    private String protocol;

    private String queryString;

    private String remoteAddr;

    private int remotePort;

    private String requestURI;

    private String requestURL;

    private String scheme;

    private String serverName;

    private int serverPort;

    private transient Optional<InputStream> requestBody;

    @Override
    public String getContentType() {
        return this.contentType;
    }

    void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public Collection<Cookie> getCookies() {
        return this.cookies;
    }

    void setCookies(Collection<Cookie> cookies) {
        this.cookies = cookies;
    }

    @Override
    public Map<String, Collection<String>> getHeaders() {
        return this.headers;
    }

    void setHeaders(Map<String, Collection<String>> headers) {
        this.headers = headers;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    void setMethod(String method) {
        this.method = method;
    }

    @Override
    public Map<String, List<String>> getParameterMap() {
        return this.parameterMap;
    }

    void setParameterMap(Map<String, List<String>> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getQueryString() {
        return this.queryString;
    }

    void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    @Override
    public int getRemotePort() {
        return this.remotePort;
    }

    void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    @Override
    public String getRequestURI() {
        return this.requestURI;
    }

    void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    @Override
    public String getRequestURL() {
        return this.requestURL;
    }

    void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String getServerName() {
        return this.serverName;
    }

    void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public int getServerPort() {
        return this.serverPort;
    }

    void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public Optional<InputStream> getRequestBody() {
        return this.requestBody;
    }

    void setRequestBody(Optional<InputStream> requestBody) {
        this.requestBody = requestBody;
    }

    static RequestMetaData build(HttpServletRequest request) {
        return build(request, null);
    }

    static RequestMetaData build(HttpServletRequest request, byte[] requestBody) {
        RequestMetaDataWrapper meta = new RequestMetaDataWrapper();
        // ContentType
        meta.setContentType(request.getContentType());
        // Cookies
        Cookie[] cookies = request.getCookies();
        Collection<Cookie> newcookies = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                newcookies.add((Cookie) cookie.clone());
            }
        }
        meta.setCookies(Collections.unmodifiableCollection(newcookies));
        // Headers
        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        List<String> headerNames = Collections.list(request.getHeaderNames());
        for (String name : headerNames) {
            Collection<String> values = Collections.list(request.getHeaders(name))
                    .stream()
                    .collect(Collectors.toList());
            headers.put(name, Collections.unmodifiableCollection(values));
        }
        meta.setHeaders(Collections.unmodifiableMap(headers));
        // Method
        meta.setMethod(request.getMethod());
        // ParameterMap
        if (requestBody != null) {
            String contentType = HttpFields.valueParameters(meta.getContentType(), null);
            if (MimeTypes.Type.FORM_ENCODED.is(contentType)) {
                try (InputStream in = new ByteArrayInputStream(requestBody)) {
                    MultiMap<String> parameter = new MultiMap<>();
                    UrlEncoded.decodeTo(in, parameter, request.getCharacterEncoding(),
                            Integer.MAX_VALUE, Integer.MAX_VALUE);
                    meta.setParameterMap(Collections.unmodifiableMap(parameter));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                meta.setParameterMap(Collections.emptyMap());
            }
        } else {
            meta.setParameterMap(Collections.emptyMap());
        }
        // Protocol
        meta.setProtocol(request.getProtocol());
        // QueryString
        meta.setQueryString(request.getQueryString());
        // RemoteAddr
        meta.setRemoteAddr(request.getRemoteAddr());
        // RemotePort
        meta.setRemotePort(request.getRemotePort());
        // RequestURI
        meta.setRequestURI(request.getRequestURI());
        // RequestURL
        meta.setRequestURL(request.getRequestURL().toString());
        // Scheme
        meta.setScheme(request.getScheme());
        // ServerName
        meta.setServerName(request.getServerName());
        // ServerPort
        meta.setServerPort(request.getServerPort());
        // RequestBody
        meta.setRequestBody(
                requestBody == null ? Optional.empty() : Optional.of(new ByteArrayInputStream(requestBody)));
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
        this.requestBody = Optional.empty();
    }
}
