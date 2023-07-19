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
    public TerDto getTerLinkById(String terLinkId);
    public List<TerLinkDto> getByDepTerID(String depTerId);
    public List<TerLinkDto> getByArrTerID(String arrTerId);
}
