package com.marsh.sqlapi.controller;

import com.marsh.sqlapi.controller.request.ConnectIsLiveReq;
import com.marsh.sqlapi.controller.request.CreateConnectReq;
import com.marsh.sqlapi.service.ConnectService;
import com.marsh.zutils.entity.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectController {

    private final ConnectService connectService;

    public ConnectController(ConnectService connectService) {
        this.connectService = connectService;
    }

    @PostMapping("/createConnect")
    public BaseResponse<Object> connect(@RequestBody CreateConnectReq req) {
        connectService.createConnect(req);
        return BaseResponse.success();
    }

    @PostMapping("/connectIsLive")
    public BaseResponse<Boolean> connectIsLive(@RequestBody ConnectIsLiveReq req){
        return BaseResponse.success(connectService.isLive(req));
    }

}
