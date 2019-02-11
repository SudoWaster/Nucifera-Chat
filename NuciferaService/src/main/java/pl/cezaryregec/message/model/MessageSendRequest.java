package pl.cezaryregec.message.model;

import lombok.Data;

import java.io.Serializable;

public @Data class MessageSendRequest implements Serializable {
    private String message;
    private String key;
    private Long receiver;
}
