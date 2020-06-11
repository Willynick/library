package com.online.library.util;

import java.util.Map;

public class GoogleUserInfo {

    private Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getGender() {
        return (String) attributes.get("gender");
    }

    public String getLocale() {
        return (String) attributes.get("locale");
    }

    public String getPicture() {
        return (String) attributes.get("picture");
    }
}
