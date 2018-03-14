package com.demo.pojo;

/**
 * @ClassName： TreeNode
 * @Author: dhSu
 * @Description:
 * @Date:Created in 2018年3月13日
 */

public class TreeNode {
	private String id;
	private String pid;
	private String name;
	private boolean open;
	private String attr;
	
	

	public TreeNode(String id, String pid, String name, boolean open, String attr) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.open = open;
		this.attr = attr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	
	
	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	
	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", pid=" + pid + ", name=" + name + ", open=" + open + ", attr=" + attr + "]";
	}

	//重写hashcode方法
	public int hashCode()//重写    
    {  
        return id.hashCode() + pid.hashCode() + name.hashCode() ;  
    }  
	
	//重写equals方法
	public boolean equals(Object obj)//重写   Object不能换  
    {  
          if(!(obj instanceof TreeNode))  
              return false;  
          TreeNode p=(TreeNode)obj;  
          return this.id.equals(p.id) && this.pid==p.pid && this.name==p.name;  
    } 
	
}