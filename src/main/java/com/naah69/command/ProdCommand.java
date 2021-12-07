package com.naah69.command;

import picocli.CommandLine;

@CommandLine.Command(name = "prod",description = "编译+部署", mixinStandardHelpOptions = true)
public class ProdCommand {
}
