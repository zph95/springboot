package com.zph.programmer.springdemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * FileUtil
 *
 * @author zengpenghui
 * @date 2021/1/5 18:31
 */
@Slf4j
public class FileUtil {
    /**
     * MultipartFile 转 File
     *
     * @param file file
     * @throws Exception Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file != null && file.getSize() > 0) {
            InputStream ins = file.getInputStream();
            toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    /**
     * 获取流文件
     *
     * @param ins  ins
     * @param file file
     */
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file file
     */
    public static void deleteTempFile(File file) {
        log.info("删除临时文件");
        if (file != null) {
            Boolean result = file.delete();
            log.info("删除文件结果：{}", result);
        }
    }

    public static Boolean checkFileName(String fileName) {
        if (StringUtils.isEmpty(fileName) || fileName.length() > 100) {
            return false;
        } else if (fileName.startsWith("/") || fileName.contains("//")) {
            return false;
        } else {
            return fileName.matches("(([\\u4e00-\\u9fa5]*)|([0-9]*)|([A-Z]*)|([a-z]*)|(_)*|(-)*|(/)*|(\\.)*)+");
        }
    }
}
