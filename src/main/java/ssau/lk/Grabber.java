package ssau.lk;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import utility.http.Response;
import utility.http.RequestUtility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public abstract class Grabber {
    private static String getHeaderValueByName(Response response, String name){
        Header[] passportHeaders = response.getAllHeaders();
        String answer = null;
        for (Header h : passportHeaders){
            if (h.getName().equals(name)){
                answer = h.getValue().split(";")[0];
            }
        }
        return answer;
    }

    /**
     *
     * @param response ответ сервера
     * @return значение устанавливаемых Cookies
     */
    private static String getCookiesFromResponse(Response response){
        return getHeaderValueByName(response, "Set-Cookie");
    }

    /**
     * данный метод скачивает страничку личного кабинета по логину/паролю
     * @param login логин
     * @param password пароль
     * @return главная страница кабинета
     * @throws URISyntaxException если не удается создать ссылку по адресу (все ссылки внутри данной функции)
     * @throws IOException если не удается считать ответ на запрос
     */
    public static String getLK(String login, String password) throws URISyntaxException, IOException {
        final String PASSPORT = "https://passport.ssau.ru/";
        final String PASSPORT_AJAX_AUTH = "https://passport.ssau.ru/ajax.php?action=auth&data_type=json";
        final String LK = "http://lk.ssau.ru/";
        HttpGet requestGet = new HttpGet();
        HttpPost requestPost = new HttpPost();
        Response response;

        System.out.println("Start");
        requestGet.setURI(new URI(PASSPORT));
        response = RequestUtility.apacheGET(requestGet, false);
        String incubePassportCookies = getCookiesFromResponse(response);

        requestPost.setURI(new URI(PASSPORT_AJAX_AUTH));
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("login", login));
        params.add(new BasicNameValuePair("password", password));
        requestPost.setHeader("Cookie", incubePassportCookies);
        requestPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        response.setResponse(RequestUtility.apachePOST(requestPost).getResponse());
        String ssauCookie = getCookiesFromResponse(response);

        requestGet.setURI(new URI(LK));
        response = RequestUtility.apacheGET(requestGet, false);
        final String PASSPORT_WITH_CLIENT_ID = getHeaderValueByName(response, "Location");
        String incubeCookie = getCookiesFromResponse(response);

        requestGet.setURI(new URI(PASSPORT_WITH_CLIENT_ID));
        requestGet.setHeader("Cookie", incubeCookie + ";" + ssauCookie);
        response = RequestUtility.apacheGET(requestGet, true);

        String lkPage = response.getAnswer();

        StringBuilder sb = new StringBuilder(lkPage.length() / 10);
        for (int i = 0; i < lkPage.length()-1; i++) {
            if ((lkPage.charAt(i) == ' ' || lkPage.charAt(i) == '\t')
                    && (lkPage.charAt(i+1) == ' ' || lkPage.charAt(i+1) == '\t')){
                continue;
            }
            sb.append(lkPage.charAt(i));
        }
        sb.append(lkPage.charAt(lkPage.length() - 1));
        lkPage = sb.toString();
        sb.delete(0, lkPage.length());
        return lkPage;
    }

}
