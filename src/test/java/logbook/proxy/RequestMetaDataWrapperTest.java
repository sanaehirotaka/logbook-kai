package logbook.proxy;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import org.eclipse.jetty.http.MimeTypes;
import org.junit.Test;

public class RequestMetaDataWrapperTest {

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getContentType()} のためのテスト・メソッド。
     */
    @Test
    public void testGetContentType() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        String expected = "testGetContentType";
        w.setContentType(expected);
        assertEquals(expected, w.getContentType());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getCookies()} のためのテスト・メソッド。
     */
    @Test
    public void testGetCookies() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        Collection<Cookie> expected = Arrays.asList(new Cookie("test", "testGetCookies"));
        w.setCookies(expected);
        assertEquals(expected, w.getCookies());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getHeaders()} のためのテスト・メソッド。
     */
    @Test
    public void testGetHeaders() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        Map<String, Collection<String>> expected = new HashMap<>();
        expected.put("test", Arrays.asList("testGetHeaders"));
        w.setHeaders(expected);
        assertEquals(expected, w.getHeaders());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getMethod()} のためのテスト・メソッド。
     */
    @Test
    public void testGetMethod() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        String expected = "testGetMethod";
        w.setMethod(expected);
        assertEquals(expected, w.getMethod());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getParameterMap()} のためのテスト・メソッド。
     */
    @Test
    public void testGetParameterMap() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("test", Arrays.asList("testGetParameterMap"));
        w.setParameterMap(expected);
        assertEquals(expected, w.getParameterMap());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getProtocol()} のためのテスト・メソッド。
     */
    @Test
    public void testGetProtocol() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        String expected = "testGetProtocol";
        w.setProtocol(expected);
        assertEquals(expected, w.getProtocol());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getQueryString()} のためのテスト・メソッド。
     */
    @Test
    public void testGetQueryString() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        String expected = "testGetQueryString";
        w.setQueryString(expected);
        assertEquals(expected, w.getQueryString());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getRemoteAddr()} のためのテスト・メソッド。
     */
    @Test
    public void testGetRemoteAddr() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        String expected = "testGetRemoteAddr";
        w.setRemoteAddr(expected);
        assertEquals(expected, w.getRemoteAddr());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getRemotePort()} のためのテスト・メソッド。
     */
    @Test
    public void testGetRemotePort() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        int expected = 123456;
        w.setRemotePort(expected);
        assertEquals(expected, w.getRemotePort());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getRequestURI()} のためのテスト・メソッド。
     */
    @Test
    public void testGetRequestURI() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        String expected = "testGetRequestURI";
        w.setRequestURI(expected);
        assertEquals(expected, w.getRequestURI());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getRequestURL()} のためのテスト・メソッド。
     */
    @Test
    public void testGetRequestURL() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        String expected = "testGetRequestURL";
        w.setRequestURL(expected);
        assertEquals(expected, w.getRequestURL());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getScheme()} のためのテスト・メソッド。
     */
    @Test
    public void testGetScheme() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        String expected = "testGetScheme";
        w.setScheme(expected);
        assertEquals(expected, w.getScheme());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getServerName()} のためのテスト・メソッド。
     */
    @Test
    public void testGetServerName() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        String expected = "testGetServerName";
        w.setServerName(expected);
        assertEquals(expected, w.getServerName());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getServerPort()} のためのテスト・メソッド。
     */
    @Test
    public void testGetServerPort() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        int expected = 1234567;
        w.setServerPort(expected);
        assertEquals(expected, w.getServerPort());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#getRequestBody()} のためのテスト・メソッド。
     */
    @Test
    public void testGetRequestBody() {
        RequestMetaDataWrapper w = new RequestMetaDataWrapper();
        Optional<InputStream> req = Optional.of(new ByteArrayInputStream(new byte[] {}));
        w.setRequestBody(req);
        assertEquals(req, w.getRequestBody());
    }

    /**
     * {@link logbook.proxy.RequestMetaDataWrapper#build(javax.servlet.http.HttpServletRequest)} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testBuild() throws IOException {
        // expected data

        String contentType = MimeTypes.Type.FORM_ENCODED.asString();

        List<Cookie> cookies = Arrays.asList(new Cookie("cookietest1", "cookievalue1"));

        Map<String, List<String>> headers = new LinkedHashMap<>();
        headers.put("headertest1", Arrays.asList("headervalue1"));
        headers.put("headertest2", Arrays.asList("headervalue2"));

        String method = "getMethod";

        Map<String, List<String>> parameterMap = new LinkedHashMap<>();
        parameterMap.put("parametertest1", Arrays.asList("parameterMapvalue1"));
        parameterMap.put("parametertest2", Arrays.asList("parameterMapvalue2"));

        String protocol = "getProtocol";

        String queryString = "getQueryString";

        String remoteAddr = "getRemoteAddr";

        int remotePort = 124128;

        String requestURI = "getRequestURI";

        String requestURL = "getRequestURL";

        String scheme = "getScheme";

        String serverName = "getServerName";

        int serverPort = 7557;

        byte[] requestBody = "parametertest1=parameterMapvalue1&parametertest2=parameterMapvalue2".getBytes();

        // mock request
        HttpServletRequest request = new MockHttpServletRequestAdapter() {
            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public Cookie[] getCookies() {
                return cookies.toArray(new Cookie[cookies.size()]);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return Collections.enumeration(headers.keySet());
            }

            @Override
            public Enumeration<String> getHeaders(String s) {
                return Collections.enumeration(headers.get(s));
            }

            @Override
            public String getMethod() {
                return method;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return new LinkedHashMap<>();
            }

            @Override
            public String getProtocol() {
                return protocol;
            }

            @Override
            public String getQueryString() {
                return queryString;
            }

            @Override
            public String getRemoteAddr() {
                return remoteAddr;
            }

            @Override
            public int getRemotePort() {
                return remotePort;
            }

            @Override
            public String getRequestURI() {
                return requestURI;
            }

            @Override
            public StringBuffer getRequestURL() {
                return new StringBuffer(requestURL);
            }

            @Override
            public String getScheme() {
                return scheme;
            }

            @Override
            public String getServerName() {
                return serverName;
            }

            @Override
            public int getServerPort() {
                return serverPort;
            }
        };
        RequestMetaData data = RequestMetaDataWrapper.build(request, requestBody);

        // getContentType
        assertEquals(contentType, data.getContentType());
        // getCookies
        List<Cookie> actualCookies = new ArrayList<>(data.getCookies());
        assertEquals(cookies.size(), actualCookies.size());
        for (int i = 0; i < cookies.size(); i++) {
            Cookie expected = cookies.get(i);
            Cookie actual = actualCookies.get(i);
            if (expected == actual) {
                fail("インスタンスが等しい(コピーされていない)");
            }
            if (!expected.getName().equals(actual.getName())) {
                fail();
            }
        }
        // getHeaders
        Map<String, Collection<String>> actualHeaders = data.getHeaders();
        assertEquals(headers.keySet(), actualHeaders.keySet());
        for (Entry<String, List<String>> expectedHeader : headers.entrySet()) {
            Collection<String> actualHeader = actualHeaders.get(expectedHeader.getKey());
            assertEquals(expectedHeader.getValue(), new ArrayList<>(actualHeader));
        }
        // getMethod
        assertEquals(method, data.getMethod());
        // getParameterMap
        Map<String, List<String>> actualParameterMap = data.getParameterMap();
        assertEquals(parameterMap.keySet(), actualParameterMap.keySet());
        for (Entry<String, List<String>> expectedParameter : parameterMap.entrySet()) {
            Collection<String> actualParameter = actualParameterMap.get(expectedParameter.getKey());
            assertEquals(expectedParameter.getValue(), new ArrayList<>(actualParameter));
        }
        // getProtocol
        assertEquals(protocol, data.getProtocol());
        // getQueryString
        assertEquals(queryString, data.getQueryString());
        // getRemoteAddr
        assertEquals(remoteAddr, data.getRemoteAddr());
        // getRemotePort
        assertEquals(remotePort, data.getRemotePort());
        // getRequestURI
        assertEquals(requestURI, data.getRequestURI());
        // getRequestURL
        assertEquals(requestURL, data.getRequestURL());
        // getScheme
        assertEquals(scheme, data.getScheme());
        // getServerName
        assertEquals(serverName, data.getServerName());
        // getServerPort
        assertEquals(serverPort, data.getServerPort());
        // getRequestBody
        assertEquals(new String(requestBody), toString(data.getRequestBody().get()));
    }

    private static String toString(InputStream in) throws IOException {
        byte[] b = new byte[1024];
        int l = in.read(b, 0, b.length);
        return new String(b, 0, l);
    }

    /**
     * HttpServletRequest の Mock
     */
    private static abstract class MockHttpServletRequestAdapter implements HttpServletRequest {

        @Override
        public Object getAttribute(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getCharacterEncoding() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getContentLength() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getContentLengthLong() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getContentType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getParameter(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Enumeration<String> getParameterNames() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String[] getParameterValues(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getProtocol() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getScheme() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getServerName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getServerPort() {
            throw new UnsupportedOperationException();
        }

        @Override
        public BufferedReader getReader() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getRemoteAddr() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getRemoteHost() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAttribute(String s, Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void removeAttribute(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Locale getLocale() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Enumeration<Locale> getLocales() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isSecure() {
            throw new UnsupportedOperationException();
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getRealPath(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getRemotePort() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLocalName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLocalAddr() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getLocalPort() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ServletContext getServletContext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public AsyncContext startAsync() throws IllegalStateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletrequest, ServletResponse servletresponse)
                throws IllegalStateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isAsyncStarted() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isAsyncSupported() {
            throw new UnsupportedOperationException();
        }

        @Override
        public AsyncContext getAsyncContext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public DispatcherType getDispatcherType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAuthType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Cookie[] getCookies() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getDateHeader(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getHeader(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Enumeration<String> getHeaders(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getIntHeader(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getMethod() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getPathInfo() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getPathTranslated() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getContextPath() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getQueryString() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getRemoteUser() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isUserInRole(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Principal getUserPrincipal() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getRequestedSessionId() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getRequestURI() {
            throw new UnsupportedOperationException();
        }

        @Override
        public StringBuffer getRequestURL() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getServletPath() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpSession getSession(boolean flag) {
            throw new UnsupportedOperationException();
        }

        @Override
        public HttpSession getSession() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String changeSessionId() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isRequestedSessionIdValid() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isRequestedSessionIdFromCookie() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isRequestedSessionIdFromURL() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isRequestedSessionIdFromUrl() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean authenticate(HttpServletResponse httpservletresponse) throws IOException, ServletException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void login(String s, String s1) throws ServletException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void logout() throws ServletException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<Part> getParts() throws IOException, ServletException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Part getPart(String s) throws IOException, ServletException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T extends HttpUpgradeHandler> T upgrade(Class<T> class1) throws IOException, ServletException {
            throw new UnsupportedOperationException();
        }
    }
}
