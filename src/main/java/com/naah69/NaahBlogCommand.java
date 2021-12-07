package com.naah69;

import com.naah69.command.CompileCommand;
import com.naah69.command.DeployCommand;
import com.naah69.command.NewCommand;
import com.naah69.command.ServerCommand;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.annotations.QuarkusMain;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = {
        NewCommand.class,
        ServerCommand.class,
        CompileCommand.class,
        DeployCommand.class,
        ServerCommand.class
})
public class NaahBlogCommand {

}
