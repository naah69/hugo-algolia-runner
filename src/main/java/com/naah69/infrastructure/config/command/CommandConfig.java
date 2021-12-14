package com.naah69.infrastructure.config.command;

import com.naah69.infrastructure.config.command.sub.CompileBlogConfig;
import com.naah69.infrastructure.config.command.sub.NewBlogConfig;
import com.naah69.infrastructure.config.command.sub.ServerBlogConfig;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.util.Optional;

@ConfigMapping(prefix = "blog")
public interface CommandConfig {

    Optional<String> basicCommand();

    @WithName("new")
    NewBlogConfig newBlog();

    ServerBlogConfig server();

    CompileBlogConfig compile();
}
