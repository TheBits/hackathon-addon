package ru.yandex.semantic_geo;

import junit.framework.TestCase;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.io.*;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rasifiel
 * Date: 06.10.12
 * Time: 20:27
 */
public class EntryServletTest extends TestCase {
    public void testDoPost() throws Exception {
        MockRequest req = new MockRequest();
        MockResp resp = new MockResp();
        EntryServlet servlet = new EntryServlet();
        servlet.doPost(req,resp);
        System.out.println(new String(resp.bos.toByteArray()));
    }

    class MockResp implements HttpServletResponse {

        public ByteArrayOutputStream bos = new ByteArrayOutputStream();

        public void addCookie(final Cookie cookie) {

        }

        public boolean containsHeader(final String name) {
            return false;
        }

        public String encodeURL(final String url) {
            return null;
        }

        public String encodeRedirectURL(final String url) {
            return null;
        }

        public String encodeUrl(final String url) {
            return null;
        }

        public String encodeRedirectUrl(final String url) {
            return null;
        }

        public void sendError(final int sc, final String msg) throws IOException {

        }

        public void sendError(final int sc) throws IOException {

        }

        public void sendRedirect(final String location) throws IOException {

        }

        public void setDateHeader(final String name, final long date) {

        }

        public void addDateHeader(final String name, final long date) {

        }

        public void setHeader(final String name, final String value) {

        }

        public void addHeader(final String name, final String value) {

        }

        public void setIntHeader(final String name, final int value) {

        }

        public void addIntHeader(final String name, final int value) {

        }

        public void setStatus(final int sc) {

        }

        public void setStatus(final int sc, final String sm) {

        }

        public String getCharacterEncoding() {
            return null;
        }

        public String getContentType() {
            return null;
        }

        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                @Override
                public void write(final int b) throws IOException {
                    bos.write(b);
                }
            };
        }

        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(getOutputStream());
        }

        public void setCharacterEncoding(final String charset) {

        }

        public void setContentLength(final int len) {

        }

        public void setContentType(final String type) {

        }

        public void setBufferSize(final int size) {

        }

        public int getBufferSize() {
            return 0;
        }

        public void flushBuffer() throws IOException {

        }

        public void resetBuffer() {

        }

        public boolean isCommitted() {
            return false;
        }

        public void reset() {

        }

        public void setLocale(final Locale loc) {

        }

        public Locale getLocale() {
            return null;
        }
    }

    class MockRequest implements HttpServletRequest {

        public String getAuthType() {
            return null;
        }

        public Cookie[] getCookies() {
            return new Cookie[0];
        }

        public long getDateHeader(final String name) {
            return 0;
        }

        public String getHeader(final String name) {
            return null;
        }

        public Enumeration getHeaders(final String name) {
            return null;
        }

        public Enumeration getHeaderNames() {
            return null;
        }

        public int getIntHeader(final String name) {
            return 0;
        }

        public String getMethod() {
            return null;
        }

        public String getPathInfo() {
            return null;
        }

        public String getPathTranslated() {
            return null;
        }

        public String getContextPath() {
            return null;
        }

        public String getQueryString() {
            return null;
        }

        public String getRemoteUser() {
            return null;
        }

        public boolean isUserInRole(final String role) {
            return false;
        }

        public Principal getUserPrincipal() {
            return null;
        }

        public String getRequestedSessionId() {
            return null;
        }

        public String getRequestURI() {
            return null;
        }

        public StringBuffer getRequestURL() {
            return null;
        }

        public String getServletPath() {
            return null;
        }

        public HttpSession getSession(final boolean create) {
            return null;
        }

        public HttpSession getSession() {
            return null;
        }

        public boolean isRequestedSessionIdValid() {
            return false;
        }

        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        public boolean isRequestedSessionIdFromUrl() {
            return false;
        }

        public Object getAttribute(final String name) {
            return null;
        }

        public Enumeration getAttributeNames() {
            return null;
        }

        public String getCharacterEncoding() {
            return null;
        }

        public void setCharacterEncoding(final String env) throws UnsupportedEncodingException {

        }

        public int getContentLength() {
            return 0;
        }

        public String getContentType() {
            return null;
        }

        public ServletInputStream getInputStream() throws IOException {
            return new ServletInputStream() {
                ByteArrayInputStream byis =  new ByteArrayInputStream("[\"Kaluga\",\"asas New York\"]".getBytes());

                @Override
                public int read() throws IOException {
                    return byis.read();
                }
            };
        }

        public String getParameter(final String name) {
            return null;
        }

        public Enumeration getParameterNames() {
            return null;
        }

        public String[] getParameterValues(final String name) {
            return new String[0];
        }

        public Map getParameterMap() {
            return null;
        }

        public String getProtocol() {
            return null;
        }

        public String getScheme() {
            return null;
        }

        public String getServerName() {
            return null;
        }

        public int getServerPort() {
            return 0;
        }

        public BufferedReader getReader() throws IOException {
            return null;
        }

        public String getRemoteAddr() {
            return null;
        }

        public String getRemoteHost() {
            return null;
        }

        public void setAttribute(final String name, final Object o) {

        }

        public void removeAttribute(final String name) {

        }

        public Locale getLocale() {
            return null;
        }

        public Enumeration getLocales() {
            return null;
        }

        public boolean isSecure() {
            return false;
        }

        public RequestDispatcher getRequestDispatcher(final String path) {
            return null;
        }

        public String getRealPath(final String path) {
            return null;
        }

        public int getRemotePort() {
            return 0;
        }

        public String getLocalName() {
            return null;
        }

        public String getLocalAddr() {
            return null;
        }

        public int getLocalPort() {
            return 0;
        }
    }
}
