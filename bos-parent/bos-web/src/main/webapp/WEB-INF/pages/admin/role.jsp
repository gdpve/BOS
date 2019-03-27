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
<!-- 导入ztree类库 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/js/ztree/zTreeStyle.css"
	type="text/css" />
<script
	src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.js"
	type="text/javascript"></script>	
<script type="text/javascript">
function doDelete(){
	//获取数据表格中所有选中的行，返回数组对象
	var rows = $("#grid").datagrid("getSelections");
	if(rows.length == 0){
		//没有选中记录，弹出提示
		$.messager.alert("提示信息","请选择需要删除角色！","warning");
	}else{
		//选中了取派员,弹出确认框
		$.messager.confirm("删除确认","你确定要删除选中的角色吗？",function(r){
			if(r){
				if(rows.length==1){
					var id = rows[0].id;
					location.href ='${pageContext.request.contextPath}/roleAction_delete?Rid='+id;
				}
				else{
 					$.messager.alert("提示信息","不支持批量删除！","warning");
				}
			
			}
		});
	}
}

var toolbar = [
 
				{
					id : 'add',
					text : '添加角色',
					iconCls : 'icon-add',
					handler : function(){
						location.href='${pageContext.request.contextPath}/page_admin_role_add.action';
					}
				},
				{
					id : 'delete',
					text : '删除角色',
					iconCls : 'icon-cancel',
					handler :doDelete
				}  
		]
var columns = [[
				{
					field : 'id',
					title : '编号',
					width : 200
				},
				{
					field : 'name',
					title : '名称',
					width : 200
				}, 
				{
					field : 'description',
					title : '描述',
					width : 200
				} 
			]]

	$(function(){
		// 授权树初始化
		var setting = {
			data : {
				key : {
					title : "t"
				},
				simpleData : {
					enable : true
				}
			},
			check : {
				enable : true//使用ztree的勾选效果
			}
		};
		
		
		// 数据表格属性
		$("#grid").datagrid({
			toolbar :toolbar,
			url : 'roleAction_pageQuery.action',
			pagination : true,
			fit:true,
			columns : columns, 
			onDblClickRow : doDblClickRow
		});
		
		// 修改角色窗口
		$('#editStaffWindow').window({
	        title: '修改用户',
	        width: 400,
	        modal: true,//遮罩效果
	        shadow: true,//阴影效果
	        closed: true,//关闭
	        height: 400,
	        resizable:false
	    });
		
		$.ajax({
			url : '${pageContext.request.contextPath}/functionAction_listajax.action',
			type : 'POST',
			dataType : 'json',
			success : function(data) {
				$.fn.zTree.init($("#functionTree"), setting, data);
			},
			error : function(msg) {
				alert('树加载异常!');
			}
		});

	});

function doDblClickRow(rowIndex, rowData) {

	$('#editStaffWindow').window("open");

	$("#editStaffForm").form("load",rowData);
}
	
	
</script>	
</head>
<body class="easyui-layout">
	<div data-options="region:'center'">
		<table id="grid"></table>
	</div>
		<!-- 修改用户窗口 -->
		<div class="easyui-window" title="改变角色信息" id="editStaffWindow" collapsible="false" 
			minimizable="false" maximizable="false" style="top:20px;left:200px width:500px" >
			<div region="north" style="height:31px;width:500px; overflow:hidden;" split="false" border="false" >
				<div class="datagrid-toolbar">
					<a id="edit" icon="icon-edit" href="#" class="easyui-linkbutton" plain="true" >保存</a>
				</div>
			</div>
			<div region="center" style="overflow:auto;padding:5px; width:500px" border="false">
				<form id="editStaffForm" action="roleAction_update.action" method="post">
				<input type="hidden" name="id">
				<input type="hidden" name="functionIds">
				<input type="hidden" name="code">
				<input type="hidden" name="description">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">用户信息</td>
					</tr>
					<tr>
						<td>名称</td>
						 <script type="text/javascript">
								$(function(){
									//为保存按钮绑定事件
									$("#edit").click(function(){
										//根据ztree的id获取ztree对象
										var treeObj = $.fn.zTree.getZTreeObj("functionTree");
										//获取ztree上选中的节点，返回数组对象
										var nodes = treeObj.getCheckedNodes(true);
										var array = new Array();
										for(var i=0;i<nodes.length;i++){
											var id = nodes[i].id;
											array.push(id);
										}
										var functionIds = array.join(",");
										//为隐藏域赋值（权限的id拼接成的字符串）
										$("input[name=functionIds]").val(functionIds);
										$("#editStaffForm").submit();
									});
									
									});
							</script>
						<td><input type="text" name="name" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>授权</td>
						<td>
							<ul id="functionTree" class="ztree"></ul>
						</td>
					</tr>
					</table>
				</form>
			</div>
		</div>
</body>
</html>