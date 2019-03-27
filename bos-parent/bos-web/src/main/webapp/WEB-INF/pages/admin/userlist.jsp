<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">	
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<script type="text/javascript">
	// 工具栏
	var toolbar = [  {
		id : 'button-add',
		text : '新增',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	}];
	


	// 定义标题栏
	var columns = [ [   {
		field : 'id',
		checkbox : true,
	}, {
		field : 'username',
		title : '名称',
		width : 80,
		rowspan : 2
	} ,
	{
		field : 'gender',
		title : '性别',
		width : 60,
		rowspan : 2,
		align : 'center'
	}, {
		field : 'roleNames',
		title : '对应角色',
		width : 300,
		rowspan : 2
	} , {
		field : 'station',
		title : '单位',
		width : 80,
		align : 'center'
	}, {
		field : 'salary',
		title : '工资',
		width : 80,
		align : 'right'
	}, {
		field : 'remark',
		title : '备注',
		width : 120,
		rowspan : 2,
		align : 'center'
	} ] ];
	$(function(){
		// 初始化 datagrid
		// 创建grid
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			toolbar : toolbar,
			url : "userAction_pageQuery.action",
			idField : 'id', 
			columns : columns,
			onDblClickRow : doDblClickRow
		});
		
		$("body").css({visibility:"visible"});
		
		// 修改用户窗口
		$('#editStaffWindow').window({
	        title: '修改用户',
	        width: 400,
	        modal: true,//遮罩效果
	        shadow: true,//阴影效果
	        closed: true,//关闭
	        height: 400,
	        resizable:false
	    });
		
	});
	// 双击
	function doDblClickRow(rowIndex, rowData) {
	
		$('#editStaffWindow').window("open");

		$("#editStaffForm").form("load",rowData);
	}
	
	function doAdd() {
		location.href="${pageContext.request.contextPath}/page_admin_userinfo.action";
	}
	
	function doDelete() {
		//获取数据表格中所有选中的行，返回数组对象
		var rows = $("#grid").datagrid("getSelections");
		if(rows.length == 0){
			//没有选中记录，弹出提示
			$.messager.alert("提示信息","请选择需要删除管理员！","warning");
		}else{
			//选中了取派员,弹出确认框
			$.messager.confirm("删除确认","你确定要删除选中的管理员吗？",function(r){
				if(r){
					if(rows.length==1){
						var id = rows[0].id;
						location.href ='${pageContext.request.contextPath}/userAction_delete?Uid='+id;
					}
					else{
	 					$.messager.alert("提示信息","不支持批量删除！","warning");
					}
				
				}
			});
		}
	}
		
</script>		
</head>
<body class="easyui-layout" style="visibility:hidden;">
    <div region="center" border="false">
    	<table id="grid"></table>
	</div>
		<!-- 修改用户窗口 -->
		<div class="easyui-window" title="改变用户信息" id="editStaffWindow" collapsible="false" 
			minimizable="false" maximizable="false" style="top:20px;left:200px width:500px" >
			<div region="north" style="height:31px;width:500px; overflow:hidden;" split="false" border="false" >
				<div class="datagrid-toolbar">
					<a id="edit" icon="icon-edit" href="#" class="easyui-linkbutton" plain="true" >保存</a>
				</div>
			</div>
			<div region="center" style="overflow:auto;padding:5px; width:500px" border="false">
				<form id="editStaffForm" action="userAction_update.action" method="post">
				<input type="hidden" name="id">
				<input type="hidden" name="password">
				<input type="hidden" name="remark">
				<input type="hidden" name="gender">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">用户信息</td>
					</tr>
					<tr>
						<td>姓名</td>
						<td><input type="text" name="username" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>手机</td>
						<td>
						    <script type="text/javascript">
								$(function(){
									//为保存按钮绑定事件
									$("#edit").click(function(){
										//表单校验，如果通过，提交表单
										var v = $("#editStaffForm").form("validate");
										if(v){
											
											$("#editStaffForm").submit();
										}
									});
									
									});
							</script>
							<input type="text" data-options="validType:'telephone'" 
							name="telephone" class="easyui-validatebox" required="true"/>
						</td>
					</tr>
					<tr>
						<td>单位</td>
						<td><select name="station" id="station" class="easyui-combobox" style="width: 150px;">
	           					<option value="">请选择</option>
	           					<option value="总公司">总公司</option>
	           					<option value="分公司">分公司</option>
	           					<option value="厅点">厅点</option>
	           					<option value="基地运转中心">基地运转中心</option>
	           					<option value="营业所">营业所</option>
	           				</select>
	           			</td>
					</tr>
					<tr>
						<td>选择角色:</td>
	           		    <td  id="roleIds">
	           			<script type="text/javascript">
	           				$(function(){
	           					//页面加载完成后，发送ajax请求，获取所有的角色数据
	           					$.post('roleAction_listajax.action',function(data){
	           						//在ajax回调函数中，解析json数据，展示为checkbox
	           						for(var i=0;i<data.length;i++){
	           							var id = data[i].id;
	           							var name = data[i].name;
	           							$("#roleIds").append('<input id="'+id+'" type="checkbox" name="roleIds" value="'+id+'"><label for="'+id+'">'+name+'</label>');
	           						}
	           					});
	           				});
	           			</script>
	           			</td>
					</tr>
					</table>
				</form>
			</div>
		</div>
</body>
</html>