package com.xsh;

import com.xsh.dao.BookDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Springboot06MybatisPlusApplicationTests {

	@Resource
	private BookDao bookDao;

	@Test
	void contextLoads() {
		System.out.println(bookDao.selectById(1));
	}

	@Test
	void textGetAll() {
		System.out.println(bookDao.selectList(null));
	}
}
