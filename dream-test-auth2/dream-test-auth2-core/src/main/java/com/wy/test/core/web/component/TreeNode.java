package com.wy.test.core.web.component;

/**
 * 数控件的节点 使用HashMap<String,Object> attr存储节点数据.
 * 
 */
public class TreeNode {

	String key;

	String code;

	String title;

	String codePath;

	String namePath;

	String parentKey;

	String parentCode;

	String parentTitle;

	boolean expanded;

	boolean isLeaf;

	// TreeNode
	Object attrs;

	public TreeNode() {
		super();
	}

	public TreeNode(String key, String title) {
		this.key = key;
		this.title = title;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCodePath() {
		return codePath;
	}

	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}

	public String getNamePath() {
		return namePath;
	}

	public void setNamePath(String namePath) {
		this.namePath = namePath;
	}

	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentTitle() {
		return parentTitle;
	}

	public void setParentTitle(String parentTitle) {
		this.parentTitle = parentTitle;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Object getAttrs() {
		return attrs;
	}

	public void setAttrs(Object attrs) {
		this.attrs = attrs;
	}

}
