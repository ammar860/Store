package com.utility.store.service;

import com.utility.store.dto.StoreMasterDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class StoreService {
        @Value("${app.config.lbs-file-path}")
        String filePath;

    public static boolean validateDate(String strDate) {
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        sdfrmt.setLenient(false);
        try
        {
            Date javaDate = sdfrmt.parse(strDate);
        }
        catch (ParseException e)
        {
            return false;
        }
        return true;
    }





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

    public String saveFile(byte[] bytes) throws IOException {

        String file_name = new Date().getTime() + ".xlsx";
        File file = new File(filePath+file_name);

        OutputStream out = new FileOutputStream(file);
        try {
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
        return file.getName();
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


    public Map<String,List<StoreMasterDTO>> readExcelData(Map<String,String> mapping , String file_name) throws IOException, NoSuchFieldException, IllegalAccessException, ParseException {
        StoreService service = new StoreService();
        File file = new File(filePath+file_name);
        InputStream is = new FileInputStream(file);
        InputStream inputStream = new FileInputStream(file);

        List<String> excelHeaders = service.getHeaders(is);
        Map<String,List<StoreMasterDTO>> res = new HashMap<>();
        List<StoreMasterDTO> list = new ArrayList<>();
        List<StoreMasterDTO> errData = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            StoreMasterDTO storeMasterDTO = new StoreMasterDTO();
            Boolean errValue = false;
            for (String str:excelHeaders) {
                Class cls = storeMasterDTO.getClass();
                for (String key: mapping.keySet()) {
                    if(mapping.get(key).equals(str)){
                        Field field = cls.getDeclaredField(key);
                        field.setAccessible(true);
                        DataFormatter formatter = new DataFormatter();
                        String val = formatter.formatCellValue(row.getCell(excelHeaders.indexOf(str)));
                        if(field.toString().equals("private java.util.Date com.utility.store.dto.StoreMasterDTO.whenAddedDate")){
                            errValue=validateDate(val);
                            if(errValue){
                                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(val);
                                field.set(storeMasterDTO, date);
                            }
                            else{
                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                                LocalDateTime now = LocalDateTime.now();
                                field.set(storeMasterDTO, new SimpleDateFormat("dd/MM/yyyy").parse(dateTimeFormatter.format(now)));
                            }
                        }
                        else{
                            field.set(storeMasterDTO, val);
                        }
                    }
                }
            }
            if(!errValue||storeMasterDTO.getBocId().isEmpty()||storeMasterDTO.getStoreId().isEmpty()){
                errData.add(storeMasterDTO);
            }
            else {
                list.add(storeMasterDTO);
            }
        }
        res.put("validData",list);
        res.put("errorData",errData);
        return res;
    }
}
