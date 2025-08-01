package io.github.tianyulife.excelimport.util;

import io.github.tianyulife.excelimport.constant.FileConstant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author: HanTianyu
 * @Date: 2025/8/1 12:54
 * @Description: io.github.tianyulife.excelimport.util
 */
public class CsvWriteUtils {


    public static void writeSingleDataToCsv(List<String> data, String filename, boolean addFlag, boolean isTitle) {

        //根据指定路径构建文件对象
        File csvFile = new File(System.getProperty("user.dir"),filename + FileConstant.CSV_SUFFIX_CSV);
        //文件输出流对象，第二个参数时boolean类型,为true表示文件追加（在已有的文件中追加内容）
        String content = convertToCSV(data);
        // 将流写在try里面，当try执行完之后，流会自动关闭
//       log.error("写入的内容是：" +content);
        try (FileOutputStream fileOutputStream = new FileOutputStream(csvFile, addFlag);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            //写入头
            if (isTitle){
                byte[] uft8bom = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
                fileOutputStream.write(uft8bom);
            }
            // 将数据写入到文件中
            bufferedWriter.write(content);
            bufferedWriter.newLine();
            //把缓存中的数据输出到CSV文件
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String convertToCSV(List<String> data) {
        return data.stream()
                .map(CsvWriteUtils::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }


    public static void writeDataToCsv(List<List<String>> datas, String filename, boolean addFlag, boolean isTitle) {
        //根据指定路径构建文件对象
        File csvFile = new File(System.getProperty("user.dir"), filename + FileConstant.CSV_SUFFIX_CSV);
        //文件输出流对象，第二个参数时boolean类型,为true表示文件追加（在已有的文件中追加内容）
        // 将流写在try里面，当try执行完之后，流会自动关闭
//       log.error("写入的内容是：" +content);
        try (FileOutputStream fileOutputStream = new FileOutputStream(csvFile, addFlag);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
            //写入头
            if (isTitle) {
                byte[] uft8bom = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
                fileOutputStream.write(uft8bom);
            }
            for(List<String> data : datas) {
                // 将数据写入到文件中
                String content = convertToCSV(data);
                bufferedWriter.write(content);
                bufferedWriter.newLine();
            }
            //把缓存中的数据输出到CSV文件
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
