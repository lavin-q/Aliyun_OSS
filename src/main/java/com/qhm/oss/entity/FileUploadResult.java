package com.qhm.oss.entity;

import lombok.Data;
/**
* @Description: 返回值类<br/>
* @Author: qhm <br/>
* @Date: 2019/10/10 10:26<br/>
* @Version: 1.0 <br/>
*/
@Data
public class FileUploadResult {
    // 文件唯一标识
    private String uid;
    // 文件名
    private String name;
    // 状态有：uploading done error removed
    private String status;
    // 服务端响应内容，如：'{"status": "success"}'
    private String response;
}
