package com.cd.acti.controller;

import com.cd.acti.model.FormDataWithBLOBs;
import com.cd.acti.service.FormDataService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ProcessDetailController {
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

    @Autowired
    private FormDataService formDataService;

    @RequestMapping("/userInfo")
    public String userInfo(HttpServletRequest request) {
        //项目中每创建一个新用户，对应的要创建一个Activiti用户
        //两者的userId和userName一致
        User admin = identityService.newUser("userId_3");
        admin.setLastName("chendeng3");
        admin.setPassword("test");
        identityService.saveUser(admin);

        //项目中每创建一个角色，对应的要创建一个Activiti用户组
      /*  Group adminGroup = identityService.newGroup("groupId_1");
        adminGroup.setName("admin");
        identityService.saveGroup(adminGroup);*/

        //用户与用户组关系绑定
        identityService.createMembership("userId_3", "groupId_1");
        return "";
    }

    @RequestMapping("/startFormData")
    public String startFormData(HttpServletRequest req, String userId, Integer days, String formId) {
        //启动流程实例，字符串"vacation"是BPMN模型文件里process元素的id
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave_process");
        //流程实例启动后，流程会跳转到请假申请节点
        Task vacationApply = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        log.info(vacationApply.toString());

        TaskFormData taskFormData = formService.getTaskFormData("5016");
        log.info(taskFormData.getFormKey());
        log.info(taskFormData.getFormProperties().toString());
        Model model = repositoryService.getModel("2537");
        log.info(model.getName());

        return vacationApply.toString();
    }

    @RequestMapping("/saveFormData")
    public String saveFormData(HttpServletRequest request, String userId, int pageNum, int pageSize) {
        //查出当前登录用户所在的用户组
        List<Group> groups = identityService.createGroupQuery()
                .groupMember(userId).list();
        List<String> groupNames = groups.stream()
                .map(group -> group.getName()).collect(Collectors.toList());

        //查询用户组的待审批请假流程列表
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey("vacation")
                .taskCandidateGroupIn(groupNames)
                .listPage(pageNum - 1, pageSize);

        //根据流程实例ID查询请假申请表单数据
        List<String> processInstanceIds = tasks.stream()
                .map(task -> task.getProcessInstanceId())
                .collect(Collectors.toList());
        /*List<User> vacationApplyList =
                vacationRepository.getVacationApplyList(processInstanceIds);*/
        return "";
    }

    @RequestMapping("/getCurrentTask")
    public String getCurrentTask(String taskId, int auditResult, String auditId, String userId) {
        //查询当前审批节点
        Task vacationAudit = taskService.createTaskQuery()
                .taskId(taskId).singleResult();

        if (auditResult == 1) {//审批通过
            //设置流程参数：审批ID
            Map<String, Object> args = new HashMap<>();
            args.put("auditId", auditId);

            //设置审批任务的执行人
            taskService.claim(vacationAudit.getId(), userId);
            //完成审批任务
            taskService.complete(vacationAudit.getId(), args);
        } else {
            //审批不通过，结束流程
            runtimeService.deleteProcessInstance(vacationAudit.getProcessInstanceId(), auditId);
        }
        return "";
    }

    @RequestMapping("/getFormProperties")
    public String getFormProperties(String formKey) {
        String formProperties = getFormPropertiesByFormKey(formKey);
        log.info(formProperties);
        return formProperties;
    }

    private String getFormPropertiesByFormKey(String formKey) {
        FormDataWithBLOBs formData = formDataService.findByFormKey(formKey);
        return formData.getModelEditorJson();
    }


}
