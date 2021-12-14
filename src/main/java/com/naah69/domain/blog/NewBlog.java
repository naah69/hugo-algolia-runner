package com.naah69.domain.blog;

import com.naah69.domain.Hugo;
import com.naah69.infrastructure.config.command.sub.NewBlogConfig;
import com.naah69.infrastructure.constant.NewConstant;
import com.naah69.infrastructure.enums.NewBlogPrefixType;
import com.naah69.infrastructure.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class NewBlog {


    public final NewBlogConfig newConfig;

    public boolean createBlog(String fileName) {
        return Hugo.create(NewConstant.COMMAND).add(generateFileName(fileName)).run();
    }

    public String generateFileName(String fileName) {
        String dir = generateDir();
        if (log.isDebugEnabled()) {
            log.debug("[NEW] dir of new blog is {}", dir);
        }
        String finalFileName = fixFilename(fileName);
        String prefix = generatePrefix();
        if (StringUtils.isNotBlank(prefix)) {
            finalFileName = String.join(getSep(newConfig), List.of(prefix, finalFileName));
        }
        if (log.isDebugEnabled()) {
            log.debug("[NEW] final file name of new blog is {}", finalFileName);
        }
        return dir + finalFileName;
    }

    private String getSep(NewBlogConfig newConfig) {
        return Optional.ofNullable(newConfig).map(c -> c.prefix()).map(p -> p.sep()).orElse(NewConstant.NULL_STR);
    }

    public static String fixFilename(String fileName) {
        return fileName + (!fileName.endsWith(NewConstant.MD_SUFFIX) ? NewConstant.MD_SUFFIX : NewConstant.NULL_STR);
    }

    public String generateDir() {
        return generateDir(getDir(newConfig));
    }

    private String getDir(NewBlogConfig newConfig) {
        return Optional.ofNullable(newConfig).map(c -> c.dir().orElse(NewConstant.NULL_STR)).orElse(NewConstant.NULL_STR);
    }

    public static String generateDir(String dir) {
        if (StringUtils.isNotBlank(dir)) {
            dir = dir.replace(NewConstant.UNIX_SEP, File.separator);
            dir = dir.replace(NewConstant.WIN_SEP, File.separator);
            if (dir.startsWith(File.separator)) {
                dir = dir.substring(1);
            }
            if (dir.endsWith(File.separator)) {
                dir = dir.substring(0, dir.length() - 1);
            }
            if (StringUtils.isNotBlank(dir)) {
                dir = dir + File.separator;
            }
        }
        return dir;
    }

    @SneakyThrows
    public String generatePrefix() {
        NewBlogConfig.Prefix prefix = getPrefix(newConfig);
        //获取所有类型prefix
        List<NewBlogConfig.Prefix.BasePrefix> prefixList = Arrays.asList(prefix.text(), prefix.date());
        if (log.isDebugEnabled()) {
            log.debug("[NEW] prefix enable:{}", prefixList.stream().filter(Objects::nonNull).map(p -> p.enabled()).collect(Collectors.toList()));
        }
//        log.debug("aa:{}", ConfigProvider.getConfig().getConfigValue("blog.new.prefix.date.enabled"));
//        SmallRyeConfig unwrap = ConfigProvider.getConfig().unwrap(SmallRyeConfig.class);
//        构建所有prefix
        List<String> prefixs = prefixList.stream().filter(Objects::nonNull).map(NewBlog::generatePrefix).filter(s -> StringUtils.isNotBlank(s)).collect(Collectors.toList());
        return String.join(getSep(newConfig), prefixs);
    }

    private NewBlogConfig.Prefix getPrefix(NewBlogConfig newConfig) {
        return Optional.ofNullable(newConfig).map(c -> c.prefix()).orElse(new NewBlogConfig.Prefix() {
            @Override
            public String sep() {
                return null;
            }

            @Override
            public DatePrefix date() {
                return null;
            }

            @Override
            public TextPrefix text() {
                return null;
            }
        });
    }

    public static String generatePrefix(NewBlogConfig.Prefix.BasePrefix prefix) {
        Boolean enable = prefix.enabled();
        NewBlogPrefixType type = prefix.type();
        String prefixStr = NewConstant.NULL_STR;

        if (enable && Objects.nonNull(type)) {
//            如果实现多了，需要重构为策略模式
            switch (type) {
                case DATE:
                    NewBlogConfig.Prefix.DatePrefix datePrefix = (NewBlogConfig.Prefix.DatePrefix) prefix;
                    prefixStr = DateTimeFormatter.ofPattern(datePrefix.dateFormat()).format(LocalDate.now());
                    break;
                case TEXT:
                default:
                    NewBlogConfig.Prefix.TextPrefix textPrefix = (NewBlogConfig.Prefix.TextPrefix) prefix;
                    prefixStr = textPrefix.value().orElse(NewConstant.NULL_STR);
            }
        }
        return prefixStr;
    }


}
