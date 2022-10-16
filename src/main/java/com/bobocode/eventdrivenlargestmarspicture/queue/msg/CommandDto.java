package com.bobocode.eventdrivenlargestmarspicture.queue.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandDto {

    private Integer sol;

    private String camera;

    private String commandId;
}
