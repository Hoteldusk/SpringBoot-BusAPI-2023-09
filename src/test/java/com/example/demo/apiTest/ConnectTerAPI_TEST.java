package com.example.demo.apiTest;

import com.example.demo.api.LoadAPIData;
import com.example.demo.mapper.TerDriveMapper;
import com.example.demo.mapper.TerLinkMapper;
import com.example.demo.mapper.TerMapper;
import com.example.demo.mapper.TerScheduleMapper;
import com.example.demo.models.TerDriveDto;
import com.example.demo.models.TerDto;
import com.example.demo.models.TerLinkDto;
import com.example.demo.models.TerScheduleDto;
import com.example.demo.utils.DeduplicationUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ConnectTerAPI_TEST {

    private LoadAPIData apiData;
    private TerMapper terMapper;
    private TerLinkMapper terLinkMapper;
    private TerDriveMapper terDriveMapper;
    private TerScheduleMapper terScheduleMapper;

    @Autowired
    public ConnectTerAPI_TEST(LoadAPIData apiData, TerMapper terMapper, TerLinkMapper terLinkMapper, TerDriveMapper terDriveMapper, TerScheduleMapper terScheduleMapper) {
        this.apiData = apiData;
        this.terMapper = terMapper;
        this.terLinkMapper = terLinkMapper;
        this.terDriveMapper = terDriveMapper;
        this.terScheduleMapper = terScheduleMapper;
    }

    /**
     * api 데이터 불러오기 테스트
     * 테스트 성공 (2023-07-06)
     */
    @Test
    public void connectionTerAPI() throws IOException {

        // ODsay Api Key 정보
        String apiKey = "";

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
    }

    /**
     * 전체 정류장 데이터 받아오기 테스트
     * 테스트 완료 (2023-07-07)
     */
    @Test
    public void connectionTerAPI_Parse_Save_TerData() throws JSONException {
        List<TerDto> terList = new ArrayList<>();
        String data = null;
        try {
            data = apiData.connectionTerAPI();
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
        List<TerDto> dedupTerList = DeduplicationUtils.removeDuplicates(terList);

        for (TerDto dto : dedupTerList) {
            terMapper.insert(dto);
        }
    }

    /**
     * 연결 정류장 받아오기 테스트
     * 테스트 완료 (2023-07-07)
     */
    @Test
    public void connectionTerAPI_Parse_Save_TerLink() throws JSONException {
        List<TerLinkDto> terLinkList = new ArrayList<>();
        String data = null;
        try {
            data = apiData.connectionTerAPI();
        } catch (IOException e) {
            System.out.println("API 로딩실패");
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
                    terLinkDto.setTl_Id(depTerId + arrTerId);
                    terLinkList.add(terLinkDto);
                }
            }

        }
        for (TerLinkDto dto : terLinkList) {
            terLinkMapper.insert(dto);
        }
    }

    /**
     * 노선 정보 받아오기 테스트
     * 테스트 완료 (2023-07-11)
     */
    @Test
    public void connectionBusAPI_Parse_Save_BusData() throws JSONException {
        // 테스트 데이터
        List<TerLinkDto> test_linkTerList = terLinkMapper.selectAll();

        // 로직 코드 시작
        String data = null;
        List<TerDriveDto> terDriveDtoList = new ArrayList<>();
        for (TerLinkDto dto : test_linkTerList) {
            String depTerId = dto.getTl_DepTerId();
            String arrTerId = dto.getTl_ArrTerId();
            try {
                data = apiData.connectionBusAPI(depTerId, arrTerId);
            } catch (IOException e) {
                System.out.println("BUS API 호출 오류");
                throw new RuntimeException(e);
            }
            JSONObject jsonObject = new JSONObject(data);
            JSONObject resultObject = jsonObject.getJSONObject("result");
            JSONArray stations = resultObject.getJSONArray("station");
            for (int i = 0; i < stations.length(); i++) {
                JSONObject station = stations.getJSONObject(i);
                TerDriveDto terDriveDto = new TerDriveDto();
                terDriveDto.setTd_TlID(dto.getTl_Id());
                terDriveDto.setTd_ID(dto.getTl_Id() + "_0" + i);
                // waste time
                String wasteTime = station.getString("wasteTime");
                terDriveDto.setTd_WasteTime(wasteTime);
                // interval
                String intervals = station.getString("interval");
                String[] lines = intervals.split("/");
                String interval = lines[0];
                terDriveDto.setTd_Interval(interval);
                // normal fare

                int nightFare = station.getInt("nightFare");

                if (nightFare == 0) {
                    int normalFare = station.getInt("normalFare");
                    terDriveDto.setTd_Fare(normalFare);
                    // 주간 스케쥴
                } else {
                    // 야간 스케쥴
                    terDriveDto.setTd_Fare(nightFare);
                }
                terDriveDtoList.add(terDriveDto);
            }
        }
        for (TerDriveDto dto : terDriveDtoList) {
            terDriveMapper.insert(dto);
        }
    }

    /**
     * 노선 스케쥴 받아오기 테스트
     * 테스트 완료 (2023-07-17)
     */
    @Test
    public void connectionBusAPI_Parse_Save_BusSchedule() throws JSONException {
        // 테스트 데이터
        List<TerLinkDto> test_linkTerList = terLinkMapper.selectAll();

        // terDrive 데이터 준비
        String data = null;
        List<TerScheduleDto> terScheduleDtoList = new ArrayList<>();

        for (TerLinkDto dto : test_linkTerList) {
            String depTerId = dto.getTl_DepTerId();
            String arrTerId = dto.getTl_ArrTerId();
            try {
                data = apiData.connectionBusAPI(depTerId, arrTerId);
            } catch (IOException e) {
                System.out.println("BUS API 호출 오류");
                throw new RuntimeException(e);
            }
            JSONObject jsonObject = new JSONObject(data);
            JSONObject resultObject = jsonObject.getJSONObject("result");
            JSONArray stations = resultObject.getJSONArray("station");
            for (int i = 0; i < stations.length(); i++) {
                JSONObject station = stations.getJSONObject(i);

                int normalFare = station.getInt("normalFare");
                int nightFare = station.getInt("nightFare");

                if (nightFare == 0) {
                    // 주간 스케쥴
                    String schedules = station.getString("schedule");
                    String[] lines = schedules.split("\n");

                    for (String line : lines) {
                        String[] times = line.split("/");
                        for (String time : times) {
                            TerScheduleDto terScheduleDto = new TerScheduleDto();
                            terScheduleDto.setTes_TdId(dto.getTl_Id() + "_0" + i);
                            terScheduleDto.setTes_Schedule(time);
                            terScheduleDtoList.add(terScheduleDto);
                        }
                    }
                } else if(normalFare == 0){
                    // 야간 스케쥴
                    String schedules = station.getString("nightSchedule");
                    String[] lines = schedules.split("\n");

                    for (String line : lines) {
                        String[] times = line.split("/");
                        for (String time : times) {
                            TerScheduleDto terScheduleDto = new TerScheduleDto();
                            terScheduleDto.setTes_TdId(dto.getTl_Id() + "_0" + i);
                            terScheduleDto.setTes_Schedule(time);
                            terScheduleDtoList.add(terScheduleDto);
                        }
                    }
                } else {
                    // 주간,야간 스케쥴
                    String schedules = station.getString("schedule");
                    String[] lines = schedules.split("\n");

                    for (String line : lines) {
                        String[] times = line.split("/");
                        for (String time : times) {
                            TerScheduleDto terScheduleDto = new TerScheduleDto();
                            terScheduleDto.setTes_TdId(dto.getTl_Id() + "_0" + i);
                            terScheduleDto.setTes_Schedule(time);
                            terScheduleDtoList.add(terScheduleDto);
                        }
                    }

                    schedules = station.getString("nightSchedule");
                    lines = schedules.split("\n");

                    for (String line : lines) {
                        String[] times = line.split("/");
                        for (String time : times) {
                            TerScheduleDto terScheduleDto = new TerScheduleDto();
                            terScheduleDto.setTes_TdId(dto.getTl_Id() + "_0" + i);
                            terScheduleDto.setTes_Schedule(time);
                            terScheduleDtoList.add(terScheduleDto);
                        }
                    }
                }
            }
        }
        // print
        for (TerScheduleDto dto : terScheduleDtoList) {
            terScheduleMapper.insert(dto);
        }
    }
}