package com.example.demo.service;

import com.example.demo.api.LoadAPIData;
import com.example.demo.mapper.TerDriveMapper;
import com.example.demo.mapper.TerLinkMapper;
import com.example.demo.mapper.TerMapper;
import com.example.demo.mapper.TerScheduleMapper;
import com.example.demo.models.*;
import com.example.demo.utils.DeduplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class LoadDB {
    private TerMapper terMapper;
    private TerLinkMapper terLinkMapper;
    private TerDriveMapper terDriveMapper;
    private TerScheduleMapper terScheduleMapper;
    @Autowired
    public LoadDB(TerMapper terMapper, TerLinkMapper terLinkMapper, TerDriveMapper terDriveMapper, TerScheduleMapper terScheduleMapper) {
        this.terMapper = terMapper;
        this.terLinkMapper = terLinkMapper;
        this.terDriveMapper = terDriveMapper;
        this.terScheduleMapper = terScheduleMapper;
    }

    // 출발 드롭다운 데이터 전송
    public List<TerDto> loadDepTerData() {
        List<TerLinkDto> terLinkDtos = terLinkMapper.selectAll();
        List<TerDto> depTerDtos = new ArrayList<>();
        for (TerLinkDto terLinkDto: terLinkDtos) {
            // 출발정류장 반환
            TerDto depTerDto = terMapper.getTerById(terLinkDto.getTl_DepTerId());
            depTerDtos.add(depTerDto);
        }
        return DeduplicationUtils.removeDuplicates(depTerDtos);
    }

    // 출발지 선택시 도착지 리스트 데이터 전송
    public List<TerDto> loadArrTerData(String depTerId) {
        TerDto depTerDto = terMapper.getTerById(depTerId);
        List<TerDto> terDtoList = new ArrayList<>();
        List<TerLinkDto> terLinkDtoList = terLinkMapper.getTerLinkByDepTerId(depTerDto.getTerId());
        for (TerLinkDto terLinkDto : terLinkDtoList) {
            TerDto terDto = terMapper.getTerById(terLinkDto.getTl_ArrTerId());
            terDtoList.add(terDto);
        }
        return terDtoList;
    }

    // 출발지 도착지 드롭다운에서 선택후 시간표 게시판에 데이터 전달
    public List<TerScheduleVO> loadTerDriveAndSchedule(String depTerId, String arrTerId) {
        TerLinkDto terLinkDto = terLinkMapper.getTerLinkByDepTerIdAndArrTerId(depTerId, arrTerId);
        List<TerDriveDto> terDriveDtoList = terDriveMapper.getTerDriveByTl_Id(terLinkDto.getTl_Id());
        List<TerScheduleVO> terScheduleVOList = new ArrayList<>();
        // get by 2id
        // linkter id 로 db 접근 => list로 받음
        // 반환
        for (TerDriveDto terDriveDto : terDriveDtoList) {
            List<TerScheduleDto> terScheduleDtoList = terScheduleMapper.getTerScheduleByTd_Id(terDriveDto.getTd_ID());
            TerScheduleVO terScheduleVO = new TerScheduleVO();

            terScheduleVO.setTd_ID(terDriveDto.getTd_ID());
            terScheduleVO.setTd_TlID(terDriveDto.getTd_TlID());
            terScheduleVO.setTd_WasteTime(terDriveDto.getTd_WasteTime());
            terScheduleVO.setTd_Fare(terDriveDto.getTd_Fare());
            terScheduleVO.setTd_Interval(terDriveDto.getTd_Interval());
            terScheduleVO.setTd_schedule(terScheduleDtoList);

            terScheduleVOList.add(terScheduleVO);
        }
        return terScheduleVOList;
    }
}
