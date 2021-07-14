package com.etocrm.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

@Getter
@Setter
public class EventTraceDataSourceConfiguration implements Serializable {

    private static final long serialVersionUID = -5996257770767863699L;

    private String name;

    private String driver;

    private String url;

    private String username;

    private String password;

    private boolean activated;

    public EventTraceDataSourceConfiguration(String name, String driver, String url, String username, String password, boolean activated) {
        this.name = name;
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.activated = activated;
    }
}
