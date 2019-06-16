package com.newkeshe.controller;

import com.newkeshe.entity.Exam;
import com.newkeshe.entity.Task;
import com.newkeshe.entity.User;
import com.newkeshe.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/users")
    public Object getUsers() {
        return adminService.findAllUser();
    }

    @PostMapping("/user")
    public Object addUser(@RequestBody User user) {
        log.info(user.toString());
        return adminService.addUser(user);
    }

    @DeleteMapping("/user/{id}")
    public Object rmUser(@PathVariable Integer id) {
        return adminService.rmUser(id);
    }

    @PatchMapping("/user/{id}")
    public Object modiUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        return adminService.modiUserInfo(user);
    }

    @PostMapping("/exam")
    public Object addExam(@RequestBody Exam exam) {
        return adminService.addExam(exam);
    }

    @DeleteMapping("/exam/{id}")
    public Object rmExam(@PathVariable Integer id) {
        return adminService.rmExam(id);
    }

    @PatchMapping("/exam/{id}")
    public Object modiExam(@PathVariable Integer id, @RequestBody Exam exam) {
        exam.setId(id);
        return adminService.modiExamInfo(exam);
    }

    @GetMapping("/user_exam/{id}/countIsSetExams")
    public Integer countIsSetExam(@PathVariable Integer id){
        return adminService.countIsSetExam(id);
    }
    @PostMapping("/user_exam")
    public Object setUserExam(@RequestBody Map map) {
        return adminService.setUserExam(
                Integer.valueOf(map.get("uId").toString()),
                Integer.valueOf(map.get("examId").toString()));
    }

    @DeleteMapping("/user_exam/{id}")
    public Object rmUserExam(@PathVariable Integer id) {
        return adminService.rmUserExam(id);

    }

    @PostMapping("/task")
    public Object addTask(@RequestBody Task task) {
        return adminService.addTask(task);
    }

    @DeleteMapping("/task/{id}")
    public Object rmTask(@PathVariable Integer id) {
        return adminService.rmTask(id);
    }

    @PatchMapping("/task/{id}")
    public Object modiTask(@PathVariable Integer id, @RequestBody Task task) {
        task.setId(id);
        return adminService.modiTaskInfo(task);
    }

    @GetMapping("/task/{id}/close")
    public Object closeTask(@PathVariable Integer id) {
        return adminService.closeTask(id);
    }

    @DeleteMapping("/user_task/{id}")
    public Object rmUserTask(@PathVariable Integer id) {
        return adminService.rmUserTask(id);
    }

}
