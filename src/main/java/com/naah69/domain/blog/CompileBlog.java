package com.naah69.domain.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naah69.domain.Hugo;
import com.naah69.domain.Participle;
import com.naah69.infrastructure.config.command.sub.CompileBlogConfig;
import com.naah69.infrastructure.constant.BaseConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CompileBlog {

    private final CompileBlogConfig compilelConfig;
    private final ObjectMapper objectMapper;

    public void compileBlog() {
        Hugo hugo = Hugo.create(BaseConstant.NULL_STR);
        buildDraftsParam(hugo);
        buildThemeParam(hugo);
        buildParticipleListener(hugo);
        hugo.run();
    }


    private void buildThemeParam(Hugo hugo) {
        compilelConfig.theme().ifPresent(t -> {
            hugo.add("-t", t);
        });
    }

    private void buildDraftsParam(Hugo hugo) {
        if (compilelConfig.drafts()) {
            hugo.add("-D");
        }
    }

    private void buildParticipleListener(Hugo hugo) {
        if (compilelConfig.participle().enabled()) {
            hugo.setCommandConfig(compilelConfig);
            hugo.addListener(new Hugo.HugoListener() {
                @Override
                public void beforeRun(Hugo hugo) {

                }

                @Override
                public void afterRun(Hugo hugo) {
                    new Participle(compilelConfig.participle(),objectMapper).participle();
                }
            });
        }
    }


}
