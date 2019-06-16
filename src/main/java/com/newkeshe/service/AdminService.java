package com.newkeshe.service;

import com.newkeshe.entity.*;

public interface AdminService {
//    用户信息管理
    Object findAllUser();
    User addUser(User user);
    Boolean rmUser(Integer uId);
    User modiUserInfo(User user);
//    监考信息管理以及监考人员管理
    Exam addExam(Exam Exam);
    Boolean rmExam(Integer ExamId);
    Exam modiExamInfo(Exam Exam);
    UserExam setUserExam(Integer uId,Integer ExamId);
    Integer countIsSetExam(Integer ExamId);
    Boolean rmUserExam(Integer id);
//    任务信息管理以及任务完成情况管理
    Task addTask(Task task);
    Boolean rmTask(Integer tId);
    Task modiTaskInfo(Task task);
    Task closeTask(Integer tId);
//    删除用户完成信息情况
    boolean rmUserTask(Integer id);
}
