package pl.cezaryregec.message.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import pl.cezaryregec.user.models.User;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "messages")
@NamedQueries({
        @NamedQuery(name = "Message.findUndelivered", query = "SELECT m " +
                "FROM Message m " +
                "WHERE m.receiver.id = :userid " +
                "AND m.state = pl.cezaryregec.message.model.MessageState.UNDELIVERED"),
        @NamedQuery(name = "Message.findMessages", query = "SELECT m " +
                "FROM Message m " +
                "WHERE (m.receiver.id = :receiver " +
                "AND m.sender.id = :sender) OR " +
                "(m.sender.id = :receiver " +
                "AND m.receiver.id = :sender) " +
                "ORDER BY m.sentTime DESC")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public @Data class Message implements Serializable {
    private static final long serialVersionUID = -5307768275554599383L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "message")
    private String message;

    @Column(name = "key")
    private String key;

    @Column(name = "sent_time")
    private Timestamp sentTime;

    @Column(name = "message_state")
    private MessageState state;

    @Column(name = "state_change_time")
    private Timestamp stateChangeTime;

    @Column(name = "exchange_state")
    private KeyExchangeState keyExchangeState;

    @Override
    public boolean equals(Object other) {
        if (other instanceof Message) {
            Message m = (Message) other;
            return this.id == m.id;
        }

        return false;
    }
}
