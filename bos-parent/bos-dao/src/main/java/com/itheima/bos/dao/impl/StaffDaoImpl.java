package com.itheima.bos.dao.impl;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.itheima.bos.dao.IStaffDao;
import com.itheima.bos.dao.base.impl.BaseDaoImpl;
import com.itheima.bos.domain.Staff;

@Repository
public class StaffDaoImpl extends BaseDaoImpl<Staff> implements IStaffDao{


	public void restoreBatch(String id) {
		
		String sql="update bc_staff set deltag=0  where  id="+"'"+id+"'";
		Session session=this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(sql);
		sqlQuery.executeUpdate();
		
	}

}
