package com.bobocode.eventdrivenlargestmarspicture.controller;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import com.bobocode.eventdrivenlargestmarspicture.controller.dto.LargestPictureRequestDto;
import com.bobocode.eventdrivenlargestmarspicture.queue.CommandPublisher;
import com.bobocode.eventdrivenlargestmarspicture.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class NasaController {

    @Autowired
    private CommandPublisher commandPublisher;

    @Autowired
    private PictureService pictureService;

    @PostMapping("/mars/pictures/largest")
    public ResponseEntity<String> getLargestLocation(@RequestBody LargestPictureRequestDto requestDto,
                                                     HttpServletRequest request) {
        String commandId = commandPublisher.publishAndGetCommand(requestDto.getSol(), requestDto.getCamera());
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create(request.getRequestURL() + "/" + commandId))
                .build();
    }

    @GetMapping(value = "/mars/pictures/largest/{commandId}")
    public ResponseEntity<byte[]> getPicture(@PathVariable String commandId) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(pictureService.findByCommandId(commandId));
    }

}