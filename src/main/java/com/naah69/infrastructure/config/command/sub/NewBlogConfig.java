package com.naah69.infrastructure.config.command.sub;

import com.naah69.infrastructure.config.command.EnableConfig;
import com.naah69.infrastructure.config.command.SubCommandConfig;
import com.naah69.infrastructure.enums.NewBlogPrefixType;
import io.smallrye.config.WithDefault;

import java.util.Optional;

public interface NewBlogConfig extends SubCommandConfig {

    Prefix prefix();

    Optional<String> dir();

    interface Prefix {

        @WithDefault("-")
        String sep();

        DatePrefix date();

        TextPrefix text();


        interface DatePrefix extends BasePrefix {
            @WithDefault("yyyy-MM-dd")
            String dateFormat();

            @Override
            @WithDefault("DATE")
            NewBlogPrefixType type();

        }

        interface TextPrefix extends BasePrefix {
            Optional<String> value();

            @Override
            @WithDefault("TEXT")
            NewBlogPrefixType type();
        }

        interface BasePrefix extends EnableConfig {

            NewBlogPrefixType type();

        }


    }
}