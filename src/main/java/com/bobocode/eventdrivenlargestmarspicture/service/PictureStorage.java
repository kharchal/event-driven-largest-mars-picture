package com.bobocode.eventdrivenlargestmarspicture.service;


import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;



@Service
public class PictureStorage {

    Map<String, byte[]> pictures = new ConcurrentHashMap<>();


    public void addPicture(String id, byte[] pic) {
        pictures.put(id, pic);
    }

    public Optional<byte[]> findPicture(String commandId) {
        return Optional.ofNullable(pictures.get(commandId));
    }



}