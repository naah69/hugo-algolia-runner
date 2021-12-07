package com.naah69.infrastructure.config;

import com.naah69.infrastructure.enums.NewBlogPrefixType;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

import java.util.Optional;

@ConfigMapping(prefix = "blog")
public interface CommandConfig {

    Optional<String> basicCommand();

    Optional<New> newBlog();

    interface New {

        Optional<Prefix> prefix();

        Optional<String> dir();

        interface Prefix {

            @WithDefault("false")
            boolean enabled();

            @WithDefault("DATE")
            NewBlogPrefixType prefixType();

            Optional<String> prefixStr();

            @WithDefault("yyyy-MM-dd")
            String dateFormat();

            @WithDefault("-")
            String sep();
        }
    }
}