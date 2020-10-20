package com.jff.ims.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jff.ims.util.Page;
import com.github.pagehelper.PageHelper;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestGenealogyManagement {
	
	@Autowired
//	@Test
	public void findByPage() {
		Page page = new Page();
		int count = 6;
		int num = 1;
		page.setStart(count*(num-1));
		page.setCount(count);
		PageHelper.offsetPage(page.getStart(),page.getCount());
//		int total = (int) new PageInfo<>(userList).getTotal();
//        page.setTotal(total);
	}
	
}
