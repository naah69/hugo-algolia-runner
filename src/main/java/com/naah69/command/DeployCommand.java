package com.naah69.command;

import picocli.CommandLine;

@CommandLine.Command(name = "deploy",description = "deploy blog", mixinStandardHelpOptions = true)
public class DeployCommand implements Runnable {
    @Override
    public void run() {
        //todo
    }
}
