package com.naah69.infrastructure.model;


import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.util.List;

/**
 * Algolia
 *
 * @author naah
 * @date 2018-09-01 下午11:34
 * @desc
 */
@Data
@RegisterForReflection
public class Algolia {

    private String title;
    private String uri;
    private String content;
    private String objectID;
    private String subtitle;
    private String description;
    private String date;
    private String author;
    private String image;
    private List<String> tags;
    private List<String> categories;

}
