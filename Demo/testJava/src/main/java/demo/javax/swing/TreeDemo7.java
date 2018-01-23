package demo.javax.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class TreeDemo7 implements TreeSelectionListener, ActionListener {
	public static void main(String[] args) {
		new TreeDemo7();
	}
	
	JButton next = new JButton("next");
	JButton front = new JButton("previous");
	JToolBar toolbar = new JToolBar();
	JPanel panel = new JPanel();
	JEditorPane editorPane;
	String path;
	JTree tree;

	public TreeDemo7() {
		next.addActionListener(this);
		front.addActionListener(this);
		panel.add(toolbar);
		toolbar.add(next);
		toolbar.add(front);
		JFrame f = new JFrame("TreeDemo");
		Container contentPane = f.getContentPane();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("资源管理器");
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("TreeDemo1.java");
		root.add(node);
		node = new DefaultMutableTreeNode("TreeDemo2.java");
		root.add(node);
		node = new DefaultMutableTreeNode("TreeDemo3.java");
		root.add(node);
		node = new DefaultMutableTreeNode("TreeDemo4.java");
		root.add(node);

		tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);

		JScrollPane scrollPane1 = new JScrollPane(tree);
		editorPane = new JEditorPane();
		JScrollPane scrollPane2 = new JScrollPane(editorPane);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, scrollPane1, scrollPane2);

		contentPane.add(panel, BorderLayout.NORTH);
		contentPane.add(splitPane, BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void valueChanged(TreeSelectionEvent e) {
		JTree tree = (JTree) e.getSource();
		DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		String nodeName = selectionNode.toString();

		if (selectionNode.isLeaf()) {
			String filepath = "file:" + System.getProperty("user.dir") + System.getProperty("file.separator")
					+ nodeName;
			path = System.getProperty("user.dir") + System.getProperty("file.separator") + nodeName;
			System.out.println(path);

			try {
				editorPane.setPage(filepath);
			} catch (IOException ex) {
				System.out.println("找不到此文件");
			}
		}
	}

	

	int i = 0;

	public void actionPerformed(ActionEvent e) {
		i++;
		int key = i % 4 + 1;
		if (e.getSource() == next) {

			System.out.println("ok ok ok");
			DefaultMutableTreeNode node = new DefaultMutableTreeNode("TreeDemo4.java");

			TreePath tp = new TreePath(node);

			tree.setSelectionRow(key);
		}

		if (e.getSource() == front) {
		}
	}
}
