package com.skapral.parrot.accounting.events;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skapral.parrot.accounting.ops.ChargeFeeForTaskAssignment;
import com.skapral.parrot.accounting.ops.EstimateNewTask;
import com.skapral.parrot.accounting.ops.MakeAccount;
import com.skapral.parrot.accounting.ops.PayForTaskCompletion;
import com.skapral.parrot.common.Operation;
import com.skapral.parrot.common.events.EventType;
import com.skapral.parrot.common.events.EventsConfig;
import com.skapral.parrot.common.events.data.Task;
import com.skapral.parrot.common.events.data.TaskAssignment;
import com.skapral.parrot.common.events.data.TaskAssignments;
import com.skapral.parrot.common.events.data.User;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Configuration
@Import(EventsConfig.class)
public class EventsInbox {
    private static final Logger log = LoggerFactory.getLogger(EventsInbox.class);

    private final JdbcTemplate template;
    private final ObjectMapper objectMapper;
    private final Random random;

    @Autowired
    public EventsInbox(JdbcTemplate template, ObjectMapper objectMapper, Random random) {
        this.template = template;
        this.objectMapper = objectMapper;
        this.random = random;
    }

    @RabbitListener(queues = "${amqp.inbox}")
    @Transactional
    public void listen(Message message) {
        log.info("appId = " + message.getMessageProperties().getAppId());
        log.info("correlationId = " + message.getMessageProperties().getCorrelationId());
        log.info("type = " + message.getMessageProperties().getType());
        var type = EventType.valueOf(message.getMessageProperties().getType());
        try {
            switch (type) {
                case USER_NEW: {
                    var user = objectMapper.readValue(message.getBody(), User.class);
                    new MakeAccount(template, user.getId()).execute();
                    break;
                }
                case TASK_NEW: {
                    var task = objectMapper.readValue(message.getBody(), Task.class);
                    new EstimateNewTask(template, task.getId(), random).execute();
                    break;
                }
                case TASKS_REASSIGNED: {
                    var taskAssignments = objectMapper.readValue(message.getBody(), TaskAssignments.class);
                    taskAssignments.getList().map(
                            ta -> new ChargeFeeForTaskAssignment(template, ta.getTaskId(), ta.getAssigneeId())
                    ).forEach(Operation::execute);
                    break;
                }
                case TASK_COMPLETED: {
                    var taskAssignments = objectMapper.readValue(message.getBody(), TaskAssignments.class);
                    taskAssignments.getList().map(
                        ta -> new PayForTaskCompletion(template, ta.getTaskId(), ta.getAssigneeId())
                    ).forEach(Operation::execute);
                    break;
                }
                default:
                    log.warn("Received unrecognized message: " + message);
            }
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
