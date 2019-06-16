package com.newkeshe.controller;

import com.newkeshe.entity.User;
import com.newkeshe.entity.UserTask;
import com.newkeshe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public Object Register(@RequestBody User user) {
        user.setAid(0);
        System.out.println(user.getAid());
        return userService.register(user);
    }

    @PostMapping("/login")
    public Object Login(@RequestBody Map map) {
        return userService.login(map.get("phone").toString(), map.get("password").toString());
    }

    @PatchMapping("/user/{userid}/modidfyUserInfo")
    public Object ModiUserInfo(@PathVariable Integer userid,
                               @RequestBody User user,
                               HttpServletRequest request) {
        user.setId(Integer.valueOf(request.getAttribute("uId").toString()));
        user.setAid(Integer.valueOf(request.getAttribute("uPerm").toString()));
        return userService.ModiPersInfo(user);
    }

    @GetMapping("/exams")
    public Object getExams() {
        return userService.listAllExam();
    }

    @GetMapping("/tasks")
    public Object getTasks() {
        return userService.listAllTask();
    }

    @GetMapping("/me")
    public Object getMyInfo(HttpServletRequest request) {
        return userService.findSelf(Integer.valueOf(request.getAttribute("uId").toString()));
    }

    @GetMapping("/findUserByExamId")
    public Object getExamsByUserId(@RequestParam Integer id) {
        return userService.viewExamsUser(id);
    }

    @GetMapping("/findExamByUser")
    public Object getUserByExam(@RequestParam Integer id) {
        return userService.viewUsersExam(id);
    }

    @PostMapping("/submitTask")
    public Object submitTask(@RequestBody UserTask userTask) {
        return userService.setUserTask(userTask);
    }

    @GetMapping("/getUserTask")
    public Object getUserTask(@RequestParam Integer id) {
        return userService.getUserTask(id);
    }

    @GetMapping("/getTaskUser")
    public Object getTaskUser(@RequestParam Integer id) {
        return userService.getTaskUser(id);
    }

    @GetMapping("/findTaskInfoByUserIdAndTaskId")
    public Object findTaskInfoByUserIdAndTaskId(@RequestParam Integer uId, @RequestParam Integer tId) {
        return userService.findSomeoneTaskInfo(uId, tId);
    }
}
