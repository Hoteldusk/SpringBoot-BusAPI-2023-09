package com.example.demo.mapper;

import com.example.demo.models.TerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TerMapper {
    public void insert(TerDto terDto);
    public void deleteAll();
    public List<TerDto> selectAll();
    public TerDto getTerById(String terId);
    public List<TerDto> getTerByRegion(String terRegion);
}
