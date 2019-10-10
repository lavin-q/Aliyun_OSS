package com.qhm.oss.controller;

import com.aliyun.oss.model.OSSObjectSummary;
import com.qhm.oss.entity.FileUploadResult;
import com.qhm.oss.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @Description: 文件上传Controller<br/>
* @Author: qhm <br/>
* @Date: 2019/10/10 10:22<br/>
* @Version: 1.0 <br/>
*/
@Controller
@RequestMapping("file")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;


    /**
    * @Description: 上传文件到oss <br/>
    * @Author  qhm    <br/>
    * @Date: 2019/10/10 10:46 <br/>
    * @Version: 1.0 <br/>
    */
    @RequestMapping("/upload")
    @ResponseBody
    public FileUploadResult upload(@RequestParam("file") MultipartFile uploadFile)
            throws Exception {
        return this.fileUploadService.upload(uploadFile);
    }


    /**
    * @Description: 根据文件名删除oss文件 <br/>
    * @Author  qhm    <br/>
    * @Date: 2019/10/10 10:46 <br/>
    * @Version: 1.0 <br/>
    */
    @RequestMapping("/delete")
    @ResponseBody
    public FileUploadResult delete(@RequestParam("fileName") String objectName)
            throws Exception {
        return this.fileUploadService.delete(objectName);
    }


    /**
    * @Description: 查询所有文件 <br/>
    * @Author  qhm    <br/>
    * @Date: 2019/10/10 10:47 <br/>
    * @Version: 1.0 <br/>
    */
    @RequestMapping("/list")
    @ResponseBody
    public List<OSSObjectSummary> list()
            throws Exception {
        return this.fileUploadService.list();
    }

   
    /**
    * @Description: 根据文件名下载oss上的文件 <br/>
    * @Author  qhm    <br/>
    * @Date: 2019/10/10 10:47 <br/>
    * @Version: 1.0 <br/>
    */
    @RequestMapping("/download")
    @ResponseBody
    public void download(@RequestParam("fileName") String objectName, HttpServletResponse response) throws IOException {
        //通知浏览器以附件形式下载
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(objectName.getBytes(), "ISO-8859-1"));
        this.fileUploadService.exportOssFile(response.getOutputStream(),objectName);
    }
}
