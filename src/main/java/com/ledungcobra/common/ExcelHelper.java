package com.ledungcobra.common;

import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class ExcelHelper {

    public static OutputStream readFrom(MultipartFile multipartFile) {
        return null;
    }

    @SneakyThrows
    public static byte[] createFileExcel(String... columns) {
        try (var workBook = new XSSFWorkbook();) {
            var sheet = workBook.createSheet();
            var header = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                var cell = header.createCell(i);
                cell.setCellValue(columns[i]);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workBook.write(bos);
            return bos.toByteArray();
        }
    }

}
