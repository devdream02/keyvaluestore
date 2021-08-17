package com.devdream02.keyvaluestore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;

@Slf4j
@Service
public class KeyValueService {

    private HashMap<String,String> keyValueStore;
    private String datafile = "target/myData.txt";

    @Autowired
    private Producer producer;

    public KeyValueService() {
        keyValueStore = new HashMap<String,String>();
        try {
            File file = new File(datafile);
            if(!file.exists()){
                file.createNewFile();
            }
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                System.out.println(data);
                String[] kvpair = data.split(":");
                String key = null, value = null;
                if(kvpair.length >= 2) {
                    key = (kvpair[0] != null) ? kvpair[0] : null;
                    value = (kvpair[1] != null) ? kvpair[1] : null;
                } else if(kvpair.length == 1) {
                    key = kvpair[0];
                }

                System.out.println("Key :" + key + "value : " + value);
                //check if the value is empty. that means its a deleted record
                //if an entry already exists in hashmap remove that also
                if (value == null) {
                    if (keyValueStore.containsKey(key)) {
                        keyValueStore.remove(key);
                    }
                } else {
                    keyValueStore.put(key, value);
                }
            }

            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getValue(String key) {

        if(!keyValueStore.containsKey(key)){
            throw new EntityNotFoundException("Key is not present");
        }
        return keyValueStore.get(key);
    }

    public boolean addOrUpdateEntry(String key, String value) {
        String data = key + ":" + value;
        if(!addFileEntry(data)) {
            return false;
        }

        keyValueStore.put(key,value);
        this.producer.sendMessage(data);
        return true;
    }

    public boolean deleteEntry(String key) {
        if(!keyValueStore.containsKey(key)){
            throw new EntityNotFoundException(key + " : key is not there in store");
        }
        String data = key + ":";
        if(!addFileEntry(data)) {
            return false;
        }
        keyValueStore.remove(key);
        return true;
    }

    private boolean addFileEntry(String data) {
        //create a file entry
        System.out.println("Trying to write an entry into the file : " + data);
        try {
            File fh = new File(datafile);
            if(!fh.exists()) {
                fh.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(datafile,true);
            fileWritter.write(data);
            fileWritter.write("\n");
            fileWritter.flush();
            fileWritter.close();
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
