package com.example.demo.mapper;

import com.example.demo.models.TerDriveDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TerDriveMapper {
    public void insert(TerDriveDto terDriveDto);
    public void deleteAll();
    public List<TerDriveDto> selectAll();
    public List<TerDriveDto> getTerDriveByTl_Id(String terLinkId);
}
