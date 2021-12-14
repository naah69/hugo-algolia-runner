package com.naah69.command;

import com.naah69.domain.blog.ServerBlog;
import com.naah69.infrastructure.config.command.CommandConfig;
import com.naah69.infrastructure.config.command.sub.ServerBlogConfig;
import com.naah69.infrastructure.utils.CommandParamUtils;
import com.naah69.infrastructure.utils.StringUtils;
import picocli.CommandLine;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

@CommandLine.Command(name = "server", description = "start on localhost", mixinStandardHelpOptions = true)
public class ServerCommand implements Runnable, ConfigCommand<ServerBlogConfig> {

    public static final String DEFAULT_PORT = "1313";
    @CommandLine.Option(names = {"-p", "--port"}, description = "port", defaultValue = DEFAULT_PORT)
    Integer port;

    @CommandLine.Option(names = {"-t", "--theme"}, description = "theme", defaultValue = "")
    String theme;

    @CommandLine.Option(names = {"-D", "--drafts"}, description = "build drafts")
    boolean[] drafts;

    @Inject
    CommandConfig commandConfig;


    @Override
    public void run() {
        new ServerBlog(getConfig()).serverBlog();
    }


    @Override
    public ServerBlogConfig getConfig() {
        ServerBlogConfig yamlConfig = commandConfig.server();
        return new ServerBlogConfig() {
            @Override
            public Integer port() {
                return !Objects.equals(DEFAULT_PORT, port) ? port : yamlConfig.port();
            }

            @Override
            public boolean drafts() {
                return CommandParamUtils.optionalEnbale(drafts) ? true : yamlConfig.drafts();
            }

            @Override
            public Optional<String> theme() {
                return StringUtils.isNotBlank(theme) ? Optional.ofNullable(theme) : yamlConfig.theme();
            }
        };
    }


}
