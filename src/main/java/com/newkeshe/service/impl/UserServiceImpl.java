package com.newkeshe.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.newkeshe.dao.*;
import com.newkeshe.entity.*;
import com.newkeshe.service.UserService;
import com.newkeshe.util.TokenService;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    UserExamDao userExamDao;
    @Autowired
    UserTaskDao userTaskDao;
    @Autowired
    TaskDao taskDao;
    @Autowired
    ExamDao examDao;

    @Autowired
    TokenService tokenService;

    PasswordEncoder p = new BCryptPasswordEncoder();

    public Object login(String uPhone, String uPwd) {
        User user = Optional.ofNullable(userDao.findByPhone(uPhone))
                .orElseThrow(() -> new RuntimeException("用户不存在!"));
        if (p.matches(uPwd, user.getPassword())) {
            JSONObject jsonObject = new JSONObject();
            user.setPassword("");
            jsonObject.put("uInfo", user);
            Map<String, String> map = new HashMap<>();
            map.put("uId", user.getId().toString());
            map.put("uPerm", user.getAid().toString());
            jsonObject.put("token", tokenService.encrypt(map));
            return jsonObject;
        } else {
            throw new RuntimeException("密码错误!");
        }
    }

    public Object register(User user) {
        if (userDao.findByPhone(user.getPhone()) == null) {
            user.setPassword(p.encode(user.getPassword()));
            userDao.save(user);
            return user;
        } else {
            throw new RuntimeException("电话号已存在!");
        }
    }

    @Override
    public User findSelf(Integer id) {
        return userDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到用户信息"));
    }

    public User ModiPersInfo(User user) {
        String phone = userDao.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到用户信息"))
                .getPhone();
        if (userDao.findByPhone(user.getPhone()) == null || user.getPhone().equals(phone)) {
            user.setPassword(p.encode(user.getPassword()));
            userDao.save(user);
            user.setPassword("");
            return user;
        } else
            throw new RuntimeException("电话号已存在!");
    }

    @Override
    public Object listAllExam() {
        List<Map> result = new ArrayList<>();
        List<Exam> list = examDao.findAll();
        for (Exam exam : list) {
            Map map = new BeanMap(exam);
            map.put("count", userExamDao.findCountExamByExamId(exam.getId()));
            result.add(map);
        }
        return result;
    }

    @Override
    public Object viewExamsUser(Integer examId) {
        return Optional.ofNullable(userExamDao.findByExam(new Exam(examId))).orElse(new ArrayList<>());
    }

    @Override
    public List<UserExam> viewUsersExam(Integer uId) {
        return Optional.ofNullable(userExamDao.findByUser(new User(uId))).orElse(new ArrayList<>());
    }

    @Override
    public UserTask setUserTask(UserTask userTask) {
        LocalDateTime ddl = taskDao.findById(userTask.getTask().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getDdl();
        LocalDateTime now = LocalDateTime.now();
        userTask.setTimeOut(now.isAfter(ddl));
        return userTaskDao.save(userTask);
    }

    @Override
    public List<UserTask> findSomeoneTaskInfo(Integer uId, Integer tId) {
        return userTaskDao.findByUserAndTask(new User(uId), new Task(tId));
    }

    @Override
    public List<Task> listAllTask() {
        return taskDao.findAll();
    }

    @Override
    public List<UserTask> getUserTask(Integer uId) {
        return userTaskDao.findByUser(new User(uId));
    }

    @Override
    public List<UserTask> getTaskUser(Integer tId) {
        return userTaskDao.findByTask(new Task(tId));
    }
}
