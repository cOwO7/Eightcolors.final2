package com.springbootfinal.app.service.mainpage;

import com.springbootfinal.app.domain.mainpage.MainPage;
import com.springbootfinal.app.mapper.mainpage.MainPageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainPageService {

    @Autowired
    private MainPageMapper mainPageMapper;

    public List<MainPage> getRandomProperties() {
        return mainPageMapper.getRandomProperties();
    }
}