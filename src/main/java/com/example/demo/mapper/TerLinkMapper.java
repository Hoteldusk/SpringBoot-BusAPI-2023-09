package com.example.demo.mapper;

import com.example.demo.models.TerDto;
import com.example.demo.models.TerLinkDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TerLinkMapper {
    public void insert(TerLinkDto terLinkDto);
    public void deleteAll();
    public List<TerLinkDto> selectAll();
    public List<TerLinkDto> getTerLinkByDepTerId(String depTerId);
    public TerLinkDto getTerLinkByDepTerIdAndArrTerId(String depTerId, String arrTerId);
}
