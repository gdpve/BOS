package com.itheima.bos.service;

import java.util.List;

import com.itheima.bos.domain.Role;
import com.itheima.bos.utils.PageBean;

public interface IRoleService {

	public void save(Role role, String functionIds);

	public void pageQuery(PageBean pageBean);

	public List<Role> findAll();

	public void deleteRoleFunction(String rid);

	public void update(Role model, String functionIds);

}
