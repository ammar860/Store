package com.utility.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

@Service
public class FileService {

    public String saveFile(byte[] bytes) throws MalformedURLException, IOException {

        File file = new File("src/main/resources/" + "temp.xlsx");

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
}
