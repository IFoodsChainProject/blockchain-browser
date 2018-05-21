package com.lbcy.controller;

import com.lbcy.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */
@RestController
@RequestMapping("/interface")
public class InterfaceController
{
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/address/{id}")
    public Address addressDetail(@PathVariable("id") String id, Model model)
    {
        Address address = mongoTemplate.findById(id, Address.class);

        return address;
    }
}
