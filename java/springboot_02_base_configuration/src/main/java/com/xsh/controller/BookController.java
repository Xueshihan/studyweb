package com.xsh.controller;

import com.xsh.MyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Rest模式
@RestController
@RequestMapping("/books")
public class BookController {

    @Value("${tempDir}")
    public  String country1;

    @Value("${users.name}")
    public  String name1;

    @Value("${class1.name}")
    public  String name2;

    @Autowired
    private MyDataSource myDataSource;

    @GetMapping
    public String getbyId(){
        System.out.println("spring is running...");
        System.out.println("country1===>"+country1);
        System.out.println("name1===>"+name1);
        System.out.println("name1===>"+name2);
        System.out.println("myDataSource===>"+myDataSource);
        return "spring is running.....";
    };
}
