package com.example.demo.dbTest;

import com.example.demo.mapper.TerMapper;
import com.example.demo.models.TerDto;
import com.example.demo.service.LoadDB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DBCRUD_TEST {
    private final TerMapper mapper;
    private final LoadDB loadDB;

    @Autowired
    public DBCRUD_TEST(TerMapper mapper, LoadDB loadDB) {
        this.mapper = mapper;
        this.loadDB = loadDB;
    }

    @Test
    public void insertTest() {
        TerDto dto = new TerDto();
        dto.setTerId("1");
        dto.setTerName("a");
        dto.setTerRegion("a");
        dto.setTerCoorX(1);
        dto.setTerCoorY(1);
        mapper.insert(dto);
    }

    @Test
    public void selectAllTest() {
        List<TerDto> terDtoList = mapper.selectAll();

        for (TerDto dto:
                terDtoList) {
            System.out.println(dto);
        }
    }

    @Test
    public void select_arr_terList() {
        List<TerDto> terDtoList = loadDB.loadArrTerData("3601695");
        for (TerDto terDto:
             terDtoList) {
            System.out.println(terDto);
        }
    }
    @Test
    public void select_dep_terList() {
        List<TerDto> terDtoList = loadDB.loadDepTerData();
        for (TerDto terDto:
             terDtoList) {
            System.out.println(terDto);
        }
    }
}
