package com.sonny.franchise_app.franchise.mapper;

import com.sonny.franchise_app.franchise.dto.FranchiseDto;
import com.sonny.franchise_app.franchise.entity.Franchise;
import org.springframework.beans.BeanUtils;

public class FranchiseMapper {

    public static Franchise toEntity(FranchiseDto franchiseDto) {

        Franchise franchise = new Franchise();
        BeanUtils.copyProperties(franchiseDto, franchise);

        return franchise;

    }

    public static FranchiseDto toDto(Franchise franchise) {
        FranchiseDto franchiseDto = new FranchiseDto();
        BeanUtils.copyProperties(franchise, franchiseDto);

        return franchiseDto;
    }
}
