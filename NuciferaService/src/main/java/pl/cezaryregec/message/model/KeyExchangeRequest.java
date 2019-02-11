package pl.cezaryregec.message.model;

import lombok.Data;

import java.io.Serializable;

public @Data class KeyExchangeRequest implements Serializable {
    private static final long serialVersionUID = -5367815648534678414L;
    private String publicKey;
    private Long receiver;
    private KeyExchangeState state;
}
