package ssau.lk;

import database.exceptions.ObjectInitException;
import database.utility.DatabaseConnector;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utility.http.Response;
import utility.http.RequestUtility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;

/**
 * класс, предоставляющий методы для скачивания расписания с сайта lk.ssau.ru
 */
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
        try{
            String sqlRequest = "SELECT * FROM suappdatabase_test.lk_keys WHERE login=?;";
            PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
            statement.setString(1, login);
            ResultSet res = statement.executeQuery();
            if(res.next()){
                HttpGet requestGet =new HttpGet();
                requestGet.setURI(new URI(res.getString("auth_key")));
                Response response = RequestUtility.apacheGET(requestGet, true);
                String lk = response.getAnswer();
                Document document = Jsoup.parse(lk);
                String salary =  document.select("div.right_col_inside").text();
                if (!salary.equals(""))
                    return trim(lk);
            }
        } catch (SQLException | ObjectInitException ignored) {}
        final String PASSPORT = "http://passport.ssau.ru/";
        final String PASSPORT_AJAX_AUTH = "https://passport.ssau.ru/ajax.php?action=auth&data_type=json";
        final String LK = "http://lk.ssau.ru/";
        HttpGet requestGet = new HttpGet();
        HttpPost requestPost = new HttpPost();
        Response response;

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
        response = RequestUtility.apacheGET(requestGet, false);

        final String KEY = getHeaderValueByName(response, "Location");


        try {
            String sqlRequest = "SELECT COUNT(login) FROM suappdatabase_test.lk_keys WHERE login=?;";
            PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
            statement.setString(1, login);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                if (set.getInt(1) == 1){
                    sqlRequest = "UPDATE suappdatabase_test.lk_keys SET auth_key=? WHERE login=?;";
                    statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
                    statement.setString(2, login);
                    statement.setString(1, KEY);
                    statement.executeUpdate();
                }else{
                    sqlRequest = "INSERT INTO suappdatabase_test.lk_keys SET login=?, auth_key=?;";
                    statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
                    statement.setString(1, escapeJava(login));
                    statement.setString(2, escapeJava(KEY));
                    statement.executeUpdate();
                }
            }
        } catch (SQLException | ObjectInitException ignored) {
            ignored.printStackTrace();
        }

        requestGet.setURI(new URI(KEY));
        requestGet.setHeader("Cookie", incubeCookie);
        response = RequestUtility.apacheGET(requestGet, true);

        return trim(response.getAnswer());


    }

    private static String trim(String lkPage){
        StringBuilder sb = new StringBuilder(lkPage.length() / 10);
        for (int i = 0; i < lkPage.length()-1; i++) {
            if ((lkPage.charAt(i) == ' ' || lkPage.charAt(i) == '\t')
                    && (lkPage.charAt(i+1) == ' ' || lkPage.charAt(i+1) == '\t')){
                continue;
            }
            sb.append(lkPage.charAt(i));
        }
        sb.append(lkPage.charAt(lkPage.length() - 1));
        return sb.toString();
    }

}
