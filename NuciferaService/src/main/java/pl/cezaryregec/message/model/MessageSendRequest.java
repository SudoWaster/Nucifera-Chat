package pl.cezaryregec.message.model;

import lombok.Data;

import java.io.Serializable;

public @Data class MessageSendRequest implements Serializable {
    private static final long serialVersionUID = -5018391977028891919L;
    private String message;
    private String key;
    private Long receiver;
}
