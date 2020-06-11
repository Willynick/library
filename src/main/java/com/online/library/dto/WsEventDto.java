package com.online.library.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.online.library.domain.Views;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonView(Views.Id.class)
public class WsEventDto {
    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue
    private String body;

    public WsEventDto(ObjectType objectType, EventType eventType, String body) {
        this.objectType = objectType;
        this.eventType = eventType;
        this.body = body;
    }
}
