package com.springbootfinal.app.mapper.mainpage;

import com.springbootfinal.app.domain.mainpage.MainPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainPageMapper {
    List<MainPage> getRandomProperties();
}