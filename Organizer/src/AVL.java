import java.lang.Math;
import java.lang.Exception;
// AVL tree of ints
public class AVL {
	
	private class Node {
		int height;
		int value;
		Node left;
		Node right;
		Node parent;
		
		Node(int h, int v, Node l, Node r, Node p) {
			this.height = h;
			value = v;
			left = l;
			right = r;
			parent = p;
		}
	}
	
	private Node root;
	
	private int size;
	
	public boolean contains(int num) {
		return contains_helper(root, num);
	}

	private boolean contains_helper(Node n, int num) {
		if (n == null) {
			return false;
		}
		if (n.value == num) {
			return true;
		} else if (num > n.value) {
			return contains_helper(n.right, num);
		} else {
			return contains_helper(n.left, num);
		}
	}
	
	public int pop_smallest() throws Exception {
		if (size == 0) {
			throw new Exception("Size = 0; there is nothing to pop");
		}
		if (size == 1) {
			int to_return = root.value;
			root.value = 0;
			size -= 1;
			return to_return;
		} else {
			Node smallest = get_min(root);
			size -= 1;
			return smallest.value;
		}
	}
	
	//make tree
	AVL() {
		root = new Node(0, 0, null, null, null);
		root.parent = root;
		size = 0;
	}
	
	public int get_size() {
		return size;
	}
	
	//true if added valid, false if is duplicate
	public boolean add_node(int num) {
		if (size == 0) {
			root.value = num;
			size = 1;
			return true;
		} else {
			boolean result = add_recurse(root, num);
			if (result) {
				size += 1;
			}
			return result;
		}
	}
	
	//do standard Btree add, update height, rebalance
	private boolean add_recurse(Node n, int num) {
		if (num == n.value)
			return false;
		
		boolean result = false;
		
		if (num > n.value) {
			if (n.right == null) {
				Node newest = new Node(0, num, null, null, n);
				n.right = newest;
				result = true;
			} else {
				result = add_recurse(n.right, num);
			}
		} else if (num < n.value) {
			if (n.left == null) {
				Node newest = new Node(0, num, null, null, n);
				n.left = newest;
				result = true;
			} else {
				result = add_recurse(n.left, num);
			}
		}
		
		if (result) {
			n.height = Math.max(n.left.height, n.right.height) + 1;
			if (n.left.height - n.right.height > 1) {
				CW(n);
			} else if (n.right.height - n.left.height > 1) {
				CCW(n);
			}
			return true;
		} else {
			return false;
		}
	}
	
	//do true if existed and removed, false if doesn't exist
	public boolean remove_node(int num) {
		if (size == 1) {
			root.value = 0;
			size = 0;
			return true;
		} else {
			boolean result = remove_recurse(root, num);
			if (result) {
				size -= 1;
			}
			return result;
		}
	}
	
	//do standard Btree remove, update height, rebalance
	private boolean remove_recurse(Node n, int num) {
		if (n == null) {
			return false;
		}
		
		boolean result = false;
		Node p = n.parent;
		
		if (n.value == num) {
			result = true;
			Node replacement = null;
			if (n == p.left) {
				replacement = get_max(n);
				p.left = replacement;
			} else {
				replacement = get_min(n);
				p.right = replacement;
			}
			replacement.parent = p;
			replacement.left = n.left;
			replacement.right = n.right;
			replacement.height = 1 + Math.max(replacement.left.height, replacement.right.height);
			return true;
		} else if (num > n.value) {
			result = remove_recurse(n.right, num);
		} else {
			result = remove_recurse(n.left, num);
		}
		
		if (result) {
			n.height = 1 + Math.max(n.left.height, n.right.height);
			if (n.left.height - n.right.height > 1) {
				CW(n);
			} else if (n.right.height - n.left.height > 1) {
				CCW(n);
			}
			return true;
		} else {
			return false;
		}
	}
	
	/*        n            l
	 *       / \          / \ 
	 *      l   z   -->  x   n
	 *     / \              / \
	 *     x  y            y   z
	 */
	private void CW(Node n) {
		Node p = n.parent;
		Node l = n.left;
		Node x = n.left.left;
		Node y = n.left.right;
		Node z = n.right;
		if (n == p.left) {
			p.left = l;
		} else {
			p.right = l;
		}
		l.left = x;
		x.parent = l;
		
		l.right = n;
		n.parent = l;
		
		n.left = y;
		y.parent = n;
		
		n.right = z;
		z.parent = n;
		
		n.height = 1 + Math.max(y.height, z.height);
		l.height = 1 + Math.max(x.height, n.height);
	}

	/*        r            n
	 *       / \          / \ 
	 *      n   z   <--  x   r
	 *     / \              / \
	 *     x  y            y   z
	 */
	private void CCW(Node n) {
		Node p = n.parent;
		Node r = n.right;
		Node x = n.left;
		Node y = r.left;
		Node z = r.right;
		if (n == p.left) {
			p.left = r;
		} else {
			p.right = r;
		}
		r.left = n;
		n.parent = r;
		
		r.right = z;
		z.parent = r;
		
		n.left = x;
		x.parent = n;
		
		n.right = y;
		y.parent = n;
	}
	
	//get smallest value node in trees of n
	private Node get_min(Node n) {
		Node p = n.parent;
		if (n.left == null) {
			if (n == p.left) {
				p.left = n.right;
			} else {
				p.right = n.right;
			}
			n.right.parent = p;
			p.height = 1 + Math.max(p.left.height, p.right.height);
			return n;
		} else {
			Node result = get_min(n.left);
			if (result != null) {
				n.height = 1 + Math.max(n.left.height, n.right.height);
			}
			return result;
		}
	}
	
	//get largest value node in trees of n
	private Node get_max(Node n) {
		Node p = n.parent;
		if (n.right == null ) {
			if (n == p.left) {
				p.left = n.left;
			} else {
				p.right = n.left;
			}
			n.left.parent = p;
			p.height = 1 + Math.max(p.left.height, p.right.height);
			return n;
		} else {
			Node result = get_max(n.right);
			if (result != null) {
				n.height = 1 + Math.max(n.left.height, n.right.height);
			}
			return result;
		}
	}	
 }