package com.example.demo.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaveDB {
    private LoadAPIData apiData;
    private TerMapper terMapper;
    private TerLinkMapper terLinkMapper;
    private TerDriveMapper terDriveMapper;
    private TerScheduleMapper terScheduleMapper;

    @Autowired
    public SaveDB(LoadAPIData apiData, TerMapper terMapper, TerLinkMapper terLinkMapper, TerDriveMapper terDriveMapper, TerScheduleMapper terScheduleMapper) {
        this.apiData = apiData;
        this.terMapper = terMapper;
        this.terLinkMapper = terLinkMapper;
        this.terDriveMapper = terDriveMapper;
        this.terScheduleMapper = terScheduleMapper;
    }
    public void saveTerDB() throws JSONException {
        terMapper.deleteAll();

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
    public void saveTerLinkDB() throws JSONException {
        terLinkMapper.deleteAll();

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
    public void saveTerDriveDB() throws JSONException {
        terDriveMapper.deleteAll();

        List<TerLinkDto> test_linkTerList = terLinkMapper.selectAll();

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
    public void saveTerScheduleDB() throws JSONException {
        terScheduleMapper.deleteAll();

        List<TerLinkDto> test_linkTerList = terLinkMapper.selectAll();

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
        for (TerScheduleDto dto : terScheduleDtoList) {
            terScheduleMapper.insert(dto);
        }
    }

}
