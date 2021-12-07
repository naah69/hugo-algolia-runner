package com.naah69.command;

import picocli.CommandLine;

@CommandLine.Command(name = "deploy",description = "部署博客", mixinStandardHelpOptions = true)
public class DeployCommand implements Runnable {
    @Override
    public void run() {
        //todo
    }
}
