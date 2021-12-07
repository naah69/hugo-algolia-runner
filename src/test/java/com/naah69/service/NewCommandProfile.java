package com.naah69.service;

import com.google.common.collect.Maps;
import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class NewCommandProfile {

    public static class DatePrefix implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("blog.new.prefix.enabled", "true");
        }

        @Override
        public String getConfigProfile() {
            return "test";
        }
    }
}
