package com.marsh.sqlapi.util;

import cn.hutool.core.util.RuntimeUtil;
import com.marsh.sqlapi.exception.BaseException;
import com.marsh.zutils.exception.BaseBizException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
public class CommandLineUtil {

    public static String execute(String commandLine) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(new File("/root/soar"));

        builder.command("sh", "-c", commandLine);
        Process process  = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            log.error("执行soar指令出错：{}", e.getMessage());
            throw new BaseBizException("111111", e.getMessage());
        }

        BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return output.lines().collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) {
        System.out.println("`user`".replace("`", ""));
    }
}
