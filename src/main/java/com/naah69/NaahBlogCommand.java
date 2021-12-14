package com.naah69;

import com.naah69.command.*;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = {
        NewCommand.class,
        ServerCommand.class,
        CompileCommand.class,
        DeployCommand.class,
        ProdCommand.class
})
public class NaahBlogCommand {

}
