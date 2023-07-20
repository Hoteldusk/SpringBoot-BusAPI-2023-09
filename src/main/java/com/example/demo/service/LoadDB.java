package com.example.demo.service;

import com.example.demo.api.LoadAPIData;
import com.example.demo.mapper.TerDriveMapper;
import com.example.demo.mapper.TerLinkMapper;
import com.example.demo.mapper.TerMapper;
import com.example.demo.mapper.TerScheduleMapper;
import com.example.demo.models.TerDto;
import com.example.demo.models.TerLinkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class LoadDB {
    private LoadAPIData apiData;
    private TerMapper terMapper;
    private TerLinkMapper terLinkMapper;
    private TerDriveMapper terDriveMapper;
    private TerScheduleMapper terScheduleMapper;
    @Autowired
    public LoadDB(LoadAPIData apiData, TerMapper terMapper, TerLinkMapper terLinkMapper, TerDriveMapper terDriveMapper, TerScheduleMapper terScheduleMapper) {
        this.apiData = apiData;
        this.terMapper = terMapper;
        this.terLinkMapper = terLinkMapper;
        this.terDriveMapper = terDriveMapper;
        this.terScheduleMapper = terScheduleMapper;
    }

    // 출발 드롭다운 데이터 전송
    public List<TerDto> loadDepTerData() {
        List<TerDto> resultList = new ArrayList<>();

        List<TerLinkDto> terLinkDtoList = terLinkMapper.selectAll();
        for (TerLinkDto terLinkDto : terLinkDtoList) {
            TerDto terDto = terMapper.getTerById(terLinkDto.getTl_DepTerId());
            resultList.add(terDto);
        }
        return resultList;
    }

    // 출발지 선택시 도착지 리스트 데이터 전송
    public List<TerDto> loadArrTerData(TerDto depTerDto) {
        List<TerDto> terDtoList = new ArrayList<>();

    }
}
