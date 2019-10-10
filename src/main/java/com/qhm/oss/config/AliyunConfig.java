package com.qhm.oss.config;
import	java.beans.BeanInfo;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
* @Description: 阿里云配置类<br/>
* @Author: qhm <br/>
* @Date: 2019/10/10 10:12<br/>
* @Version: 1.0 <br/>
*/
@Configuration
@PropertySource(value = {"classpath:oss.properties"})
@ConfigurationProperties(prefix = "aliyun")
@Data
public class AliyunConfig {

    private String endPoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String urlPrefix;

    @Bean
    public OSS ossClient(){
        return new OSSClient(endPoint,accessKeyId,accessKeySecret);
    }

}
