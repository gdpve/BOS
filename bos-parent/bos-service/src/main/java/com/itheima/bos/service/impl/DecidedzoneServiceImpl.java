package com.itheima.bos.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.IDecidedzoneDao;
import com.itheima.bos.dao.ISubareaDao;
import com.itheima.bos.domain.Decidedzone;
import com.itheima.bos.domain.Subarea;
import com.itheima.bos.service.IDecidedzoneService;
import com.itheima.bos.utils.PageBean;
@Service
@Transactional
public class DecidedzoneServiceImpl implements IDecidedzoneService {
	@Autowired
	private IDecidedzoneDao decidedzoneDao;
	@Autowired
	private ISubareaDao subareaDao;
	
	/**
	 * 添加定区，同时关联分区
	 */
	public void save(Decidedzone model, String[] subareaid) {
		decidedzoneDao.save(model);
		for (String id : subareaid) {
			Subarea subarea = subareaDao.findById(id);
			//model.getSubareas().add(subarea);一方（定区）已经放弃维护外键权利，只能由多方（分区）负责维护
			subarea.setDecidedzone(model);//分区关联定区
		}
	}

	public void pageQuery(PageBean pageBean) {
		decidedzoneDao.pageQuery(pageBean);
	}

	
	public void update(Decidedzone model, String[] subareaid) {
		 
//	    String  decidedzoneid=.getId();
	    DetachedCriteria dc =  DetachedCriteria.forClass(Subarea.class);
	    dc.add(Restrictions.eq("decidedzone", model));
        List<Subarea> subareas = subareaDao.findByCriteria(dc);
        for (Subarea subarea : subareas) {
		    subarea.setDecidedzone(null);
		    subareaDao.update(subarea);
		}
		for (String id : subareaid) {
			Subarea subarea = subareaDao.findById(id);
			//model.getSubareas().add(subarea);一方（定区）已经放弃维护外键权利，只能由多方（分区）负责维护
			subarea.setDecidedzone(model);//分区关联定区
		}
	}


	public void deleteBatch(String ids) {
		if(StringUtils.isNotBlank(ids)){
			String[] staffIds = ids.split(",");
			for (String id : staffIds) {
				DetachedCriteria dc =  DetachedCriteria.forClass(Subarea.class);
			    dc.add(Restrictions.eq("decidedzone",decidedzoneDao.findById(id) ));
		        List<Subarea> subareas = subareaDao.findByCriteria(dc);
		        for (Subarea subarea : subareas) {
				    subarea.setDecidedzone(null);
				    subareaDao.update(subarea);
				}
				decidedzoneDao.delete(decidedzoneDao.findById(id));
			}
		}
		
	}
}
