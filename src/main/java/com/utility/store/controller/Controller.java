package com.utility.store.controller;


import com.utility.store.dto.StoreMasterDTO;
import com.utility.store.service.FileService;
import com.utility.store.service.StoreService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/store")
public class Controller {

    @Autowired
    StoreService storeService;
    @Autowired
    FileService fileService;
    @RequestMapping(method = RequestMethod.POST,value = "/getExcelHeaders")
    public ResponseEntity<?> Phase1(@RequestBody MultipartFile file) {
        JSONObject responseJsonObject = new JSONObject();
        try {
            if (file.getOriginalFilename().endsWith(".xlsx")){
                List<Map<String,Object>> list ;
                fileService.saveFile(file.getBytes());
                list = storeService.readExcelHeaders(file.getInputStream());
                String message = "task performed successfully";
                responseJsonObject.put("success",true);
                responseJsonObject.put("message",message);
                responseJsonObject.put("data",list);


            }
            else {
                String message = "Wrong input file";
                responseJsonObject.put("success",false);
                responseJsonObject.put("message",message);
            }
        }
        catch (Exception e){
            responseJsonObject.put("success",false);
            responseJsonObject.put("message",e.getMessage());
        }
        return new ResponseEntity<>(responseJsonObject.toString(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/getData")
    public ResponseEntity<?> Phase1(@RequestBody List<Map<String,String>> mapList) {
        JSONObject responseJsonObject = new JSONObject();
        try {
            List<StoreMasterDTO> list ;
            list = storeService.readExcelData(mapList);
            String message = "task performed successfully";
            responseJsonObject.put("success",true);
            responseJsonObject.put("message",message);
            responseJsonObject.put("data",list);
        }
        catch (Exception e){
            responseJsonObject.put("success",false);
            responseJsonObject.put("message",e.getMessage());
        }
        return new ResponseEntity<>(responseJsonObject.toString(), HttpStatus.OK);
    }
}
