package com.chaosopher.tigerlang.web.backend.source;
import org.springframework.data.annotation.Id;

public class Source {
    
    @Id
    private String id;
    private String tiger;
    private String name;
    private String description;

    public Source() {
    }

    public Source(String id, String tiger, String name, String description) {
        this.id = id;
        this.tiger = tiger;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTiger() {
        return tiger;
    }

    public void setTiger(String tiger) {
        this.tiger = tiger;
    }

    public void setId(String id) {
        this.id = id;
    }

}