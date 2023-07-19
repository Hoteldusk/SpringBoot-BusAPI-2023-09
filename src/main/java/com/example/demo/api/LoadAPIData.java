package com.example.demo.api;

import com.example.demo.config.APIKEY;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class LoadAPIData {
    public String connectionTerAPI() throws IOException {

        // ODsay Api Key 정보
        String apiKey = APIKEY.APIKEY;

        String urlInfo = "https://api.odsay.com/v1/api/intercityBusTerminals?lang=0&CID=5000&apiKey="
                + URLEncoder.encode(apiKey, "UTF-8");

        // http 연결
        URL url = new URL(urlInfo);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();
        conn.disconnect();

        // 결과 출력
        System.out.println(sb.toString());

        String result = sb.toString();
        return result;
    }

    public String connectionBusAPI(String depTerId, String arrTerId) throws IOException {
        // ODsay Api Key 정보
        String apiKey = APIKEY.APIKEY;

        String urlInfo = "https://api.odsay.com/v1/api/intercityServiceTime?lang=0"
                + "&startStationID=" + depTerId
                + "&endStationID=" + arrTerId
                + "&apiKey="
                + URLEncoder.encode(apiKey, "UTF-8");

        // http 연결
        URL url = new URL(urlInfo);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();
        conn.disconnect();

        // 결과 출력
        System.out.println(sb.toString());

        String result = sb.toString();
        return result;
    }
}
