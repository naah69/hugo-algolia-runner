package com.naah69.infrastructure.config.command.sub;

import com.naah69.infrastructure.config.command.SubCommandConfig;
import io.smallrye.config.WithDefault;

import java.util.Optional;

public interface ServerBlogConfig extends SubCommandConfig {

    @WithDefault("1313")
    Integer port();

    @WithDefault("false")
    boolean drafts();

    Optional<String> theme();

}
