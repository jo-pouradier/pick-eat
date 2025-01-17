package fr.pick_eat.socketspring.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

/**
 * Used to convert JMS messages from/to JSON. Registered in Spring-JMS
 * automatically via auto configuration
 */
@Component
public class JsonMessageConverter implements MessageConverter {

    private final ObjectMapper mapper;
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(JsonMessageConverter.class);

    @Autowired
    public JsonMessageConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Converts message to JSON. Used mostly by
     * {@link org.springframework.jms.core.JmsTemplate}
     */
    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        log.debug("starting to convert message: " + object);
        String json;

        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new MessageConversionException("Message cannot be parsed. ", e);
        }
        log.info("Converted message to JSON: " + json);
        TextMessage message = session.createTextMessage();
        message.setText(json);
        return message;
    }

    /**
     * Extracts JSON payload for further processing by JacksonMapper.
     */
    @Override
    public Object fromMessage(Message message)
            throws JMSException, MessageConversionException {
        log.info("Received message: " + message);
        try {
            log.info(((TextMessage) message).getText());
            return ((TextMessage) message).getText();
        } catch (org.springframework.messaging.converter.MessageConversionException e) {
            log.info(e);
        }
        return "Error";
    }
}
