package com.naah69.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naah69.domain.blog.CompileBlog;
import com.naah69.infrastructure.config.command.CommandConfig;
import com.naah69.infrastructure.config.command.sub.CompileBlogConfig;
import com.naah69.infrastructure.utils.CommandParamUtils;
import com.naah69.infrastructure.utils.StringUtils;
import picocli.CommandLine;

import javax.inject.Inject;
import java.util.Optional;


@CommandLine.Command(name = "compile", description = "compile hugo", mixinStandardHelpOptions = true)
public class CompileCommand implements Runnable, ConfigCommand<CompileBlogConfig> {

    @CommandLine.Option(names = {"-p", "--participle"}, description = "participle")
    boolean[] participle;

    @CommandLine.Option(names = {"-t", "--theme"}, description = "theme", defaultValue = "")
    String theme;

    @CommandLine.Option(names = {"-D", "--drafts"}, description = "build drafts")
    boolean[] drafts;

    @CommandLine.Option(names = {"-m", "--markdown-dir"}, description = "markdown dir", defaultValue = "")
    String markdownDir;

    @CommandLine.Option(names = {"-a", "--algolia-json-path"}, description = "algolia json path", defaultValue = "")
    String algoliaJsonPath;


    @Inject
    CommandConfig commandConfig;

    @Inject
    ObjectMapper objectMapper;


    @Override
    public void run() {
        new CompileBlog(getConfig(), objectMapper).compileBlog();
    }


    @Override
    public CompileBlogConfig getConfig() {
        CompileBlogConfig yamlConfig = commandConfig.compile();
        return new CompileBlogConfig() {
            @Override
            public boolean drafts() {
                return CommandParamUtils.optionalEnbale(drafts) ? true : yamlConfig.drafts();
            }

            @Override
            public Optional<String> theme() {
                return StringUtils.isNotBlank(theme) ? Optional.ofNullable(theme) : yamlConfig.theme();
            }

            @Override
            public ParticipleConfig participle() {
                final ParticipleConfig participle = yamlConfig.participle();
                return new ParticipleConfig() {
                    @Override
                    public boolean enabled() {
                        return CommandParamUtils.optionalEnbale(CompileCommand.this.participle) ? true : participle.enabled();
                    }

                    @Override
                    public Optional<String> stopPath() {
                        return participle.stopPath();
                    }

                    @Override
                    public String markdownDir() {
                        return StringUtils.isNotBlank(markdownDir) ? markdownDir : participle.markdownDir();
                    }

                    @Override
                    public String algoliaJsonPath() {
                        return StringUtils.isNotBlank(algoliaJsonPath) ? algoliaJsonPath : participle.algoliaJsonPath();
                    }
                };
            }
        };
    }
}
