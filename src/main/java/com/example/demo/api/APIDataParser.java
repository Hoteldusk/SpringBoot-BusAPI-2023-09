package com.example.demo.api;
import com.example.demo.api.LoadAPIData;
import com.example.demo.models.TerDriveDto;
import com.example.demo.models.TerDto;
import com.example.demo.models.TerLinkDto;
import com.example.demo.utils.DeduplicationUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class APIDataParser {
    LoadAPIData loadAPIData = new LoadAPIData();

    public List<TerDto> connectionTerAPIParse_GetTerList() throws JSONException {
        List<TerDto> terList = new ArrayList<>();
        String data = null;
        try {
            data = loadAPIData.connectionTerAPI();
        } catch (IOException e) {
            System.out.println("API 로딩실패");
        }
        JSONObject jsonObject = new JSONObject(data);
        JSONArray resultArray = jsonObject.getJSONArray("result");

        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject resultObject = resultArray.getJSONObject(i);
            TerDto depTerDto = new TerDto();

            //id
            String depTerId = resultObject.getLong("stationID") + "";
            depTerDto.setTerId(depTerId);
            //name
            String depTerName_Region = resultObject.getString("stationName");

            String depTerName = "";
            String depTerRegion = "";

            if (depTerName_Region.contains("/")) {
                String[] dep_parts = depTerName_Region.split("/");
                depTerRegion = dep_parts[0];
                depTerName = dep_parts[1];
            } else {
                depTerRegion = "광주";
                depTerName = depTerName_Region;
            }
            //name
            depTerDto.setTerName(depTerName);
            //region
            depTerDto.setTerRegion(depTerRegion);

            //x
            double depTerX = resultObject.getDouble("x");
            depTerDto.setTerCoorX(depTerX);
            //y
            double depTerY = resultObject.getDouble("y");
            depTerDto.setTerCoorY(depTerY);

            terList.add(depTerDto);

            boolean haveDestinationTer = resultObject.getBoolean("haveDestinationTerminals");

            if (haveDestinationTer) {
                JSONArray arrTers = resultObject.getJSONArray("destinationTerminals");
                for (int j = 0; j < arrTers.length(); j++) {

                    JSONObject arrTer = arrTers.getJSONObject(j);

                    TerDto arrTerDto = new TerDto();

                    //id
                    String arrTerId = arrTer.getLong("stationID") + "";
                    arrTerDto.setTerId(arrTerId);
                    //name
                    String arrTerName_Region = arrTer.getString("stationName");

                    String[] arr_parts = arrTerName_Region.split("/");

                    String arrTerRegion = arr_parts[0];
                    String arrTerName = arr_parts[1];
                    //name
                    arrTerDto.setTerName(arrTerName);
                    //region
                    arrTerDto.setTerRegion(arrTerRegion);

                    //x
                    double arrTerX = arrTer.getDouble("x");
                    arrTerDto.setTerCoorX(arrTerX);
                    //y
                    double arrTerY = arrTer.getDouble("y");
                    arrTerDto.setTerCoorY(arrTerY);

                    terList.add(arrTerDto);
                }// for end
            }
        }
        List<TerDto> deDupTerList = DeduplicationUtils.removeDuplicates(terList);
        return deDupTerList;
    }

    public List<TerLinkDto> connectionTerAPIParse_GetLinkTerList() throws JSONException {
        List<TerLinkDto> terLinkList = new ArrayList<>();
        String data = null;
        try {
            data = loadAPIData.connectionTerAPI();
        } catch (IOException e) {
            System.out.println("API 로딩실패");
            return null;
        }
        JSONObject jsonObject = new JSONObject(data);
        JSONArray resultArray = jsonObject.getJSONArray("result");

        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject resultObject = resultArray.getJSONObject(i);

            boolean haveDestinationTer = resultObject.getBoolean("haveDestinationTerminals");

            if (haveDestinationTer) {
                String depTerId = resultObject.getLong("stationID") + "";

                JSONArray arrTers = resultObject.getJSONArray("destinationTerminals");

                for (int j = 0; j < arrTers.length(); j++) {
                    JSONObject arrTer = arrTers.getJSONObject(j);

                    TerLinkDto terLinkDto = new TerLinkDto();

                    String arrTerId = arrTer.getLong("stationID") + "";

                    terLinkDto.setTl_DepTerId(depTerId);
                    terLinkDto.setTl_ArrTerId(arrTerId);
                    terLinkDto.setTl_Id(depTerId+arrTerId);
                    terLinkList.add(terLinkDto);
                }
            }
        }
        return terLinkList;
    }

    public List<TerDriveDto> connectionBusAPIParse(String depTerId, String arrTerId) {
        String data = null;
        try {
            data = loadAPIData.connectionBusAPI(depTerId, arrTerId);
        } catch (IOException e) {
            System.out.println("API 연결실패");
            return null;
        }
        return null;
    }
}
