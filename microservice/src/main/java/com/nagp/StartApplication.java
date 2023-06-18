package com.nagp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
													 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class StartApplication {

    private final JdbcTemplate jdbcTemplate;
    private final String dbUsername;
    private final String dbPassword;
    private final String dbUrl;

    @Autowired
    public StartApplication(JdbcTemplate jdbcTemplate,
                            @Value("${spring.datasource.username}") String dbUsername,
                            @Value("${spring.datasource.password}") String dbPassword,
                            @Value("${spring.datasource.url}") String dbUrl) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbUrl = dbUrl;
    }

    @GetMapping("/records")
    public List<Map<String, Object>> getRecords() {
        String sql = "SELECT * FROM your_table";
        return jdbcTemplate.queryForList(sql);
    }

    @PostMapping("/records")
    public ResponseEntity<String> addRecord(@RequestBody Map<String, Object> record) {
        String sql = "INSERT INTO your_table (column1, column2, column3) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, record.get("column1"), record.get("column2"), record.get("column3"));
        if (rowsAffected == 1) {
            return new ResponseEntity<>("Record added successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to add record", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

}
