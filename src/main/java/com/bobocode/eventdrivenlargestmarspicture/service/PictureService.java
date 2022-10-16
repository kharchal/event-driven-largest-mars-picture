package com.bobocode.eventdrivenlargestmarspicture.service;

import com.bobocode.eventdrivenlargestmarspicture.exception.PictureException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PictureService {

    private final PictureStorage storage;

    public byte[] findByCommandId(String commandId) {
        return storage.findPicture(commandId)
                .orElseThrow(() -> new PictureException("No pictures found"));
    }

    public void save(String commandId, byte[] picture) {
        storage.addPicture(commandId, picture);
    }
}
