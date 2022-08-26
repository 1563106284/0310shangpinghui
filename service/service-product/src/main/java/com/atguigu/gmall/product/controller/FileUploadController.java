package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.sun.xml.internal.bind.v2.TODO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.applet.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台图片上传接口
 */
@Api(tags = "文件上传")
@RequestMapping("/admin/product")
@RestController
public class FileUploadController {

    @Autowired
    BaseTrademarkService baseTrademarkService;
    /**
     * 1：上传图片
     */
    @ApiOperation("上图图片接口")
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file") MultipartFile file) throws Exception {
       String url = baseTrademarkService.uploadFile(file);

        return Result.ok(url);
    }
    // TODO: 2022/8/25


}

