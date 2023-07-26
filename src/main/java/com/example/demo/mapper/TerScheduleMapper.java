package com.example.demo.mapper;

import com.example.demo.models.TerScheduleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TerScheduleMapper {
    public void insert(TerScheduleDto terScheduleDto);
    public void deleteAll();
    public List<TerScheduleDto> getTerScheduleByTd_Id(String terDriveId);

}
