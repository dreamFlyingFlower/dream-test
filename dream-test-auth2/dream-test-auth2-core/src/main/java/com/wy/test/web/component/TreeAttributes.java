package com.wy.test.web.component;

import java.util.ArrayList;

/**
 * 数控件节点列表 列表的元素为TreeNode
 * 
 */
public class TreeAttributes {

	TreeNode rootNode;

	int nodeCount;

	ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();

	public ArrayList<TreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<TreeNode> nodes) {
		this.nodes = nodes;
	}

	public TreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}

	/**
	 * 新增节点到列表
	 * 
	 * @param treeNode
	 */
	public void addNode(TreeNode treeNode) {
		this.nodes.add(treeNode);
	}

}
