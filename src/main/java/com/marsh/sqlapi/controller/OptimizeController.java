package com.marsh.sqlapi.controller;

import com.marsh.sqlapi.controller.request.OptimizeReq;
import com.marsh.sqlapi.service.OptimizeService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OptimizeController {

    private final OptimizeService optimizeService;

    public OptimizeController(OptimizeService optimizeService) {
        this.optimizeService = optimizeService;
    }

    @PostMapping("/optimize")
    public BaseResponse<String> optimize(@RequestBody OptimizeReq req) {
        var res = optimizeService.optimize(req);
        return BaseResponse.success(res);
    }

}
