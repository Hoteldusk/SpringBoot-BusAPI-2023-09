package com.example.demo.utils;

import com.example.demo.models.TerDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeduplicationUtils {
    public static List<TerDto> removeDuplicates(List<TerDto> list) {
        Set<TerDto> set = new HashSet<>();
        List<TerDto> deduplicatedList = new ArrayList<>();

        for (TerDto dto : list) {
            if (set.add(dto)) {
                deduplicatedList.add(dto);
            }
        }

        return deduplicatedList;
    }
}
