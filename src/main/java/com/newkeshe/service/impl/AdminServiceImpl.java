package com.newkeshe.service.impl;

import com.newkeshe.dao.*;
import com.newkeshe.entity.*;
import com.newkeshe.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class AdminServiceImpl implements AdminService {

    @Autowired
    UserDao userDao;
    @Autowired
    ExamDao examDao;
    @Autowired
    UserExamDao userExamDao;
    @Autowired
    TaskDao taskDao;
    @Autowired
    UserTaskDao userTaskDao;

    PasswordEncoder p = new BCryptPasswordEncoder();

    @Override
    public List<Map<String,Object>> findAllUser() {
        List<User> list = userDao.findAll();
        List<Map<String,Object>> result = new ArrayList<>();
        for (User u : list){
            Map map = new BeanMap(u);
            map.put("count", userExamDao.findCountUserByUserId(u.getId()));
            result.add(map);
        }
        return result;
    }

    @Override
    public User addUser(User user) {
        if (userDao.findByPhone(user.getPhone()) == null) {
            user.setPassword(p.encode(user.getPassword()));
            userDao.save(user);
            user.setPassword("");
            return user;
        } else {
            throw new RuntimeException("电话号已存在!");
        }
    }

    @Override
    public Boolean rmUser(Integer uId) {
        Optional.ofNullable(userDao.findById(uId))
                .orElseThrow(() -> new RuntimeException("用户不存在,请检查你您的操作."));
        userDao.deleteById(uId);
        return true;
    }

    @Override
    public User modiUserInfo(User user) {
        String phone = userDao.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "发生错误"))
                .getPhone();
        if (user.getPhone().equals(phone) || userDao.findByPhone(user.getPhone()) == null) {
            user.setPassword(p.encode(user.getPassword()));
            userDao.save(user);
            user.setPassword("");
            return user;
        } else
            throw new RuntimeException("电话号已存在!");
    }

    @Override
    public Exam addExam(Exam Exam) {
        examDao.save(Exam);
        return Exam;
    }

    @Override
    public Boolean rmExam(Integer ExamId) {
        examDao.findById(ExamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到"));
        examDao.deleteById(ExamId);
        return true;
    }

    @Override
    public Exam modiExamInfo(Exam Exam) {
        examDao.save(Exam);
        return Exam;
    }

    @Override
    public UserExam setUserExam(Integer uId, Integer ExamId) {
        if (!userDao.findById(uId).isPresent() || !examDao.findById(ExamId).isPresent())
            throw new RuntimeException("参数错误");
        UserExam userExam = new UserExam();
        userExam.setUser(new User(uId));
        userExam.setExam(new Exam(ExamId));
        examDao.findById(ExamId).ifPresent(Exam -> {
            if (userExamDao.findCountExamByExamId(ExamId) >= Exam.getNumbersOfTeacher()) {
                throw new RuntimeException("分配人数超过限制");
            }
        });
        userExamDao.findByUser(new User(uId)).forEach(ui -> {
            //用户已分配的考试的开始时间和结束时间
            LocalDateTime isSetBegin = ui.getExam().getBeginTime();
            LocalDateTime isSetEnd = isSetBegin.plusHours(ui.getExam().getDuration().getHour())
                    .plusMinutes(ui.getExam().getDuration().getMinute());
            //准备分配的考试的开始和结束时间
            Exam Exam = examDao.findById(ExamId).orElseThrow(() -> new RuntimeException("发生错误"));
            LocalDateTime begin = Exam.getBeginTime();
            LocalDateTime end = begin.plusHours(Exam.getDuration().getHour())
                    .plusMinutes(Exam.getDuration().getMinute());
            if ((end.isBefore(isSetEnd) && end.isAfter(isSetBegin))
                    || (begin.isBefore(isSetEnd) && begin.isAfter(isSetBegin))
                    || (begin.isBefore(isSetBegin) && end.isAfter(isSetEnd))) {
                userExamDao.save(userExam);
                throw new RuntimeException("与时间为" + ui.getExam().getBeginTime() +
                        "的" + ui.getExam().getName() + "考试冲突");
            }
        });
        return userExamDao.save(userExam);
    }

    @Override
    public Integer countIsSetExam(Integer ExamId) {
        return userExamDao.findCountExamByExamId(ExamId);
    }

    @Override
    public Boolean rmUserExam(Integer id) {
        userExamDao.deleteById(id);
        return true;
    }

    @Override
    public Task addTask(Task task) {
        taskDao.save(task);
        return task;
    }

    @Override
    public Boolean rmTask(Integer tId) {
        taskDao.findById(tId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到任务信息"));
        taskDao.deleteById(tId);
        return true;
    }

    @Override
    public Task modiTaskInfo(Task task) {
        taskDao.save(task);
        return task;
    }

    @Override
    public Task closeTask(Integer tId) {
        taskDao.closeTask(tId);
        return taskDao.findById(tId).orElse(null);
    }

    @Override
    public boolean rmUserTask(Integer id) {
        userTaskDao.deleteById(id);
        return true;
    }
}
