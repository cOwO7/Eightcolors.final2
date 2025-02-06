package com.springbootfinal.app.mapper.mainpage;

import org.apache.ibatis.annotations.Mapper;
import com.springbootfinal.app.domain.mainpage.MainPage;
import java.util.List;

@Mapper
public interface MainPageMapper {
    List<MainPage> getRandomProperties();
}