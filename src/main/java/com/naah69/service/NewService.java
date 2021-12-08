package com.naah69.service;

import com.naah69.domain.Hugo;
import com.naah69.infrastructure.config.CommandConfig;
import lombok.AllArgsConstructor;

import javax.enterprise.context.Dependent;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Dependent
@AllArgsConstructor
public class NewService {
    public static final String NULL_STR = "";

    public final CommandConfig commandConfig;

    public boolean createBlog(String fileName) {
        return Hugo.create().add("new").add(generateFileName(fileName)).run();
    }

    public String generateFileName(String fileName) {
        StringBuilder sb = new StringBuilder();
        CommandConfig.New newConfig = commandConfig.newBlog();
        sb.append(generateDir(newConfig));
        sb.append(generatePrefix(newConfig.prefix()));
        sb.append(fixFilename(fileName));
        return sb.toString();
    }

    public String fixFilename(String fileName) {
        return fileName + (!fileName.endsWith(".md") ? ".md" : NULL_STR);
    }

    public String generateDir(CommandConfig.New newConfig) {
        String dir = newConfig.dir().orElse(NULL_STR);
        if (!isNullOrEmpty(dir)) {
            dir = dir.replace("/", File.separator);
            dir = dir.replace("\\", File.separator);
            if (dir.startsWith(File.separator)) {
                dir = dir.substring(1);
            }
            if (dir.endsWith(File.separator)) {
                dir = dir.substring(0, dir.length() - 1);
            }
            if (!isNullOrEmpty(dir)) {
                dir = dir + File.separator;
            }
        }
        return dir;
    }


    public String generatePrefix(CommandConfig.New.Prefix prefix) {
        Boolean enable = prefix.enabled();
        String prefixStr = NULL_STR;

        if (enable) {
                switch (prefix.prefixType()) {
                    case DATE:
                        prefixStr = DateTimeFormatter.ofPattern(prefix.dateFormat()).format(LocalDate.now());
                        break;
                    case STRING:
                    default:
                        prefixStr = prefix.prefixStr().orElse(NULL_STR);
                }

            String sep = prefix.sep();
            if (!isNullOrEmpty(prefixStr)) {
                prefixStr = prefixStr + sep;
            }
        }

        return prefixStr;
    }

    private boolean isNullOrEmpty(String dir) {
        return dir == null || dir.isBlank();
    }

}
