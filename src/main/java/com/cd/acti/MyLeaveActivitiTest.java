package com.cd.acti;

import org.activiti.engine.*;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class MyLeaveActivitiTest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    @Transactional
    public void createTable() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
    }

    @Test
    public void deployProcess() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("processes/mybpmn.bpmn");
        builder.deploy();
    }

    @Test
    public void startProcess() {
        RuntimeService runTimeService = processEngine.getRuntimeService();
        runTimeService.startProcessInstanceByKey("myleave");
    }

    @Test
    public void queryTask() {
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("manager").list();
        int size = taskList.size();
        for (int i = 0; i < size; i++) {
            Task task = taskList.get(i);
        }
        for(Task task : taskList) {
            System.out.println("taskId:" + task.getId() +
                    ",taskName:" + task.getName() +
                    ",assignee:" + task.getAssignee() +
                    ",createTime:" + task.getCreateTime());
        }
    }

    @Test
    public void handleTask() {
        TaskService taskService = processEngine.getTaskService();
        //根据上一步生成的taskId执行任务
        String taskId = "5002";
        taskService.complete(taskId);
    }
}
