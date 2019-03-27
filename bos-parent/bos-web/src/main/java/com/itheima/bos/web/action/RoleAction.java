package com.itheima.bos.web.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.Role;
import com.itheima.bos.service.IRoleService;
import com.itheima.bos.service.impl.RoleServiceImpl;
import com.itheima.bos.web.action.base.BaseAction;

/**
 * 角色管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role>{
	//属性驱动，接收权限的id
	private String functionIds;
	
	private String Rid;
	
	@Autowired
	private IRoleService roleServiceImpl;
	
	
	
	public void setFunctionIds(String functionIds) {
		this.functionIds = functionIds;
	}
	
	/**
	 * 添加角色
	 */
	public String add(){
		roleServiceImpl.save(model,functionIds);
		return LIST;
	}
	
	

	/**
	 * 修改用户
	 */
	public String update(){
		roleServiceImpl.update(model,functionIds);
		return LIST;
	}
	
	/**
	 * 分页查询
	 */
	public String pageQuery(){
		roleServiceImpl.pageQuery(pageBean);
		this.java2Json(pageBean, new String[]{"functions","users"});
		return NONE;
	}
	/**
	 *删除角色查询
	 */
	public String delete(){
		roleServiceImpl.deleteRoleFunction(Rid);
		return LIST;
	}
	
	
	
	
	/**
	 * 查询所有的角色数据，返回json
	 */
	public String listajax(){
		List<Role> list = roleServiceImpl.findAll();
		this.java2Json(list, new String[]{"functions","users"});
		return NONE;
	}

	public String getRid() {
		return Rid;
	}

	public void setRid(String rid) {
		Rid = rid;
	}



}
