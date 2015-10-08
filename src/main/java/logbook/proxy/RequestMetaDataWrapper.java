package logbook.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * internal package private class.
 *
 * @see RequestMetaData
 */
class RequestMetaDataWrapper implements RequestMetaData {

    private String contentType;

    private Collection<Cookie> cookies;

    private Map<String, Collection<String>> headers;

    private String method;

    private Map<String, Collection<String>> parameterMap;

    private String protocol;

    private String queryString;

    private String remoteAddr;

    private int remotePort;

    private String requestURI;

    private String requestURL;

    private String scheme;

    private String serverName;

    private int serverPort;

    @Override
    public String getContentType() {
        return contentType;
    }

    void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public Collection<Cookie> getCookies() {
        return cookies;
    }

    void setCookies(Collection<Cookie> cookies) {
        this.cookies = cookies;
    }

    @Override
    public Map<String, Collection<String>> getHeaders() {
        return headers;
    }

    void setHeaders(Map<String, Collection<String>> headers) {
        this.headers = headers;
    }

    @Override
    public String getMethod() {
        return method;
    }

    void setMethod(String method) {
        this.method = method;
    }

    @Override
    public Map<String, Collection<String>> getParameterMap() {
        return parameterMap;
    }

    void setParameterMap(Map<String, Collection<String>> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public String getRemoteAddr() {
        return remoteAddr;
    }

    void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    @Override
    public int getRemotePort() {
        return remotePort;
    }

    void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    @Override
    public String getRequestURL() {
        return requestURL;
    }

    void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    static RequestMetaData build(HttpServletRequest request) {
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
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Collection<String>> newpara = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> e : parameterMap.entrySet()) {
            String key = e.getKey();
            String[] orig = e.getValue();
            newpara.put(key, Collections.unmodifiableCollection(Arrays.asList(orig)));
        }
        meta.setParameterMap(Collections.unmodifiableMap(newpara));
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

        return meta;
    }
}
