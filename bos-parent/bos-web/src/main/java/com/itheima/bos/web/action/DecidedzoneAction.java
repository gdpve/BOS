package com.itheima.bos.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.Decidedzone;
import com.itheima.bos.service.IDecidedzoneService;
import com.itheima.bos.web.action.base.BaseAction;
import com.itheima.crm.Customer;
import com.itheima.crm.ICustomerService;

/**
 * 定区管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class DecidedzoneAction extends BaseAction<Decidedzone>{
	
	//属性驱动，接收多个分区id
	private String[] subareaid;
	
	public void setSubareaid(String[] subareaid) {
		
		this.subareaid = subareaid;
		
	}
	
	//属性驱动，接收页面提交的ids参数
	private String ids;
			
	
	@Autowired
	private IDecidedzoneService decidedzoneService;
	
	/**
	 * 添加定区
	 */
	public String add(){
		decidedzoneService.save(model,subareaid);
		return LIST;
	}
	
	/**
	 * 修改定区
	 */
	public String update(){
		decidedzoneService.update(model,subareaid);//subareaid不能为null
		return LIST;
	}
	
	
	/**
	 * 定区批量删除
	 */
	public String deleteBatch(){
			decidedzoneService.deleteBatch(ids);
			return LIST;
	}
	
	
	/**
	 * 分页查询方法
	 */
	public String pageQuery() throws IOException{
		DetachedCriteria dc = pageBean.getDetachedCriteria();
		//动态添加过滤条件
		
		if(StringUtils.isNotBlank(model.getId())){
			//添加过滤条件，根据地址关键字模糊查询
			dc.add(Restrictions.eq("id", model.getId()));
		}
		if(StringUtils.isNotBlank(model.getName())){
			//添加过滤条件，根据地址关键字模糊查询
			dc.add(Restrictions.eq("name", model.getName()));
		}
		decidedzoneService.pageQuery(pageBean);
		this.java2Json(pageBean, new String[]{"currentPage","detachedCriteria",
						"pageSize","subareas","decidedzones"});
		return NONE;
	}
	
	//注入crm代理对象
	@Autowired
	private ICustomerService proxy;
	
	/**
	 * 远程调用crm服务，获取未关联到定区的客户
	 */
	public String findListNotAssociation(){
		List<Customer> list = proxy.findListNotAssociation();
		this.java2Json(list, new String[]{});
		return NONE;
	}
	
	/**
	 * 远程调用crm服务，获取已经关联到指定的定区的客户
	 */
	public String findListHasAssociation(){
		String id = model.getId();
		List<Customer> list = proxy.findListHasAssociation(id);
		this.java2Json(list, new String[]{});
		return NONE;
	}
	
	//属性驱动，接收页面提交的多个客户id
	private List<Integer> customerIds;
	
	/**
	 * 远程调用crm服务，将客户关联到定区
	 */
	public String assigncustomerstodecidedzone(){
	    proxy.assigncustomerstodecidedzone(model.getId(), customerIds);
		return LIST;
	}
	


	public List<Integer> getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(List<Integer> customerIds) {
		this.customerIds = customerIds;
	}
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}
