package com.naah69.domain;

import com.naah69.infrastructure.config.CommandConfig;
import com.naah69.infrastructure.utils.RuntimeExec;
import io.smallrye.config.SmallRyeConfig;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.inject.spi.CDI;
import java.util.Optional;

public class Hugo {

    public static final String WIN = "win";
    public static final String LINUX = "linux";
    public static final String UNIX = "unix";
    private StringBuilder sb = null;

    private Hugo() {
        Optional<String> basicCommandOp = ConfigProvider.getConfig().unwrap(SmallRyeConfig.class).getConfigMapping(CommandConfig.class).basicCommand();
        String basicCommand = basicCommandOp.orElseGet(this::getDefaultHugoCommandBySystem);

        sb = new StringBuilder(basicCommand);
    }


    public static Hugo create() {
        return new Hugo();
    }

    public Hugo add(String parameter) {
        sb.append(" ").append(parameter);
        return this;
    }

    public String build() {
        return sb.toString();
    }

    public boolean run(){
        return RuntimeExec.run(build());
    }


    private String getDefaultHugoCommandBySystem() {

        String osType = getSystemType();
        switch (osType) {
            case WIN:
                return "hugo.exe";
            case LINUX:
            case UNIX:
            default:
                return "hugo";
        }
    }

    private String getSystemType() {
        String os = System.getProperty("os.name").toLowerCase();
        String osType = os.contains(WIN) ? WIN : os.contains(LINUX) ? LINUX : UNIX;
        return osType;
    }

}
