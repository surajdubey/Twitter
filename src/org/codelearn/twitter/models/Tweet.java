package org.codelearn.twitter.models;
import java.io.Serializable;

public class Tweet implements Serializable {
    private String id;
    private String title;
    private String body;
    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}