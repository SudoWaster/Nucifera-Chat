package pl.cezaryregec.message;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import pl.cezaryregec.auth.session.IdentityService;
import pl.cezaryregec.message.model.*;
import pl.cezaryregec.user.models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.ForbiddenException;
import java.sql.Timestamp;
import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService {
    private final IdentityService identityService;
    private final Clock clock;
    private final Provider<EntityManager> entityManagerProvider;

    @Inject
    public MessageService(IdentityService identityService, Clock clock, Provider<EntityManager> entityManagerProvider) {
        this.identityService = identityService;
        this.clock = clock;
        this.entityManagerProvider = entityManagerProvider;
    }

    @Transactional
    public void postMessage(MessageSendRequest messageSendRequest) {
        Message message = createMessage(messageSendRequest.getReceiver());
        message.setMessage(messageSendRequest.getMessage());
        message.setKey(messageSendRequest.getKey());
        message.setKeyExchangeState(null);

        entityManagerProvider.get().persist(message);
    }

    @Transactional
    public void postKeyExchange(KeyExchangeRequest keyExchangeRequest) {
        Message message = createMessage(keyExchangeRequest.getReceiver());
        message.setKey(keyExchangeRequest.getPublicKey());
        message.setKeyExchangeState(keyExchangeRequest.getState());

        entityManagerProvider.get().persist(message);
    }

    @Transactional
    private Message createMessage(Long receiverId) {
        User sender = identityService.getBoundUser().orElseThrow(ForbiddenException::new);
        List<User> receivers = sender
                .getContacts()
                .stream()
                .filter(user -> user.getId() == receiverId)
                .collect(Collectors.toList());
        if (receivers.isEmpty()) {
            throw new ForbiddenException("Receiver not in contacts");
        }

        Message message = new Message();
        Timestamp currentTime = new Timestamp(clock.millis());
        message.setSender(sender);
        message.setReceiver(receivers.get(0));
        message.setSentTime(currentTime);
        message.setState(MessageState.UNDELIVERED);
        message.setStateChangeTime(currentTime);

        return message;
    }

    @Transactional
    public List<Message> getMessages() {
        User user = identityService.getBoundUser().orElseThrow(ForbiddenException::new);
        TypedQuery<Message> namedQuery = entityManagerProvider.get().createNamedQuery("Message.findUndelivered", Message.class);
        namedQuery.setParameter("userid", user.getId());
        List<Message> resultList = namedQuery.getResultList();

        return getMessagesList(resultList, MessageState.DELIVERED);
    }

    @Transactional
    public List<Message> getLastMessages(Long receiverId, int part) {
        int start = 20 * part;
        int end = 20 * (part + 1);

        User user = identityService.getBoundUser().orElseThrow(ForbiddenException::new);
        TypedQuery<Message> namedQuery = entityManagerProvider.get().createNamedQuery("Message.findMessages", Message.class);
        namedQuery.setParameter("sender", receiverId);
        namedQuery.setParameter("receiver", user.getId());
        namedQuery.setFirstResult(start);
        namedQuery.setMaxResults(end);
        List<Message> resultList = namedQuery.getResultList();

        deleteKeys(resultList);
        return getMessagesList(resultList, MessageState.READ);
    }

    @Transactional
    private List<Message> getMessagesList(List<Message> resultList, MessageState messageState) {
        User user = identityService.getBoundUser().orElseThrow(ForbiddenException::new);
        Timestamp timestamp = new Timestamp(clock.millis());
        List<Message> resultListUnchanged = resultList
                .stream()
                .filter(message -> message.getState() != messageState && message.getReceiver().getId() == user.getId())
                .collect(Collectors.toList());

        for (Message message : resultListUnchanged) {
            message.setState(messageState);
            message.setStateChangeTime(timestamp);
            entityManagerProvider.get().merge(message);
        }

        return resultList;
    }

    @Transactional
    private void deleteKeys(List<Message> resultList) {
        User user = identityService.getBoundUser().orElseThrow(ForbiddenException::new);
        resultList
                .stream()
                .filter(message -> message.getReceiver().getId() == user.getId() && (message.getKeyExchangeState() == KeyExchangeState.EXCHANGE_REQUESTED || message.getKeyExchangeState() == KeyExchangeState.EXCHANGE_FINISH))
                .forEach(message -> {
                    entityManagerProvider.get().remove(message);
                });
    }
}
