package com.naah69.domain;

import com.naah69.infrastructure.config.command.CommandConfig;
import com.naah69.infrastructure.config.command.SubCommandConfig;
import com.naah69.infrastructure.constant.BaseConstant;
import com.naah69.infrastructure.utils.RuntimeExec;
import io.smallrye.config.SmallRyeConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class Hugo {

    public static final String WIN = "win";
    public static final String LINUX = "linux";
    public static final String UNIX = "unix";
    private StringBuilder sb;
    private List<HugoListener> listeners;
    private SubCommandConfig config;

    private Hugo() {
        Optional<String> basicCommandOp = ConfigProvider.getConfig().unwrap(SmallRyeConfig.class).getConfigMapping(CommandConfig.class).basicCommand();
        String basicCommand = basicCommandOp.orElseGet(this::getDefaultHugoCommandBySystem);
        sb = new StringBuilder(basicCommand);
        listeners = new LinkedList<>();
    }


    public static Hugo create(String command) {
        return new Hugo().add(command);
    }

    public Hugo add(String parameter, String value) {
        sb.append(BaseConstant.SPACE).append(parameter).append(BaseConstant.SPACE).append(value);
        return this;
    }

    public Hugo add(String parameter) {
        sb.append(BaseConstant.SPACE).append(parameter);
        return this;
    }

    public Hugo addListener(HugoListener listener) {
        listeners.add(listener);
        return this;
    }

    public Hugo setCommandConfig(SubCommandConfig config) {
        this.config = config;
        return this;
    }


    public String build() {
        return sb.toString();
    }

    public boolean run() {
        listeners.forEach(l -> l.beforeRun(this));
        String command = build();
        if (log.isDebugEnabled()) {
            log.debug("[HUGO] run command: {}", command);
        }
        boolean result = RuntimeExec.run(command);
        if (result) {
            listeners.forEach(l -> l.afterRun(this));
        }
        return result;
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

    public interface HugoListener {

        void beforeRun(Hugo hugo);

        void afterRun(Hugo hugo);
    }

}
