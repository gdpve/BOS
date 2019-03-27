package com.itheima.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.dao.impl.DecidedzoneDaoImpl;
import com.itheima.bos.dao.impl.RegionDaoImpl;
import com.itheima.bos.domain.Decidedzone;
import com.itheima.bos.domain.Region;
import com.itheima.bos.domain.Subarea;
import com.itheima.bos.service.ISubareaService;
import com.itheima.bos.utils.FileUtils;
import com.itheima.bos.utils.PinYin4jUtils;
import com.itheima.bos.web.action.base.BaseAction;
@Controller
@Scope("prototype")
public class SubareaAction extends BaseAction<Subarea>{
	
	//属性驱动，接收上传的文件
	private File subareaFile;
	
	public void setSubareaFile(File subareaFile) {//主要set方法就可以
		this.subareaFile = subareaFile;
	}
	
	
	private String did;
	
	
	
	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}


	@Resource//这个也可以在属性上注入
	private ISubareaService subareaService;
	
	
	@Autowired
	private DecidedzoneDaoImpl decidezonedao;
	
	@Autowired
	private RegionDaoImpl regiondao;
	

	/**
	 * 添加分区
	 */
	public String add(){
		subareaService.save(model);
		return LIST;
	}
	
	/**
	 * 分页查询
	 */
	public String pageQuery(){
		DetachedCriteria dc = pageBean.getDetachedCriteria();
		//动态添加过滤条件
		String addresskey = model.getAddresskey();
		if(StringUtils.isNotBlank(addresskey)){
			//添加过滤条件，根据地址关键字模糊查询
			dc.add(Restrictions.like("addresskey", "%"+addresskey+"%"));
		}
		
		Region region = model.getRegion();
		if(region != null){
			String province = region.getProvince();
			String city = region.getCity();
			String district = region.getDistrict();
			dc.createAlias("region", "r");
			if(StringUtils.isNotBlank(province)){
				//添加过滤条件，根据省份模糊查询-----多表关联查询，使用别名方式实现
				//参数一：分区对象中关联的区域对象属性名称
				//参数二：别名，可以任意
				dc.add(Restrictions.like("r.province", "%"+province+"%"));
			}
			if(StringUtils.isNotBlank(city)){
				//添加过滤条件，根据市模糊查询-----多表关联查询，使用别名方式实现
				//参数一：分区对象中关联的区域对象属性名称
				//参数二：别名，可以任意
				dc.add(Restrictions.like("r.city", "%"+city+"%"));
			}
			if(StringUtils.isNotBlank(district)){
				//添加过滤条件，根据区模糊查询-----多表关联查询，使用别名方式实现
				//参数一：分区对象中关联的区域对象属性名称
				//参数二：别名，可以任意
				dc.add(Restrictions.like("r.district", "%"+district+"%"));
			}
		}
		subareaService.pageQuery(pageBean);
		this.java2Json(pageBean, new String[]{"currentPage","detachedCriteria","pageSize",
						"decidedzone","subareas"});
		return NONE;
	}
	
	/**
	 * 分区数据导出功能
	 * @throws IOException 
	 */
	public String exportXls() throws IOException{
		//第一步：查询所有的分区数据
		List<Subarea> list = subareaService.findAll();
		
		//第二步：使用POI将数据写到Excel文件中
		//在内存中创建一个Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		//创建一个标签页
		HSSFSheet sheet = workbook.createSheet("分区数据");
		//创建标题行
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("分区编号");
		headRow.createCell(1).setCellValue("开始编号");
		headRow.createCell(2).setCellValue("结束编号");
		headRow.createCell(3).setCellValue("位置信息");
		headRow.createCell(4).setCellValue("省市区");
		
		for (Subarea subarea : list) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			dataRow.createCell(0).setCellValue(subarea.getId());
			dataRow.createCell(1).setCellValue(subarea.getStartnum());
			dataRow.createCell(2).setCellValue(subarea.getEndnum());
			dataRow.createCell(3).setCellValue(subarea.getPosition());
			dataRow.createCell(4).setCellValue(subarea.getRegion().getName());
		}
		
		//第三步：使用输出流进行文件下载（一个流、两个头）
		
		String filename = "分区数据.xls";
		String contentType = ServletActionContext.getServletContext().getMimeType(filename);
		ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();
		ServletActionContext.getResponse().setContentType(contentType);
		
		//获取客户端浏览器类型
		String agent = ServletActionContext.getRequest().getHeader("User-Agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("content-disposition", "attachment;filename="+filename);
		workbook.write(out);
		return NONE;
	}
	
	
	
	/**
	 * 分区导入数据
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	public String importXls() throws Exception{
		List<Subarea> subareaList = new ArrayList<Subarea>();
		//使用POI解析Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(subareaFile));
		//根据名称获得指定Sheet对象
		HSSFSheet hssfSheet = workbook.getSheet("Sheet1");
		for (Row row : hssfSheet) {
			int rowNum = row.getRowNum();
			if(rowNum == 0){
				continue;
			}
			String id = row.getCell(0).getStringCellValue();
			
			String regionid = row.getCell(1).getStringCellValue();
			
			Region region= regiondao.findById(regionid);
			String addresskey = row.getCell(2).getStringCellValue();
			String startnum = row.getCell(3).getStringCellValue();
			String endnum = row.getCell(4).getStringCellValue();
			String single = row.getCell(5).getStringCellValue();
			String position = row.getCell(6).getStringCellValue();
			
			//包装一个区域对象
			Subarea subarea = new Subarea(id, null,region,addresskey, startnum, endnum,single, position);
			
			 subareaList.add(subarea);
		}
		//批量保存
		subareaService.saveBatch(subareaList);
		return NONE;
	}
	
	
	
	
	/**
	 * 查询所有未关联到定区的分区，返回json
	 */
	public String listajax(){
		List<Subarea> list = subareaService.findListNotAssociation();
		this.java2Json(list, new String[]{"decidedzone","region"});
		return NONE;
	}
	
	public String listajaxall(){
		List<Subarea> list = subareaService.findListNotAssociation();
		
		List<Subarea> list_decidezone= subareaService.findListByDecidedzoneId(did);
		for (Subarea subarea : list_decidezone) {
			list.add(subarea);
		}
		this.java2Json(list, new String[]{"decidedzone","region"});
		return NONE;
	}
	
	//属性驱动，接收定区id
	private String decidedzoneId;
	
	/**
	 * 根据定区id查询关联的分区
	 */
	public String findListByDecidedzoneId(){
		List<Subarea> list = subareaService.findListByDecidedzoneId(decidedzoneId);
		this.java2Json(list, new String[]{"decidedzone","subareas"});
		return NONE;
	}

	public String getDecidedzoneId() {
		return decidedzoneId;
	}

	public void setDecidedzoneId(String decidedzoneId) {
		this.decidedzoneId = decidedzoneId;
	}
}
