package com.zph.programmer.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ZipUtils {
    private static final long FILE_SIZE = 1024 * 1024 * 30; //30M

    /**
     * 分卷压缩
     *
     * @param filesToAdd 要压缩的文件绝对路径列表（支持多个文件的合并压缩）
     * @param destFile   要压缩的zip文件名
     * @return 压缩文件路径（如分卷会返回以 "," 分隔的文件路径列表）
     * @throws ZipException
     */
    public static List<File> zipSplit(List<File> filesToAdd, String destFile) throws ZipException {
        File tmpFile = new File(destFile);
        if (tmpFile.exists()) {
            boolean result = tmpFile.delete();
            if (!result) {
                throw new ZipException("文件没有权限");
            }
        }
        ZipFile zipFile = new ZipFile(destFile);
        zipFile.createSplitZipFile(filesToAdd, new ZipParameters(), true, FILE_SIZE); //
        return zipFile.getSplitZipFiles();

    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\output.sql");
        File file2 = new File("D:\\查询结果.sql");
        if (file.exists()) {
            List<File> filesToAdd = new ArrayList<>();
            filesToAdd.add(file);
            filesToAdd.add(file2);
            //压缩
            List<File> zipList = ZipUtils.zipSplit(filesToAdd, "D:\\output.zip");

            try {
                File txtFile = new File("D:\\output.txt");
                if (txtFile.exists()) {
                    boolean result = txtFile.delete();
                }
                boolean result = txtFile.createNewFile();
                FileWriter fileWriter = new FileWriter(txtFile.getAbsoluteFile(), true);
                for (File zip : zipList) {
                    fileWriter.write(zip.getName() + "\n");
                }
                fileWriter.close();
            } catch (Exception e) {
                log.error("创建文件失败，", e);
            }

            //解压
            ZipFile extractFile = new ZipFile("D:\\output.zip");
            extractFile.extractAll("D:\\zip4j");
        }
    }
}
