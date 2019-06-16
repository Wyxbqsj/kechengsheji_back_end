package com.newkeshe.service;

import com.newkeshe.entity.*;

import java.util.List;

public interface UserService {
    Object login(String uPhone, String uPwd);
    Object register(User uesr);
    User findSelf(Integer id);
    User ModiPersInfo(User user);
    Object listAllExam();
    Object viewExamsUser(Integer ExamId);
    List<UserExam> viewUsersExam(Integer uId);
    UserTask setUserTask(UserTask userTask);
    List<UserTask> findSomeoneTaskInfo(Integer uId, Integer tId);
    List<Task> listAllTask();
    List<UserTask> getUserTask(Integer uId);
    List<UserTask> getTaskUser(Integer tId);
}
