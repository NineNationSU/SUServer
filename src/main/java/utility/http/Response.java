package utility.http;


import org.apache.http.*;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class Response {
    private HttpResponse response;
    private String answer = "";

    public Response() {	}

    public Response(HttpResponse response) {
        this.response = response;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public Response setResponse(HttpResponse response) {
        this.response = response;
        return this;
    }

    public String getAnswer() throws IOException {
        if (answer.isEmpty()) {
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String temp;
            StringBuilder sb = new StringBuilder();
            while ((temp = in.readLine()) != null) {
                sb.append(temp).append('\n');
            }
            in.close();
            answer = sb.toString();
        }

        return answer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Response{");
        sb.append("response=").append(response);
        sb.append(", answer='").append(answer).append('\'');
        sb.append('}');
        return sb.toString();
    }


    public StatusLine getStatusLine() {
        return response.getStatusLine();
    }

    public void setStatusLine(StatusLine statusLine) {
        response.setStatusLine(statusLine);
    }

    public void setStatusLine(ProtocolVersion protocolVersion, int i) {
        response.setStatusLine(protocolVersion, i);
    }

    public void setStatusLine(ProtocolVersion protocolVersion, int i, String s) {
        response.setStatusLine(protocolVersion, i, s);
    }

    public void setStatusCode(int i) throws IllegalStateException {
        response.setStatusCode(i);
    }

    public void setReasonPhrase(String s) throws IllegalStateException {
        response.setReasonPhrase(s);
    }

    public HttpEntity getEntity() {
        return response.getEntity();
    }

    public void setEntity(HttpEntity httpEntity) {
        response.setEntity(httpEntity);
    }

    public Locale getLocale() {
        return response.getLocale();
    }

    public void setLocale(Locale locale) {
        response.setLocale(locale);
    }

    public ProtocolVersion getProtocolVersion() {
        return response.getProtocolVersion();
    }

    public boolean containsHeader(String s) {
        return response.containsHeader(s);
    }

    public Header[] getHeaders(String s) {
        return response.getHeaders(s);
    }

    public Header getFirstHeader(String s) {
        return response.getFirstHeader(s);
    }

    public Header getLastHeader(String s) {
        return response.getLastHeader(s);
    }

    public Header[] getAllHeaders() {
        return response.getAllHeaders();
    }

    public void addHeader(Header header) {
        response.addHeader(header);
    }

    public void addHeader(String s, String s1) {
        response.addHeader(s, s1);
    }

    public void setHeader(Header header) {
        response.setHeader(header);
    }

    public void setHeader(String s, String s1) {
        response.setHeader(s, s1);
    }

    public void setHeaders(Header[] headers) {
        response.setHeaders(headers);
    }

    public void removeHeader(Header header) {
        response.removeHeader(header);
    }

    public void removeHeaders(String s) {
        response.removeHeaders(s);
    }

    public HeaderIterator headerIterator() {
        return response.headerIterator();
    }

    public HeaderIterator headerIterator(String s) {
        return response.headerIterator(s);
    }

    /** @deprecated */
    @Deprecated
    public HttpParams getParams() {
        return response.getParams();
    }

    /** @deprecated
     * @param httpParams */
    @Deprecated
    public void setParams(HttpParams httpParams) {
        response.setParams(httpParams);
    }
}

