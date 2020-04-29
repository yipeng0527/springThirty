package com.yp.spring.demo.service.impl;

import com.yp.spring.demo.service.IModifyService;
import com.yp.spring.framework.annotation.GPService;

/**
 * 增删改业务
 * @author Tom
 *
 */
@GPService
public class ModifyService implements IModifyService {

	/**
	 * 增加
	 */
	@Override
	public String add(String name,String addr) throws Exception{
		throw new Exception("故意抛出异常,测试切面通知是否生效");
//		return "modifyService add,name=" + name + ",addr=" + addr;
	}

	/**
	 * 修改
	 */
	@Override
	public String edit(Integer id,String name) {
		return "modifyService edit,id=" + id + ",name=" + name;
	}

	/**
	 * 删除
	 */
	@Override
	public String remove(Integer id) {
		return "modifyService id=" + id;
	}
	
}
