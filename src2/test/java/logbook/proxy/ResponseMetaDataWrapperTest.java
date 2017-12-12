package logbook.proxy;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class ResponseMetaDataWrapperTest {

    /**
     * {@link logbook.proxy.ResponseMetaDataWrapper#getStatus()} のためのテスト・メソッド。
     */
    @Test
    public void testGetStatus() {
        ResponseMetaDataWrapper w = new ResponseMetaDataWrapper();
        int expected = 99412;
        w.setStatus(expected);
        assertEquals(expected, w.getStatus());
    }

    /**
     * {@link logbook.proxy.ResponseMetaDataWrapper#getContentType()} のためのテスト・メソッド。
     */
    @Test
    public void testGetContentType() {
        ResponseMetaDataWrapper w = new ResponseMetaDataWrapper();
        String expected = "testGetContentType";
        w.setContentType(expected);
        assertEquals(expected, w.getContentType());
    }

    /**
     * {@link logbook.proxy.ResponseMetaDataWrapper#getHeaders()} のためのテスト・メソッド。
     */
    @Test
    public void testGetHeaders() {
        ResponseMetaDataWrapper w = new ResponseMetaDataWrapper();
        Map<String, Collection<String>> expected = new HashMap<>();
        expected.put("test", Arrays.asList("testGetHeaders"));
        w.setHeaders(expected);
        assertEquals(expected, w.getHeaders());
    }

    /**
     * {@link logbook.proxy.ResponseMetaDataWrapper#getResponseBody()} のためのテスト・メソッド。
     */
    @Test
    public void testGetResponseBody() {
        ResponseMetaDataWrapper w = new ResponseMetaDataWrapper();
        Optional<InputStream> req = Optional.of(new ByteArrayInputStream(new byte[] {}));
        w.setResponseBody(req);
        assertEquals(req, w.getResponseBody());
    }

    /**
     * {@link logbook.proxy.ResponseMetaDataWrapper#build(javax.servlet.http.HttpServletResponse)} のためのテスト・メソッド。
     * @throws IOException
     */
    @Test
    public void testBuild() throws IOException {
        // expected data

        String contentType = "getContentType";

        Map<String, List<String>> headers = new LinkedHashMap<>();
        headers.put("headertest1", Arrays.asList("headervalue1"));
        headers.put("headertest2", Arrays.asList("headervalue2"));

        Map<String, List<String>> parameterMap = new LinkedHashMap<>();
        parameterMap.put("parametertest1", Arrays.asList("parameterMapvalue1"));
        parameterMap.put("parametertest2", Arrays.asList("parameterMapvalue2"));

        int status = 4011;

        byte[] responseBody = "getResponseBody".getBytes();

        // mock Response
        HttpServletResponse Response = new MockHttpServletResponseAdapter() {
            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return headers.keySet();
            }

            @Override
            public Collection<String> getHeaders(String s) {
                return headers.get(s);
            }

            @Override
            public int getStatus() {
                return status;
            }
        };
        ResponseMetaData data = ResponseMetaDataWrapper.build(Response, responseBody);

        // getContentType
        assertEquals(contentType, data.getContentType());
        // getHeaders
        Map<String, Collection<String>> actualHeaders = data.getHeaders();
        assertEquals(headers.keySet(), actualHeaders.keySet());
        for (Entry<String, List<String>> expectedHeader : headers.entrySet()) {
            Collection<String> actualHeader = actualHeaders.get(expectedHeader.getKey());
            assertEquals(expectedHeader.getValue(), new ArrayList<>(actualHeader));
        }
        // getStatus
        assertEquals(status, data.getStatus());
        // getResponseBody
        assertEquals(new String(responseBody), toString(data.getResponseBody().get()));
    }

    private static String toString(InputStream in) throws IOException {
        byte[] b = new byte[1024];
        int l = in.read(b, 0, b.length);
        return new String(b, 0, l);
    }

    /**
     * HttpServletResponse の Mock
     */
    private static class MockHttpServletResponseAdapter implements HttpServletResponse {

        @Override
        public String getCharacterEncoding() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getContentType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setCharacterEncoding(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setContentLength(int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setContentLengthLong(long l) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setContentType(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setBufferSize(int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getBufferSize() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void flushBuffer() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void resetBuffer() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isCommitted() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLocale(Locale locale) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Locale getLocale() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addCookie(Cookie cookie) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsHeader(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String encodeURL(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String encodeRedirectURL(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String encodeUrl(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String encodeRedirectUrl(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sendError(int i, String s) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sendError(int i) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sendRedirect(String s) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setDateHeader(String s, long l) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addDateHeader(String s, long l) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setHeader(String s, String s1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addHeader(String s, String s1) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setIntHeader(String s, int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addIntHeader(String s, int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setStatus(int i) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setStatus(int i, String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getStatus() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getHeader(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<String> getHeaders(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<String> getHeaderNames() {
            throw new UnsupportedOperationException();
        }

    }
}
