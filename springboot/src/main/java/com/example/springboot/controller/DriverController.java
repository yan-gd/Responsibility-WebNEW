package com.example.springboot.controller;

import com.example.springboot.entity.Driver;
import com.example.springboot.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/driver")
@CrossOrigin(origins = "*")
public class DriverController {

    @Autowired
    private DriverService driverService;

    /**
     * 获取所有驾驶员列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllDrivers() {
        try {
            List<Driver> drivers = driverService.getAllDrivers();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", drivers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "获取驾驶员列表失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 根据姓名搜索驾驶员
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchDrivers(@RequestParam String name) {
        try {
            List<Driver> drivers = driverService.findByName(name);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", drivers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "搜索驾驶员失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 根据ID获取单个驾驶员
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDriverById(@PathVariable Integer id) {
        try {
            Driver driver = driverService.findById(id);
            Map<String, Object> response = new HashMap<>();
            if (driver != null) {
                response.put("code", 200);
                response.put("message", "success");
                response.put("data", driver);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "驾驶员不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "获取驾驶员信息失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 新增驾驶员
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addDriver(@RequestBody Driver driver) {
        try {
            int result = driverService.addDriver(driver);
            Map<String, Object> response = new HashMap<>();
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "驾驶员添加成功");
                response.put("data", driver);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 500);
                response.put("message", "驾驶员添加失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "驾驶员添加失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 更新驾驶员信息
     */
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateDriver(@RequestBody Driver driver) {
        try {
            int result = driverService.updateDriver(driver);
            Map<String, Object> response = new HashMap<>();
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "驾驶员信息更新成功");
                response.put("data", driver);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "驾驶员不存在或更新失败");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "驾驶员信息更新失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 删除驾驶员
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDriver(@PathVariable Integer id) {
        try {
            int result = driverService.deleteDriver(id);
            Map<String, Object> response = new HashMap<>();
            if (result > 0) {
                response.put("code", 200);
                response.put("message", "驾驶员删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "驾驶员不存在或删除失败");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "驾驶员删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 批量删除驾驶员
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteDrivers(@RequestBody List<Integer> ids) {
        try {
            int result = driverService.batchDeleteDrivers(ids);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "成功删除" + result + "个驾驶员");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "批量删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
