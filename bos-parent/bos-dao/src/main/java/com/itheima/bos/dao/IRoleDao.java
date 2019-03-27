package com.itheima.bos.dao;

import com.itheima.bos.dao.base.IBaseDao;
import com.itheima.bos.domain.Role;

public interface IRoleDao extends IBaseDao<Role> {
 
	void  deleteRoleFuction(String Rid);	
	void   deleteRole(String id); 
	void   deleteRoleUser(String id); 
}
