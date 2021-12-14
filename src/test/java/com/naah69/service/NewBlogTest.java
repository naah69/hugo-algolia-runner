package com.naah69.service;

import com.naah69.domain.blog.NewBlog;
import com.naah69.infrastructure.config.command.CommandConfig;
import com.naah69.infrastructure.config.command.sub.NewBlogConfig;
import com.naah69.infrastructure.enums.NewBlogPrefixType;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@QuarkusTest
@TestProfile(NewCommandProfile.DatePrefixProfile.class)
class NewBlogTest {

    final String dateFormatStr = "yyyy-MM-dd";

    @Inject
    CommandConfig commandConfig;

    @Test
    void createBlog() {
    }

    @Test
    void generateFileNameWithNull() {
        NewBlog newBlog = new NewBlog(null);
        String filename = "aa";
        String except="aa.md";
        String actual = newBlog.generateFileName(filename);
        Assertions.assertEquals(except,actual);
    }

    @Test
    void generateFileNameWithEmpty() {
        NewBlog newBlog = new NewBlog(new NewBlogConfig() {
            @Override
            public Prefix prefix() {
                return null;
            }

            @Override
            public Optional<String> dir() {
                return Optional.empty();
            }
        });
        String filename = "aa";
        String except="aa.md";
        String actual = newBlog.generateFileName(filename);
        Assertions.assertEquals(except,actual);
    }

    @Test
    void generateFileNameWithMultiPrefix() {
        NewBlog newBlog = new NewBlog(commandConfig.newBlog());
        String filename = "aa";
        String strPrefix = "asd";
        String datePrefix = DateTimeFormatter.ofPattern(dateFormatStr).format(LocalDate.now());
        String dir="post";
        String except=String.format("%s%s%s=%s=%s.md",dir,File.separator,strPrefix,datePrefix,filename);
        String actual = newBlog.generateFileName(filename);
        Assertions.assertEquals(except,actual);

    }

    @Test
    void fixFilename() {
        Assertions.assertEquals("aaa.md",NewBlog.fixFilename("aaa"));
    }

    @Test
    void generateDir() {
        String expected="post"+ File.separator;
        Assertions.assertEquals(expected,NewBlog.generateDir("/post"));
        Assertions.assertEquals(expected,NewBlog.generateDir("/post/"));
        Assertions.assertEquals(expected,NewBlog.generateDir("\\post\\"));
        Assertions.assertEquals(expected,NewBlog.generateDir("/post\\"));

    }

    @Test
    void datePrefix() {


        String prefix = NewBlog.generatePrefix(new NewBlogConfig.Prefix.DatePrefix() {

            @Override
            public boolean enabled() {
                return true;
            }

            @Override
            public String dateFormat() {
                return dateFormatStr;
            }

            @Override
            public NewBlogPrefixType type() {
                return NewBlogPrefixType.DATE;
            }
        });
        String dateFormat = DateTimeFormatter.ofPattern(dateFormatStr).format(LocalDate.now());
        Assertions.assertEquals(dateFormat,prefix);
    }

    @Test
    void strPrefix() {

        String prefix = NewBlog.generatePrefix(new NewBlogConfig.Prefix.TextPrefix() {

            @Override
            public boolean enabled() {
                return true;
            }

            @Override
            public Optional<String> value() {
                return Optional.of("asd");
            }

            @Override
            public NewBlogPrefixType type() {
                return NewBlogPrefixType.TEXT;
            }
        });
        Assertions.assertEquals("asd",prefix);
    }
}