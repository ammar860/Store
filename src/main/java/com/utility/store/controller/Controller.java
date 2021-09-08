package com.utility.store.controller;


import com.utility.store.dto.StoreMasterDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.lang.reflect.Field;
import java.util.*;


@RestController
@RequestMapping(value = "/store")
public class Controller {

    @RequestMapping(method = RequestMethod.POST,value = "/pase1")
    public ResponseEntity<?> Phase1(@RequestBody MultipartFile file) {
        JSONObject responseJsonObject = new JSONObject();
        try {
            if (file.getOriginalFilename().endsWith(".xlsx")){
                XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
                XSSFSheet sheet = workbook.getSheetAt(0);
                Row row;
                Cell cell;
                Field[] prop = StoreMasterDTO.class.getDeclaredFields();
                row = sheet.getRow(0);
                java.util.Iterator<Cell> cellItr =  row.cellIterator();
                ListIterator<Field> fields =  Arrays.asList(prop).listIterator();
                List<Map<String,Object>> list = new ArrayList<>();

                while(cellItr.hasNext()) {
                    cell = cellItr.next();
                    Map<String,Object> obj = new HashMap<>();
                    Field field = fields.next();
                    obj.put("Excel Header",cell.getStringCellValue());
                    obj.put("Store Master",field.getName());
                    list.add(obj);
                }
                String message = "task performed successfully";
                responseJsonObject.put("success",true);
                responseJsonObject.put("message",message);
                responseJsonObject.put("data",list);

            }
        }
        catch (Exception e){
            responseJsonObject.put("success",false);
            responseJsonObject.put("message",e.getMessage());
        }
        return new ResponseEntity<>(responseJsonObject.toString(), HttpStatus.OK);
    }
}
