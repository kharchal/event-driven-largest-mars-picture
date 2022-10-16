package com.bobocode.eventdrivenlargestmarspicture.queue;

import com.bobocode.eventdrivenlargestmarspicture.queue.msg.CommandDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String publishAndGetCommand(Integer sol, String cameraName) {
        String commandId = RandomStringUtils.randomAlphabetic(8);
        CommandDto commandDto = new CommandDto(sol, cameraName, commandId);
        rabbitTemplate.convertAndSend("picture-exchange", "", commandDto);
        return commandId;
    }

}
