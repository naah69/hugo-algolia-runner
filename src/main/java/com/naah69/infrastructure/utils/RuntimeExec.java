package com.naah69.infrastructure.utils;

import lombok.SneakyThrows;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class RuntimeExec {
    private static final Logger logger = Logger.getLogger(RuntimeExec.class);
    private static ExecutorService executor= Executors.newFixedThreadPool(5);

    @SneakyThrows
    public static boolean run(String command) {
        Process process = Runtime.getRuntime().exec(command);
        clearStream(process.getInputStream(),true);
        clearStream(process.getErrorStream(),false);
        int i = process.waitFor();
        if (i != 0) {
            logger.errorf("Failed to exec command and the return status's is: {}", i);
            logger.errorf("Failed Command:{}", command);
            return false;
        }
        return true;
    }

    public static void clearStream(InputStream stream,boolean isNormal) {
        //处理ErrorStream的线程
        executor.execute(() -> {
            String line = null;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(stream));) {
                while ((line = in.readLine()) != null) {
                    if (isNormal){
                        logger.info(line);
                    }else{
                        logger.error(line);
                    }
                }
            } catch (IOException e) {
                logger.error("print log error",e);
            }
        });
    }
}
