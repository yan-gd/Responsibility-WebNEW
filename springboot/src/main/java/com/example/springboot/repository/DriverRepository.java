package com.example.springboot.repository;

import com.example.springboot.entity.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DriverRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Driver> driverRowMapper = new RowMapper<Driver>() {
        @Override
        public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
            Driver driver = new Driver();
            driver.setId(rs.getInt("id"));
            driver.setDriverName(rs.getString("driver_name"));
            driver.setAge(rs.getInt("age"));
            driver.setLicensePlate(rs.getString("license_plate"));
            driver.setGender(rs.getString("gender"));
            driver.setDrivingYears(rs.getInt("driving_years"));
            driver.setFatigueStatus(rs.getString("fatigue_status"));
            driver.setBehaviorStatus(rs.getString("behavior_status"));
            driver.setEmotionStatus(rs.getString("emotion_status"));
            return driver;
        }
    };

    public List<Driver> findAll() {
        return jdbcTemplate.query("SELECT * FROM drivers_information", driverRowMapper);
    }

    public List<Driver> findByDriverNameContaining(String name) {
        return jdbcTemplate.query("SELECT * FROM drivers_information WHERE driver_name LIKE ?", driverRowMapper, "%" + name + "%");
    }

    public List<Driver> findByFatigueStatus(String fatigueStatus) {
        return jdbcTemplate.query("SELECT * FROM drivers_information WHERE fatigue_status = ?", driverRowMapper, fatigueStatus);
    }

    public List<Driver> findByBehaviorStatus(String behaviorStatus) {
        return jdbcTemplate.query("SELECT * FROM drivers_information WHERE behavior_status = ?", driverRowMapper, behaviorStatus);
    }

    public List<Driver> findByLicensePlate(String licensePlate) {
        return jdbcTemplate.query("SELECT * FROM drivers_information WHERE license_plate = ?", driverRowMapper, licensePlate);
    }

    /**
     * 根据ID查询单个驾驶员
     */
    public Driver findById(Integer id) {
        List<Driver> drivers = jdbcTemplate.query("SELECT * FROM drivers_information WHERE id = ?", driverRowMapper, id);
        return drivers.isEmpty() ? null : drivers.get(0);
    }

    /**
     * 新增驾驶员
     */
    public int insert(Driver driver) {
        String sql = "INSERT INTO drivers_information (driver_name, age, license_plate, gender, driving_years, fatigue_status, behavior_status, emotion_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
            driver.getDriverName(), 
            driver.getAge(), 
            driver.getLicensePlate(), 
            driver.getGender(), 
            driver.getDrivingYears(), 
            driver.getFatigueStatus(), 
            driver.getBehaviorStatus(), 
            driver.getEmotionStatus()
        );
    }

    /**
     * 更新驾驶员信息
     */
    public int update(Driver driver) {
        String sql = "UPDATE drivers_information SET driver_name = ?, age = ?, license_plate = ?, gender = ?, driving_years = ?, fatigue_status = ?, behavior_status = ?, emotion_status = ? WHERE id = ?";
        return jdbcTemplate.update(sql, 
            driver.getDriverName(), 
            driver.getAge(), 
            driver.getLicensePlate(), 
            driver.getGender(), 
            driver.getDrivingYears(), 
            driver.getFatigueStatus(), 
            driver.getBehaviorStatus(), 
            driver.getEmotionStatus(),
            driver.getId()
        );
    }

    /**
     * 删除驾驶员
     */
    public int deleteById(Integer id) {
        return jdbcTemplate.update("DELETE FROM drivers_information WHERE id = ?", id);
    }

    /**
     * 批量删除驾驶员
     */
    public int batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        String sql = "DELETE FROM drivers_information WHERE id IN (" + String.join(",", ids.stream().map(String::valueOf).toArray(String[]::new)) + ")";
        return jdbcTemplate.update(sql);
    }
}
