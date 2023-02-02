package com.xsh;

import com.xsh.dao.BookDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Springboot07DruidApplicationTests {

	@Resource
	private BookDao bookDao;
	@Test
	void contextLoads() {
		System.out.println(bookDao.selectList(null));
	}

}
