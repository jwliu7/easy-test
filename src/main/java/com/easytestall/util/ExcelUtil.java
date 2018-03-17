package com.easytestall.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easytestall.constant.RuntimeData;
import com.easytestall.pojo.ParamPojo;
import com.easytestall.pojo.TreeNode;

/**
 * @ClassName： ExcelUtil
 * @Author: dhSu
 * @Description:操作excel
 * @Date:Created in 2018年3月12日
 */


public class ExcelUtil {
	private static final Logger logger = Logger.getLogger(ExcelUtil.class);
     //读取initial。clx，将其每一行放入对象ParamPojo中，整体的excel内容变成一个list<ParamPojo> 放于内存中
	 public static List<ParamPojo> getParamPojoList() throws IOException{
		 List<ParamPojo> listParamPojo = new ArrayList<ParamPojo>();
		 
		 //File file = ResourceUtils.getFile("classpath:initail_tps.xls");
		 File file = ResourceUtils.getFile("D:\\initial_tps.xls");
		 InputStream inputStream = new FileInputStream(file);
		 //HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(ResourceUtils.getFile("classpath:web-info.xls")));
		 HSSFWorkbook book = new HSSFWorkbook(inputStream);
		 HSSFSheet sheet = book.getSheetAt(0);

		 for(int i=1; i<sheet.getLastRowNum()+1; i++) {
	        HSSFRow row = sheet.getRow(i);
	        String batchNum = row.getCell(0).getStringCellValue(); //批次
	        String businessName = row.getCell(1).getStringCellValue(); //业务名称
	        String luaName = row.getCell(2).getStringCellValue();//接口名称
	        String apiUrl = row.getCell(3).getStringCellValue();//接口url
	        String params = row.getCell(4).getStringCellValue();//API URL
	        int rowNum = row.getRowNum();
	        listParamPojo.add(new ParamPojo(batchNum, businessName, luaName, apiUrl, params,rowNum));
		 }
		 book.close();
		 return listParamPojo;
	 }
	 
	 /**
     * 生成不重复的批次级、业务级、接口级节点集合
     * @param List<ParamPojo>
     * @return HashSet<TreeNode>
     * @throws 
	 */
	 public static LinkedHashSet<TreeNode> getTreeNodeSet(List<ParamPojo> listParamPojo){
		 LinkedHashSet<TreeNode> linkedHashSet = new LinkedHashSet<TreeNode>();
		 for(int i = 0;i < listParamPojo.size();i ++) {
			 String batchNum = listParamPojo.get(i).getBatchNum();//批次
			 String businessName = listParamPojo.get(i).getBusinessName();//业务
			 String luaName = listParamPojo.get(i).getLuaName();//接口
			 String attr = batchNum+"_"+businessName+"_"+luaName;
			 int rowNum = listParamPojo.get(i).getRowNum();
			 TreeNode treeNodeBatch = new TreeNode(batchNum, "0", batchNum,false,"",0);//批次级节点
			 TreeNode treeNodeBusine = new TreeNode(businessName, batchNum, businessName,false,"",0);//业务级节点
			 TreeNode treeNodelua = new TreeNode(luaName, businessName, luaName,false,attr,rowNum);//接口级节点
			 linkedHashSet.add(treeNodeBatch);
			 linkedHashSet.add(treeNodeBusine);
			 linkedHashSet.add(treeNodelua);
		 }
		 int count = 0;
		 //遍历linkedHashSet，pid=0的头两个父节点open=true
		 for(TreeNode treeNode:linkedHashSet) {
			 if("0".equals(treeNode.getPid())) {
				 treeNode.setOpen(true);
				 count = count + 1;
				 if(count > 1)
					 break;
			 }
		 }
		 return linkedHashSet;
	 }
	 
	 /**
     * set变json数组
     * @param Set<TreeNode>
     * @return jsonArray
     * @throws 
	 */
     public static JSONArray getJsonArray(Set<TreeNode> linkedHashSet) {
    	 List<TreeNode> list = new ArrayList<TreeNode>();
    	 list.addAll(linkedHashSet);
    	 JSONArray jsonArray = new JSONArray();
    	 for (TreeNode treeNode:list) {
    		 JSONObject jsonObject = (JSONObject)JSON.toJSON(treeNode);
    		 jsonArray.add(jsonObject);
    	 }
    	 return jsonArray;
     } 
     
     /**
      * 生成ztree节点jsonzi字符串
      * @param HashSet<TreeNode>
      * @return String
      * @throws IOException 
 	 */
	 public static String getNodesStr() {
		 return getJsonArray(getTreeNodeSet(RuntimeData.listParamPojo)).toJSONString();
	 }
	 
	 /**
      * 设置单元格内容
      * @param int rowNum
      * @param int colNum
      * @return String
      * @throws IOException 
 	 */
	 public static boolean setCell(int rowNum,int colNum,String cont) {
		boolean status = false;
		String fileName = "D:\\initial_tps.xls";
		try {
			//D:\\initial_tps.xls
			FileInputStream io = new FileInputStream(new File(fileName));
			HSSFWorkbook book = new HSSFWorkbook(io); // 读取的文件
			HSSFSheet  sheet = book.getSheetAt(0);
			HSSFRow row = sheet.getRow(rowNum);
			Cell cell = row.createCell(colNum);
			cell.setCellValue(cont);
			FileOutputStream fo = new FileOutputStream(fileName); // 输出到文件
			book.write(fo);
			book.close();
			fo.close();
			io.close();
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("设置单元格内容时出错:\n" + e.toString() );
			return status;
		}
		return status;
	 }
	 
	 public static void main(String[] args) throws IOException {
		 /*List<ParamPojo> listParamPojo = getParamPojoList();
		 for (ParamPojo paramPojo:listParamPojo) {
			 System.out.println(paramPojo.toString());
		 }*/
		 
		 setCell(0, 0, "妈了逼");
		 
		// System.out.println(getJsonArray(getTreeNodeSet(getParamPojoList())));
	}
}
