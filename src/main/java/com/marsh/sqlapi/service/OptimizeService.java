package com.marsh.sqlapi.service;

import com.marsh.sqlapi.controller.request.OptimizeReq;
import com.marsh.sqlapi.util.CommandLineUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OptimizeService {


    public String optimize(OptimizeReq req) {
        String res = "";
        if (req.getDbType() == 1) {
            var cmd = String.format("echo \"%s\" | soar -online-dsn=\"%s:%s@%s:%d/%s\" -allow-online-as-test=true", req.getSql(), req.getUsername(), req.getPassword(), req.getHost(), req.getPort(), req.getDbName());
            log.info(cmd);
            res = CommandLineUtil.execute(cmd);
            log.info("结果是：{}", res);
        }
        return res;
    }
}
