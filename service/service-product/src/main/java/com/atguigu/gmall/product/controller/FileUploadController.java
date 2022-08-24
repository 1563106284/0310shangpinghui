package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 后台图片上传接口
 */
@RequestMapping("/admin/product")
@RestController
public class FileUploadController {

    /**
     * 1：上传图片
     */
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file")MultipartFile file){

        return Result.ok();
    }

}
