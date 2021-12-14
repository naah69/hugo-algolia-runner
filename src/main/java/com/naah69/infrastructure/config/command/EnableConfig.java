package com.naah69.infrastructure.config.command;

import io.smallrye.config.WithDefault;

public interface EnableConfig {
    @WithDefault("false")
    boolean enabled();
}