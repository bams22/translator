package ru.bams22.translate.model;

import org.json.JSONObject;
import org.json.JSONArray;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class YandexTranslater implements Translater {

    public static void main(String[] args) {
        System.out.println(new YandexTranslater().translate("en", args[0]));
    }

    public String translate(String input) {
        return translate("en", input);
    }

    public String translate(String lang, String input) {
        String result = null;
        try {
            String urlStr = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20191029T153712Z.57a5d0105aea38dd.89b4f4d8a148cb28a433e6c9af5afd3341f41000";
            URL urlObj = new URL(urlStr);
            HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(input, StandardCharsets.UTF_8.toString()) + "&lang=" + lang);
            InputStream response = connection.getInputStream();
            String json = new java.util.Scanner(response).nextLine();

            JSONObject jo = new JSONObject(json);
            JSONArray jsonArray = (JSONArray) jo.get("text");
            result = (String) jsonArray.get(0);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Encoding not supported");
        } catch (ProtocolException e) {
            System.out.println("Protocol error");
        } catch (MalformedURLException e) {
            System.out.println("Incorrect URL");
        } catch (IOException e) {
            System.out.println("Access problem");
        }
        return result;
    }
}
