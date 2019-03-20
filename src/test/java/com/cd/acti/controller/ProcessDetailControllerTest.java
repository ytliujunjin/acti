package com.cd.acti.controller;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class ProcessDetailControllerTest {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FormService formService;

    @Test
    public void userInfo() {
        //项目中每创建一个新用户，对应的要创建一个Activiti用户
        //两者的userId和userName一致
        User admin = identityService.newUser("userId_1");
        admin.setLastName("chendeng");
        admin.setPassword("test");
        identityService.saveUser(admin);

        //项目中每创建一个角色，对应的要创建一个Activiti用户组
        Group adminGroup = identityService.newGroup("groupId_1");
        adminGroup.setName("admin");
        identityService.saveGroup(adminGroup);

        //用户与用户组关系绑定
        identityService.createMembership("userId_1", "groupId_1");
    }

    @Test
    public void startFormData() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("leave_process", "1");
        System.out.println("流程启动成功，流程id:"+pi.getId());
    }

    @Test
    public void saveFormData() {
    }

    @Test
    public void getCurrentTask() {
        String userId ="shopA";
        List<Task> resultTask = taskService.createTaskQuery().processDefinitionKey("leave_process").taskCandidateOrAssigned(userId).list();
        System.out.println("任务列表："+resultTask);
    }

    @Test
    public void image() {
    }

    @Test
    public void getDiagram() {
    }
}