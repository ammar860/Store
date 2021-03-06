package com.utility.store.controller;


import com.utility.store.dto.StoreMasterDTO;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/store")
public class Controller {

    @Autowired
    StoreService storeService;


    @RequestMapping(method = RequestMethod.POST,value = "/getExcelHeaders")
    public ResponseEntity<?> Phase1(@RequestBody MultipartFile file) {
        JSONObject responseJsonObject = new JSONObject();
        try {
            if (file.getOriginalFilename().endsWith(".xlsx")){
                List<Map<String,Object>> list ;
                 String fileName = storeService.saveFile(file.getBytes());
                list = storeService.readExcelHeaders(file.getInputStream());
                String message = "task performed successfully";
                responseJsonObject.put("success",true);
                responseJsonObject.put("message",message);
                responseJsonObject.put("data",list);
                responseJsonObject.put("fileName",fileName);


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
    public ResponseEntity<?> Phase1(@RequestBody Map<String,String> map) {
        JSONObject responseJsonObject = new JSONObject();
        try {
            List<StoreMasterDTO> list ;
            String file_name = map.get("fileName");
            map.remove("fileName");
            Map<String,List<StoreMasterDTO>> listMap = storeService.readExcelData(map,file_name);
            String message = "task performed successfully";
            responseJsonObject.put("success",true);
            responseJsonObject.put("message",message);
            responseJsonObject.put("errorData",listMap.get("errorData"));
            responseJsonObject.put("validData",listMap.get("validData"));
        }
        catch (Exception e){
            responseJsonObject.put("success",false);
            responseJsonObject.put("message",e.getMessage());
        }
        return new ResponseEntity<>(responseJsonObject.toString(), HttpStatus.OK);
    }
}
