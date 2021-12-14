package com.naah69.command;

import picocli.CommandLine;

@CommandLine.Command(name = "prod",description = "compile and deploy blog", mixinStandardHelpOptions = true)
public class ProdCommand {
}
