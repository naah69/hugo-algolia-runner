package com.naah69.service;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class NewCommandProfile {

    public static class DatePrefixProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("blog.new.prefix.date.enabled", "true",
                    "blog.new.prefix.text.enabled", "true",
                    "blog.new.prefix.text.value", "asd",
                    "blog.new.prefix.sep", "=",
                    "blog.new.dir","post/");
//            return new HashMap<>();
        }

        @Override
        public String getConfigProfile() {
            return "test";
        }
    }
}
