package ru.h562.smallsite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.h562.smallsite.dao.DBServiceDao;

@Service
public class DBServiceImpl implements ru.h562.smallsite.service.DBService {
    @Autowired
    private DBServiceDao dao;

    @Override
    public void ShutdownDB() {
        dao.ShutdownDB();
    }

    @Override
    public void ShutdownCompactDB() {
        dao.ShutdownCompactDB();
    }

}
