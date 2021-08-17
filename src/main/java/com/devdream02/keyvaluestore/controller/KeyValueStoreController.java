package com.devdream02.keyvaluestore.controller;


import com.devdream02.keyvaluestore.model.Record;
import com.devdream02.keyvaluestore.service.KeyValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/keyValueStore")
@RequiredArgsConstructor
public class KeyValueStoreController {

    @Autowired
    private KeyValueService keyValueService;

    @GetMapping("/{key}")
    public String getValue( @NotNull @PathVariable(value = "key") String key)
    {
        return keyValueService.getValue(key);
    }

    @PostMapping("/createUpdate")
    public boolean createOrUpdateRecord(@RequestBody @Valid Record record)
    {
        return keyValueService.addOrUpdateEntry(record.getKey(),record.getValue());
    }

    @DeleteMapping("/{key}")
    public boolean deleteRecord(@NotNull @PathVariable(value = "key") String key)
    {
        return keyValueService.deleteEntry(key);
    }
}
