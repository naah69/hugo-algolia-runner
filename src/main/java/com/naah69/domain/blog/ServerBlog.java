package com.naah69.domain.blog;

import com.naah69.domain.Hugo;
import com.naah69.infrastructure.config.command.sub.ServerBlogConfig;
import com.naah69.infrastructure.constant.ServerConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ServerBlog {

    public final ServerBlogConfig serverConfig;

    public void serverBlog() {
        Hugo hugo = Hugo.create(ServerConstant.COMMAND);
        buildPortParam(hugo);
        buildDraftsParam(hugo);
        buildThemeParam(hugo);
        hugo.run();
    }

    private void buildThemeParam(Hugo hugo) {
        serverConfig.theme().ifPresent(t -> {
            hugo.add("-t", t);
        });
    }

    private void buildDraftsParam(Hugo hugo) {
        if (serverConfig.drafts()) {
            hugo.add("-D");
        }

    }

    private void buildPortParam(Hugo hugo) {
        hugo.add("-p", serverConfig.port().toString());
    }

}
