package com.online.library.util;

import com.online.library.dto.EventType;
import com.online.library.dto.ObjectType;
import com.online.library.dto.WsEventDto;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.BiConsumer;

@Component
public class WsSender {
    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper;

    public WsSender(SimpMessagingTemplate template) {
        this.template = template;
        this.mapper = new ObjectMapper();
    }

    public <T> BiConsumer<EventType, T> getSender(ObjectType objectType, Class view) {
        ObjectWriter writer = mapper
                .setSerializationConfig(mapper.getSerializationConfig())
                .writerWithView(view);

        return (EventType eventType, T payload) -> {
            String value = null;

            try {
                value = writer.writeValueAsString(payload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            template.convertAndSend(
                    "/topic/activity",
                        new WsEventDto(objectType, eventType, value)
                    );
        };
    }
}
