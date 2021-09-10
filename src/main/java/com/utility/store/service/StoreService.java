package com.utility.store.service;

import com.utility.store.dto.StoreMasterDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class StoreService {
    @Value("${app.config.lbs-file-name}")
    String fileName;

    public List<Map<String,Object>> readExcelHeaders(InputStream is) throws IOException {

        List<Map<String,Object>> list = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Row row;
        Field[] prop = StoreMasterDTO.class.getDeclaredFields();
        row = sheet.getRow(0);
        java.util.Iterator<Cell> cellItr =  row.cellIterator();
        ListIterator<Field> fields =  Arrays.asList(prop).listIterator();
        List<String> feildOut = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        while(fields.hasNext()) {
            feildOut.add(fields.next().getName());
        }
        while(cellItr.hasNext()) {
            headers.add(cellItr.next().getStringCellValue());
        }
        Map<String,Object> obj = new HashMap<>();
        obj.put("Excel Header",headers);
        obj.put("Store Master",feildOut);
        list.add(obj);
        return list;
    }
    public List<String> getHeaders(InputStream is) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Row row;
        row = sheet.getRow(0);
        java.util.Iterator<Cell> cellItr =  row.cellIterator();
        List<String> headers = new ArrayList<>();
        while(cellItr.hasNext()) {
            headers.add(cellItr.next().getStringCellValue());
        }
        return headers;
    }


    public List<StoreMasterDTO> readExcelData(List<Map<String,String>> mapList ) throws IOException, NoSuchFieldException, IllegalAccessException {
        StoreService service = new StoreService();
        ClassPathResource resource = new ClassPathResource(fileName);
        InputStream is = resource.getInputStream();
        InputStream inputStream = resource.getInputStream();

        List<String> excelHeaders = service.getHeaders(is);
        List<StoreMasterDTO> list = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            for (Map<String,String> property: mapList ) {
                StoreMasterDTO storeMasterDTO = new StoreMasterDTO();
                for (String str:excelHeaders    ) {
                    Class cls = storeMasterDTO.getClass();
                    if (property.get(str)!= null){
                        Field field = cls.getDeclaredField(property.get(str));
                        field.setAccessible(true);
                        field.set(storeMasterDTO,row.getCell(excelHeaders.indexOf(str)).getStringCellValue());
                    }

                }
                list.add(storeMasterDTO);



            }
        }

        return list;
    }
}
