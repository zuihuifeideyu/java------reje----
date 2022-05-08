package com.itheima.reje.controller;

import com.itheima.reje.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    // 获取文件转存的路径位置
    // value用来获取配置文件中的配置参数
    @Value("${reje.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        //MultipartFile对象的名称必须和前端传过来的文件name保持一致（不是指文件的名字，是前端在数据中起的名字）
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();//abc.jpg
        //获取文件是什么格式的文件
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;//dfsdfdfd.jpg

        //创建一个目录对象
        File dir = new File(basePath);

        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在，需要创建
            dir.mkdirs();
        }

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //图片的文件名是需要保存到菜品那张表里，所以要返回文件名
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    // 输出流需要response来获取
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，根据指定路径和文件名通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg"); // 这里返回的是图片文件，所以设置内容的类型是jpeg格式

            int len = 0;
            byte[] bytes = new byte[1024];
            // 通过输入流将数据不断写入bytes数组
            while ((len = fileInputStream.read(bytes)) != -1){
                // 将 bytes 数组中的数据通过输出流不断输出给浏览器
                outputStream.write(bytes,0,len);
                outputStream.flush(); // 刷新
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
