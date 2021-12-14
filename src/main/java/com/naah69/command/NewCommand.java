package com.naah69.command;

import com.naah69.domain.blog.NewBlog;
import com.naah69.infrastructure.config.command.CommandConfig;
import com.naah69.infrastructure.config.command.sub.NewBlogConfig;
import com.naah69.infrastructure.enums.NewBlogPrefixType;
import com.naah69.infrastructure.utils.CommandParamUtils;
import com.naah69.infrastructure.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import javax.inject.Inject;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@CommandLine.Command(name = "new", description = "create blog", mixinStandardHelpOptions = true)
public class NewCommand implements Runnable, ConfigCommand<NewBlogConfig> {

    @CommandLine.Option(names = {"-d", "--dir"}, description = "new markdown of dir", defaultValue = "")
    String dir;

    @CommandLine.Option(names = {"--date"}, description = "enable date prefix")
    boolean[] datePrefix;

    @CommandLine.Option(names = {"--date-prefix-format"}, description = "date prefix format", defaultValue = "")
    String datePrefixFormat;

    @CommandLine.Option(names = {"-t", "--text"}, description = "enable text prefix", defaultValue = "")
    String textPrefix;

    @CommandLine.Parameters(paramLabel = "<filename>", description = "the filename of file to create")
    String name;

    @Inject
    CommandConfig commandConfig;


    @Override
    public void run() {
        new NewBlog(getConfig()).createBlog(name);
    }

    @Override
    public NewBlogConfig getConfig() {
        NewBlogConfig yamlConfig = commandConfig.newBlog();
        return new NewBlogConfig() {
            @Override
            public Prefix prefix() {
                return buildPrefixConfig(yamlConfig);
            }

            @Override
            public Optional<String> dir() {
                return StringUtils.isNotBlank(dir) ? Optional.of(dir) : yamlConfig.dir();
            }
        };
    }


    private NewBlogConfig.Prefix buildPrefixConfig(NewBlogConfig yamlConfig) {
        return new NewBlogConfig.Prefix() {
            @Override
            public String sep() {
                return yamlConfig.prefix().sep();
            }

            @Override
            public DatePrefix date() {
                return buildDatePrefixConfig(yamlConfig);
            }

            @Override
            public TextPrefix text() {
                return buildTestPrefixConfig(yamlConfig);
            }

        };
    }


    private NewBlogConfig.Prefix.DatePrefix buildDatePrefixConfig(NewBlogConfig yamlConfig) {
        NewBlogConfig.Prefix.DatePrefix yamlDatePrefix = yamlConfig.prefix().date();
        return new NewBlogConfig.Prefix.DatePrefix() {
            @Override
            public String dateFormat() {
                return StringUtils.isNotBlank(datePrefixFormat) ? datePrefixFormat : yamlDatePrefix.dateFormat();
            }

            @Override
            public NewBlogPrefixType type() {
                return NewBlogPrefixType.DATE;
            }

            @Override
            public boolean enabled() {
                return datePrefixEnbale() || yamlDatePrefix.enabled();
            }
        };
    }


    private NewBlogConfig.Prefix.TextPrefix buildTestPrefixConfig(NewBlogConfig yamlConfig) {
        NewBlogConfig.Prefix.TextPrefix yamlTextPrefix = yamlConfig.prefix().text();
        return new NewBlogConfig.Prefix.TextPrefix() {
            @Override
            public Optional<String> value() {
                return StringUtils.isNotBlank(textPrefix) ? Optional.of(textPrefix) : yamlTextPrefix.value();
            }

            @Override
            public NewBlogPrefixType type() {
                return NewBlogPrefixType.TEXT;
            }

            @Override
            public boolean enabled() {
                return textPrefixEnable() || yamlTextPrefix.enabled();
            }
        };
    }

    private boolean datePrefixEnbale() {
        return CommandParamUtils.optionalEnbale(datePrefix);
    }

    private boolean textPrefixEnable() {
        return StringUtils.isNotBlank(textPrefix);
    }


}
