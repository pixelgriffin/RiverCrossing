import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class Tree {
	public class Node {
		public Node parent;
		public boolean visited = false;
		public HashSet<Node> children = new HashSet<Node>();
		
		public int c, m, b;
		public int d;
		
		public int i, j;
		
		public Node(int _c, int _m, int _b, int depth, Node p, int lastI, int lastJ) {
			c = _c;
			m = _m;
			b = _b;
			
			d = depth;
			parent = p;
			
			i = lastI;
			j = lastJ;
		}
		
		public String getString() {
			return "(depth: " + d + ")<" + c +", " + m + ", " + b + ">" + ".....(" + i + ", " + j + ").....<" + (Tree.CANN_MAX - c) + ", " + (Tree.MISS_MAX - m) + ", " + (b == 0 ? 1 : 0) + ">";
		}
		
		public void populate(LinkedList<Node> list) {
			list.addLast(this);
			if(this.parent != null) {
				this.parent.populate(list);
			}
		}
		
		public Node generateChildren(int boatMax) {
			//System.out.println(getString());
			
			if(d == 15)
				return null;
			
			for(int i = 0; i < c + 1; i++) {
				for(int j = 0; j < m + 1; j++) {
					if((i + j > boatMax) || (i + j == 0) || (i > j)) {
						continue;
					}
					
					if(b == 1) {
						Node child = new Node(c - i, m - j, 0, d + 1, this, i, j);
						if(child.isAnswer()) {
							return child;
						}
						if(child.isValid()) {
							children.add(child);
						}
					} else {
						Node child = new Node(c + i, m + j, 1, d + 1, this, i, j);
						if(child.isValid()) {
							children.add(child);
						}
					}
				}
			}
			
			return null;
		}
		
		public boolean isValid() {
			return m >= c && m <= Tree.MISS_MAX && c <= Tree.CANN_MAX && m > 0 && c > 0;
		}
		
		public boolean isAnswer() {
			return m == 0 && c == 0 && b == 0;
		}
		
		public void unvisit() {
			visited = false;
			
			for(Node n : children) {
				n.visited = false;
				n.unvisit();
			}
		}
	}
	
	//edit missionary count, cannibal count, and boat maximum here.
	public static final int MISS_MAX = 3;
	public static final int CANN_MAX = 3;
	public static final int BOAT_MAX = 3;
	
	private Node root = new Node(CANN_MAX, MISS_MAX, 1, 0, null, 0, 0);
	
	public void bfs() {
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(this.root);
		root.visited = true;
		root.generateChildren(BOAT_MAX);
		
		while(!queue.isEmpty()) {
			Node n = (Node)queue.remove();
			Node child = null;
			
			while((child = getUnivistedChild(n)) != null) {
				child.visited = true;
				
				Node ans = child.generateChildren(BOAT_MAX);
				if(ans != null) {
					LinkedList<Node> nodeList = new LinkedList<Node>();
					ans.populate(nodeList);
					
					for(Node listNode : nodeList) {
						System.out.println(listNode.getString());
					}
					
					//System.out.println("Solution returned:" + ans.getString());
					return;
				}
				
				queue.add(child);
			}
		}
		
		//this.root.unvisit();
		System.out.println("No solution");
	}
	
	public void dfs() {
		Stack<Node> stack = new Stack<Node>();
		stack.push(this.root);
		root.visited = true;
		root.generateChildren(BOAT_MAX);
		
		while(!stack.isEmpty()) {
			Node node = stack.peek();
			Node child = getUnivistedChild(node);
			if(child != null) {
				child.visited = true;
				child.generateChildren(BOAT_MAX);
				stack.push(child);
			} else {
				stack.pop();
			}
		}
	}
	
	private Node getUnivistedChild(Node n) {
		for(Node node : n.children) {
			if(!node.visited) {
				return node;
			}
		}
		
		return null;
	}
}