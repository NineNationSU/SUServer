package responses;

import com.google.gson.Gson;

public class OKResponse {
    private String response = "OK";

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
