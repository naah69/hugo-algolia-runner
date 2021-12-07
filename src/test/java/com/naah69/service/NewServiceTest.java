package com.naah69.service;

import com.naah69.infrastructure.config.CommandConfig;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.ConfigValue;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
@TestProfile(NewCommandProfile.DatePrefix.class)
class NewServiceTest {

    @Inject
    NewService newService;

    @Inject
    CommandConfig commandConfig;

    @Test
    void createBlog() {
    }

    @Test
    void generateFileName() {
    }

    @Test
    void fixFilename() {
    }

    @Test
    void generateDir() {
    }

    @Test
    void generatePrefix() {
        ConfigValue configValue = ConfigProvider.getConfig().getConfigValue("blog.new-blog.prefix.enabled");
        String s = newService.generatePrefix(commandConfig.newBlog().map(c -> c.prefix().orElse(null)));
        System.out.println(s);
    }
}