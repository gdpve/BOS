package com.itheima.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.IFunctionDao;
import com.itheima.bos.domain.Function;
import com.itheima.bos.domain.User;
import com.itheima.bos.service.IFunctionService;
import com.itheima.bos.utils.BOSUtils;
import com.itheima.bos.utils.PageBean;

@Service
@Transactional
public class FunctionServiceImpl implements IFunctionService {
	@Autowired
	private IFunctionDao dao;//一定是接口不能是实现类（因为spring帮你们生成的是代理对象）
	public List<Function> findAll() {
		return dao.findAll();
	}
	
	//添加权限
	public void save(Function model) {
		Function parentFunction = model.getParentFunction();
		if(parentFunction != null && parentFunction.getId().equals("")){
			model.setParentFunction(null);
		}
		dao.save(model);
	}
	
	//添加权限
	public void delete(String id)  {
		boolean bool_child=false;
		List<Function> functions = dao.findAllFunction();
		for (Function function : functions) {
			if(function.getpId().equals(id)){
				dao.deleteRoleFunction(function.getId());
				dao.delete(function);
			}	
		}
	   dao.deleteRoleFunction(id);
	   dao.delete(dao.findById(id));
	
	}

	
	public void pageQuery(PageBean pageBean) {
		dao.pageQuery(pageBean);
	}

	/**
	 * 根据当前登录人查询对应的菜单数据，返回json
	 */
	public List<Function> findMenu() {
		List<Function> list = null;
		User user = BOSUtils.getLoginUser();
		if(user.getUsername().equals("admin")){
			//如果是超级管理员内置用户，查询所有菜单
			list = dao.findAllMenu();
		}else{
			//其他用户，根据用户id查询菜单
			list = dao.findMenuByUserId(user.getId());
		}
		return list;
	}
}
