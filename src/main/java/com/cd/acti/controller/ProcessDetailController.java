package com.cd.acti.controller;

import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacation");
        //流程实例启动后，流程会跳转到请假申请节点
        Task vacationApply = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        //设置请假申请任务的执行人
        taskService.setAssignee(vacationApply.getId(), userId);

        //设置流程参数：请假天数和表单ID
        //流程引擎会根据请假天数days>3判断流程走向
        //formId是用来将流程数据和表单数据关联起来
        Map<String, Object> args = new HashMap<>();
        args.put("days", days);
        args.put("formId", formId);

        //完成请假申请任务
        taskService.complete(vacationApply.getId(), args);

        return "";
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

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void image(HttpServletResponse response,
                      @RequestParam String processInstanceId) {
        try {
            InputStream is = getDiagram(processInstanceId);
            if (is == null)
            {
                log.error("为空");
                return;
            }

            response.setContentType("image/png");

            BufferedImage image = ImageIO.read(is);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "png", out);

            is.close();
            out.close();
        } catch (Exception ex) {
            log.error("查看流程图失败", ex);
        }
    }

    public InputStream getDiagram(String processInstanceId) {
        /*//获得流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = StringUtils.EMPTY;
        if (processInstance == null) {
            //查询已经结束的流程实例
            HistoricProcessInstance processInstanceHistory =
                    historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(processInstanceId).singleResult();
            if (processInstanceHistory == null) {
                log.error("1为空");
                return null;
            } else {
                processDefinitionId = processInstanceHistory.getProcessDefinitionId();
            }
        } else {
            log.error("2为空");
            processDefinitionId = processInstance.getProcessDefinitionId();
        }

        //使用宋体
        String fontName = "宋体";
        //获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        //获取流程实例当前的节点，需要高亮显示
        List<String> currentActs = Collections.EMPTY_LIST;
        if (processInstance != null)
            currentActs = runtimeService.getActiveActivityIds(processInstance.getId());

        return processEngine.getProcessEngineConfiguration()
                .getProcessDiagramGenerator()
                .generateDiagram(model, "png", currentActs, new ArrayList<String>(),
                        fontName, fontName, fontName, null, 1.0);*/
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        InputStream inputStream = processEngine.getRepositoryService()
                .getProcessModel(processInstanceId);
        return inputStream;
    }

}
