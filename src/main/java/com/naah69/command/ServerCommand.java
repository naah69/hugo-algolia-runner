package com.naah69.command;

import picocli.CommandLine;

import javax.enterprise.inject.Any;

@CommandLine.Command(name = "server",description = "启动本地调试博客", mixinStandardHelpOptions = true)
public class ServerCommand implements Runnable {

    @Any
    @CommandLine.Option(names = {"-p","--port"},description = "端口",defaultValue = "1313")
    Integer port;

    @Override
    public void run() {
        //todo
    }
}
