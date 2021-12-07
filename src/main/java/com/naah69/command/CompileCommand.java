package com.naah69.command;

import picocli.CommandLine;


@CommandLine.Command(name = "compile", description = "编译博客", mixinStandardHelpOptions = true)
public class CompileCommand implements Runnable {

    @CommandLine.Option(names = {"-t", "--template"}, description = "模板",defaultValue = "none")
    String template;

    @CommandLine.Option(names = {"-p","--participle"},description = "开启分词",defaultValue = "true")
    Boolean participle;

    @Override
    public void run() {
        StringBuilder sb=new StringBuilder("hugo");
//        todo
    }
}
