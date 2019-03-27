package com.itheima.bos.dao.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.itheima.bos.dao.IUserDao;
import com.itheima.bos.dao.base.impl.BaseDaoImpl;
import com.itheima.bos.domain.User;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements IUserDao {
	/**
	 * 根据用户名和密码查询用户
	 */
	public User findUserByUsernameAndPassword(String username, String password) {
		String hql = "FROM User u WHERE u.username = ? AND u.password = ?";
		List<User> list = (List<User>) this.getHibernateTemplate().find(hql, username,password);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	public User findUserByUsername(String username) {
		String hql = "FROM User u WHERE u.username = ?";
		List<User> list = (List<User>) this.getHibernateTemplate().find(hql, username);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	
	public void deleteUserRole(String uid) {
		String sql="delete  from user_role  where user_id="+"'"+uid+"'";
		Session session=this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql);
		sqlQuery.executeUpdate();
		
	}

	

	
	public void deleteUser(String uid) {
		String sql="delete  from t_user  where id="+"'"+uid+"'";
		Session session=this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql);
		sqlQuery.executeUpdate();
	}
}
