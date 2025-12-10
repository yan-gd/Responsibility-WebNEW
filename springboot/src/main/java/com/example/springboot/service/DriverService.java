package com.example.springboot.service;

import com.example.springboot.entity.Driver;
import com.example.springboot.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    /**
     * 获取所有司机信息
     */
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    /**
     * 根据姓名查询司机
     */
    public List<Driver> findByName(String name) {
        return driverRepository.findByDriverNameContaining(name);
    }

    /**
     * 根据车牌号查询司机
     */
    public List<Driver> findByLicensePlate(String licensePlate) {
        return driverRepository.findByLicensePlate(licensePlate);
    }

    /**
     * 根据疲劳状态查询司机
     */
    public List<Driver> findByFatigueStatus(String status) {
        return driverRepository.findByFatigueStatus(status);
    }

    /**
     * 根据驾驶行为状态查询司机
     */
    public List<Driver> findByBehaviorStatus(String status) {
        return driverRepository.findByBehaviorStatus(status);
    }

    /**
     * 生成司机数据摘要，用于 AI 提示词
     */
    public String generateDriverDataSummary() {
        List<Driver> drivers = getAllDrivers();
        
        if (drivers.isEmpty()) {
            return "当前数据库中没有司机数据。";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("【司机数据库信息摘要】\n");
        summary.append("当前系统共有 ").append(drivers.size()).append(" 名司机，详细信息如下：\n\n");

        // 统计信息
        long normalFatigue = drivers.stream().filter(d -> "正常".equals(d.getFatigueStatus())).count();
        long mildFatigue = drivers.stream().filter(d -> "轻度疲劳".equals(d.getFatigueStatus())).count();
        long moderateFatigue = drivers.stream().filter(d -> "中度疲劳".equals(d.getFatigueStatus())).count();
        long severeFatigue = drivers.stream().filter(d -> "重度疲劳".equals(d.getFatigueStatus())).count();

        summary.append("【疲劳状态统计】\n");
        summary.append("- 正常: ").append(normalFatigue).append(" 人\n");
        summary.append("- 轻度疲劳: ").append(mildFatigue).append(" 人\n");
        summary.append("- 中度疲劳: ").append(moderateFatigue).append(" 人\n");
        summary.append("- 重度疲劳: ").append(severeFatigue).append(" 人\n\n");

        // 驾驶行为统计
        long normalBehavior = drivers.stream().filter(d -> "正常驾驶".equals(d.getBehaviorStatus())).count();
        long speeding = drivers.stream().filter(d -> "超速".equals(d.getBehaviorStatus())).count();
        long hardBraking = drivers.stream().filter(d -> "急刹车".equals(d.getBehaviorStatus())).count();
        long laneChange = drivers.stream().filter(d -> "违规变道".equals(d.getBehaviorStatus())).count();

        summary.append("【驾驶行为统计】\n");
        summary.append("- 正常驾驶: ").append(normalBehavior).append(" 人\n");
        summary.append("- 超速: ").append(speeding).append(" 人\n");
        summary.append("- 急刹车: ").append(hardBraking).append(" 人\n");
        summary.append("- 违规变道: ").append(laneChange).append(" 人\n\n");

        // 司机详细列表
        summary.append("【司机详细信息】\n");
        for (Driver driver : drivers) {
            summary.append(driver.toString()).append("\n");
        }

        return summary.toString();
    }
}
