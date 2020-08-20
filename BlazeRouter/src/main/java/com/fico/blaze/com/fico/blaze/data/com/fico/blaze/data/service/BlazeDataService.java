package com.fico.blaze.com.fico.blaze.data.com.fico.blaze.data.service;

import com.fico.blaze.com.fico.blaze.data.com.fico.blaze.data.repository.BlazeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BlazeDataService implements BlazeDataRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insertBlazeData(String appID, String blazeInput, String blazeOutput) {

        String insertBlazeSQL = "INSERT INTO blaze_data VALUES (NULL, ?, ?, ?);";

        return jdbcTemplate.update(insertBlazeSQL, appID, blazeInput, blazeOutput);
    }

}
