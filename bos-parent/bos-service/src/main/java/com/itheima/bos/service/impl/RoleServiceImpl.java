package com.itheima.bos.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.IRoleDao;
import com.itheima.bos.domain.Function;
import com.itheima.bos.domain.Role;
import com.itheima.bos.service.IRoleService;
import com.itheima.bos.utils.PageBean;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {
	@Autowired
	private IRoleDao dao;
	
	
	/**
	 * 保存一个角色，同时还需要关联权限
	 */
	public void save(Role role, String functionIds) {
		dao.save(role);
		if(StringUtils.isNotBlank(functionIds)){
			String[] fIds = functionIds.split(",");
			for (String functionId : fIds) {
				//手动构造一个权限对象，设置id，对象状态为托管状态
				Function function = new Function(functionId);
				//角色关联权限
				role.getFunctions().add(function);
			}
		}
	}
	
	//删除角色权限和角色
	public void deleteRoleFunction(String id) {
		dao.deleteRoleUser(id);
	  	dao.deleteRoleFuction(id);
	    dao.deleteRole(id); 
	  
	}

	/**
	 * 分页查询
	 */
	public void pageQuery(PageBean pageBean) {
		dao.pageQuery(pageBean);
	}
   
	public List<Role> findAll() {
		return dao.findAll();
	}


	public void update(Role role, String functionIds) {
		
		if(StringUtils.isNotBlank(functionIds)){
			String[] fIds = functionIds.split(",");
			for (String functionId : fIds) {
				//手动构造一个权限对象，设置id，对象状态为托管状态
				Function function = new Function(functionId);
				//角色关联权限
				role.getFunctions().add(function);
			}
		}
		dao.update(role);
	}
}
