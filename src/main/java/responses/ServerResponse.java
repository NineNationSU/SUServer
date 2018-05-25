package responses;

import com.google.gson.Gson;

/**
 * класс, описывающий ответ сервера
 */
public class ServerResponse {
    private String response;

    public ServerResponse(String response) {
        this.response = response;
    }

    public String getResponse() {

        return response;
    }

    public ServerResponse setResponse(String response) {

        this.response = response;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
