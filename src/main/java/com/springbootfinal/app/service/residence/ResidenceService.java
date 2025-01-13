package com.springbootfinal.app.service.residence;

import com.springbootfinal.app.domain.residence.PropertyPhotosDto;
import com.springbootfinal.app.domain.residence.ResidenceDto;
import com.springbootfinal.app.mapper.residence.PropertyPhotosMapper;
import com.springbootfinal.app.mapper.residence.ResidenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidenceService {

    @Autowired
    private final ResidenceMapper residenceMapper;

    @Autowired
    private final PropertyPhotosMapper propertyPhotosMapper;

    public ResidenceService(ResidenceMapper residenceMapper, PropertyPhotosMapper propertyPhotosMapper) {
        this.residenceMapper = residenceMapper;
        this.propertyPhotosMapper = propertyPhotosMapper;
    }



}
