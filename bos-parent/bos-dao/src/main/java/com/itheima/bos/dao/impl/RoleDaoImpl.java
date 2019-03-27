package com.itheima.bos.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.itheima.bos.dao.IRoleDao;
import com.itheima.bos.dao.base.impl.BaseDaoImpl;
import com.itheima.bos.domain.Function;
import com.itheima.bos.domain.Role;
@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements IRoleDao {
	
	public void deleteRoleFuction(String Rid) {
		String sql="delete  from role_function  where role_id="+"'"+Rid+"'";
		Session session=this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql);
		sqlQuery.executeUpdate();
		
	}

	
	public void deleteRole(String Rid) {
		String sql="delete  from auth_role  where  id="+"'"+Rid+"'";
		Session session=this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql);
		sqlQuery.executeUpdate();
	}


	public void deleteRoleUser(String Rid) {
		String sql="delete  from user_role  where  role_id="+"'"+Rid+"'";
		Session session=this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql);
		sqlQuery.executeUpdate();
	}

}
