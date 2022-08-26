package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseTrademark;
import com.baomidou.mybatisplus.extension.service.IService;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
* @author mengxueshong
* @description 针对表【base_trademark(品牌表)】的数据库操作Service
* @createDate 2022-08-23 10:13:45
*/
public interface BaseTrademarkService extends IService<BaseTrademark> {

    String uploadFile(MultipartFile file) throws  Exception;
}
