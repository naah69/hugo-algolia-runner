package com.naah69.command;

import com.naah69.infrastructure.config.CommandConfig;
import com.naah69.service.NewService;
import lombok.RequiredArgsConstructor;
import picocli.CommandLine;

import java.util.Optional;

@RequiredArgsConstructor
@CommandLine.Command(name = "new", description = "创建博文", mixinStandardHelpOptions = true)
public class NewCommand implements Runnable {



    @CommandLine.Parameters(paramLabel = "<filename>", description = "文件名")
    String name;

    private final NewService newService;

    @Override
    public void run() {
        newService.createBlog(name);
    }
}
