package com.example.springboot.entity;

public class Driver {

    private Integer id;
    private String driverName;
    private Integer age;
    private String licensePlate;
    private String gender;
    private Integer drivingYears;
    private String fatigueStatus;
    private String behaviorStatus;
    private String emotionStatus;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getDrivingYears() {
        return drivingYears;
    }

    public void setDrivingYears(Integer drivingYears) {
        this.drivingYears = drivingYears;
    }

    public String getFatigueStatus() {
        return fatigueStatus;
    }

    public void setFatigueStatus(String fatigueStatus) {
        this.fatigueStatus = fatigueStatus;
    }

    public String getBehaviorStatus() {
        return behaviorStatus;
    }

    public void setBehaviorStatus(String behaviorStatus) {
        this.behaviorStatus = behaviorStatus;
    }

    public String getEmotionStatus() {
        return emotionStatus;
    }

    public void setEmotionStatus(String emotionStatus) {
        this.emotionStatus = emotionStatus;
    }

    @Override
    public String toString() {
        return String.format("司机ID:%d, 姓名:%s, 年龄:%d, 车牌号:%s, 性别:%s, 驾龄:%d年, 疲劳状态:%s, 驾驶行为:%s, 情绪状态:%s",
                id, driverName, age, licensePlate, gender, drivingYears, fatigueStatus, behaviorStatus, emotionStatus);
    }
}
