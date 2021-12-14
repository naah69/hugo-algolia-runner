package com.naah69.infrastructure.config.command.sub;

import com.naah69.infrastructure.config.command.EnableConfig;
import com.naah69.infrastructure.config.command.SubCommandConfig;
import io.smallrye.config.WithDefault;

import java.util.Optional;

public interface CompileBlogConfig extends SubCommandConfig {


    @WithDefault("false")
    boolean drafts();

    Optional<String> theme();

    ParticipleConfig participle();

    interface ParticipleConfig extends EnableConfig {

        Optional<String> stopPath();

        @WithDefault("content")
        String markdownDir();

        @WithDefault("public/algolia.json")
        String algoliaJsonPath();

    }
}
