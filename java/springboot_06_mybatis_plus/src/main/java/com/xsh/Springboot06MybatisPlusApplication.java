package com.xsh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.xsh"})
//@SpringBootApplication()
@MapperScan("com.xsh.dao")
public class Springboot06MybatisPlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot06MybatisPlusApplication.class, args);
	}

}
