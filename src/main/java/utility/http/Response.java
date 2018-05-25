package utility.http;


import org.apache.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Объект, описывающий ответ на HTTP-запрос
 */
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
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Charset.forName("UTF-8")));
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
        return answer;
    }

    public Header[] getAllHeaders() {
        return response.getAllHeaders();
    }
}

