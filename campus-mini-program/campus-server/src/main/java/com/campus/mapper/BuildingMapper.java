package com.campus.mapper;

import com.campus.entity.Building;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BuildingMapper {

    Building getById(@Param("buildingId") Long buildingId);

    List<Building> listAll();

    void insert(Building building);

    void update(Building building);

    void deleteById(@Param("buildingId") Long buildingId);
}
