package com.bobocode.eventdrivenlargestmarspicture.queue;

import com.bobocode.eventdrivenlargestmarspicture.nasaapi.NasaPicturesService;
import com.bobocode.eventdrivenlargestmarspicture.service.PictureService;
import com.bobocode.eventdrivenlargestmarspicture.queue.msg.CommandDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PictureCommandListener {

    private PictureService pictureService;

    private final NasaPicturesService nasaPicturesService;

    @RabbitListener(queues = "largest-picture-command-queue")
    public void processMessages(CommandDto commandDto) {
        System.out.println("command fetched from the queue: " + commandDto);
        byte[] picture = nasaPicturesService.findLargestPicture(commandDto.getCamera(), commandDto.getSol());
        pictureService.save(commandDto.getCommandId(), picture);
    }


}