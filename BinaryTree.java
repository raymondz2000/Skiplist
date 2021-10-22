package comp2402a3;

import java.util.*;
import java.io.PrintWriter;

/**
 * An implementation of binary trees
 * @author morin
 *
 * @param <Node>
 */
public class BinaryTree<Node extends BinaryTree.BTNode<Node>> {

	public static class BTNode<Node extends BTNode<Node>> {
		public Node left;
		public Node right;
		public Node parent;
	}

	/**
	 * An extension of BTNode that you can actually instantiate.
	 */
	protected static class EndNode extends BTNode<EndNode> {
		public EndNode() {
			this.parent = this.left = this.right = null;
		}
	}

	/**
	 * Used to make a mini-factory
	 */
	protected Node sampleNode;

	/**
	 * The root of this tree
	 */
	protected Node r;

	/**
	 * This tree's "null" node
	 */
	protected Node nil;

	/**
	 * Create a new instance of this class
	 *
	 * @param sampleNode - a sample of a node that can be used
	 *                   to create a new node in newNode()
	 * @param nil        - a node that will be used in place of null
	 */
	public BinaryTree(Node sampleNode, Node nil) {
		this.sampleNode = sampleNode;
		this.nil = nil;
		r = nil;
	}

	/**
	 * Create a new instance of this class
	 *
	 * @param sampleNode - a sample of a node that can be used
	 *                   to create a new node in newNode()
	 */
	public BinaryTree(Node sampleNode) {
		this.sampleNode = sampleNode;
	}

	/**
	 * Allocate a new node for use in this tree
	 *
	 * @return
	 */
	@SuppressWarnings({"unchecked"})
	protected Node newNode() {
		try {
			Node u = (Node) sampleNode.getClass().getDeclaredConstructor().newInstance();
			u.parent = u.left = u.right = nil;
			return u;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Compute the depth (distance to the root) of u
	 *
	 * @param u
	 * @return the distanct between u and the root, r
	 */
	public int depth(Node u) {
		int d = 0;
		while (u != r) {
			u = u.parent;
			d++;
		}
		return d;
	}

	/**
	 * Compute the size (number of nodes) of this tree
	 *
	 * @return the number of nodes in this tree
	 * @warning uses recursion so could cause a stack overflow
	 */
	public int size() {
		return size(r);
	}

	/**
	 * @return the size of the subtree rooted at u
	 */
	protected int size(Node u) {
		if (u == nil) return 0;
		return 1 + size(u.left) + size(u.right);
	}

	/**
	 * Compute the number of nodes in this tree without recursion
	 *
	 * @return
	 */
	public int size2() {
		Node u = r, prev = nil, next;
		int n = 0;
		while (u != nil) {
			if (prev == u.parent) {
				n++;
				if (u.left != nil) next = u.left;
				else if (u.right != nil) next = u.right;
				else next = u.parent;
			} else if (prev == u.left) {
				if (u.right != nil) next = u.right;
				else next = u.parent;
			} else {
				next = u.parent;
			}
			prev = u;
			u = next;
		}
		return n;
	}

	/**
	 * Compute the maximum depth of any node in this tree
	 *
	 * @return the maximum depth of any node in this tree
	 */
	public int height() {
		return height(r);
	}

	/**
	 * @return the height of the subtree rooted at u
	 */
	protected int height(Node u) {
		if (u == nil) return -1;
		return 1 + Math.max(height(u.left), height(u.right));
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		toStringHelper(sb, r);
		return sb.toString();
	}

	protected void toStringHelper(StringBuilder sb, Node u) {
		if (u == null) {
			return;
		}
		sb.append('(');
		toStringHelper(sb, u.left);
		toStringHelper(sb, u.right);
		sb.append(')');
	}


	/**
	 * @ return an n-node BinaryTree that has the shape of a random
	 * binary search tree.
	 */
	public static BinaryTree<EndNode> randomBST(int n) {
		Random rand = new Random();
		EndNode sample = new EndNode();
		BinaryTree<EndNode> t = new BinaryTree<EndNode>(sample);
		t.r = randomBSTHelper(n, rand);
		return t;
	}

	protected static EndNode randomBSTHelper(int n, Random rand) {
		if (n == 0) {
			return null;
		}
		EndNode r = new EndNode();
		int ml = rand.nextInt(n);
		int mr = n - ml - 1;
		if (ml > 0) {
			r.left = randomBSTHelper(ml, rand);
			r.left.parent = r;
		}
		if (mr > 0) {
			r.right = randomBSTHelper(mr, rand);
			r.right.parent = r;
		}
		return r;
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return r == nil;
	}

	/**
	 * Make this tree into the empty tree
	 */
	public void clear() {
		r = nil;
	}

	/**
	 * Demonstration of a recursive traversal
	 *
	 * @param u
	 */
	public void traverse(Node u) {
		if (u == nil) return;
		traverse(u.left);
		traverse(u.right);
	}

	/**
	 * Demonstration of a non-recursive traversal
	 */
	public void traverse2() {
		Node u = r, prev = nil, next;
		while (u != nil) {
			if (prev == u.parent) {
				if (u.left != nil) {
					next = u.left;
				} else if (u.right != nil) {
					next = u.right;
				} else {
					next = u.parent;
				}
			} else if (prev == u.left) {
				if (u.right != nil) {
					next = u.right;
				} else {
					next = u.parent;
				}
			} else {
				next = u.parent;
			}
			prev = u;
			u = next;
		}
	}

	/**
	 * Demonstration of a breadth-first traversal
	 */
	public void bfTraverse() {
		Queue<Node> q = new LinkedList<Node>();
		if (r != nil) q.add(r);
		while (!q.isEmpty()) {
			Node u = q.remove();
			if (u.left != nil) q.add(u.left);
			if (u.right != nil) q.add(u.right);
		}
	}

	/**
	 * Find the first node in an in-order traversal
	 *
	 * @return the first node reported in an in-order traversal
	 */
	public Node firstNode() {
		Node w = r;
		if (w == nil) return nil;
		while (w.left != nil)
			w = w.left;
		return w;
	}

	/**
	 * Find the node that follows w in an in-order traversal
	 *
	 * @param w
	 * @return the node that follows w in an in-order traversal
	 */
	public Node nextNode(Node w) {
		if (w.right != nil) {
			w = w.right;
			while (w.left != nil)
				w = w.left;
		} else {
			while (w.parent != nil && w.parent.left != w)
				w = w.parent;
			w = w.parent;
		}
		return w;
	}

	public int totalDepth() {
		// TODO: Your code goes here
		if (r == nil) {
			return -1;
		} else {
			Node u = r, prev = nil, next;
			int current_depth = 0;
			int totalDepth = -1;
			while (u != nil) {
				totalDepth += current_depth;
				if (prev == u.parent) {
					if (u.left != nil) {
						next = u.left;
						current_depth++;
					} else if (u.right != nil) {
						next = u.right;
						current_depth++;
					} else {
						next = u.parent;
						current_depth--;
						totalDepth -= current_depth;
					}
				} else if (prev == u.left) {
					if (u.right != nil) {
						next = u.right;
						current_depth++;
					} else {
						next = u.parent;
						current_depth--;
						totalDepth -= current_depth;
					}
				} else {
					next = u.parent;
					current_depth--;
					totalDepth -= current_depth;
				}
				prev = u;
				u = next;
			}
			return totalDepth;
		}


	}

	public int totalLeafDepth() {
		// TODO: Your code goes here
		if (r == nil) {
			return -1;
		} else {
			Node u = r, prev = nil, next;
			int current_leaf_depth = 0;
			int total_leaf_depth = 0;
			while (u != nil) {
				if (prev == u.parent) {
					if (u.left != nil) {
						next = u.left;
						current_leaf_depth++;
					} else if (u.right != nil) {
						next = u.right;
						current_leaf_depth++;
					} else {
						next = u.parent;
						///
						total_leaf_depth += current_leaf_depth;
						current_leaf_depth--;

					}
				} else if (prev == u.left) {
					if (u.right != nil) {
						next = u.right;
						current_leaf_depth++;
					} else {
						next = u.parent;
						current_leaf_depth--;
					}
				} else {
					next = u.parent;
					current_leaf_depth--;
				}
				prev = u;
				u = next;
			}
			return total_leaf_depth;
		}


	}

	public String bracketSequence() {
		StringBuilder sb = new StringBuilder();
		// TODO: Your code goes here, use sb.append()
		if (r == nil) {
			sb.append(".");
			return sb.toString();
		}
		Node u = r, prev = nil, next;
		while (u != nil) {
			if (prev == u.parent) {
				if (u.left != nil) {
					next = u.left;
					sb.append("(");
				} else if (u.right != nil) {
					next = u.right;
					sb.append("(.");
				} else {
					next = u.parent;
					sb.append("(..)");
				}
			} else if (prev == u.left) {
				if (u.right != nil) {
					next = u.right;
				} else {
					next = u.parent;
					sb.append(".)");
				}
			} else {
				next = u.parent;
				sb.append(")");
			}
			prev = u;
			u = next;
		}
		return sb.toString();
	}

	public void prettyPrint(PrintWriter w) {
		// TODO: Your code goes here
		int x = 0;
		int y = 0;
		int depthprint = 0;

//		int h= totalLeafDepth();
//		int l= totalLeafDepth();

//		System.out.println("height is "+height());
//		System.out.println("total depth  is "+totalLeafDepth());

		String[][] tree = new String[totalDepth()/2][totalDepth()/2];

		Node u = r, prev = nil, next;

		while (u != nil) {
			if (prev == u.parent) {
				tree[y][x] = "*";
				if (u.left != nil) {
					next = u.left;
					tree[y + 1][x] = "|";
					y += 2;

				} else if (u.right != nil) {
					next = u.right;
					tree[y][x+1] = "-";
					x += 2;
				} else {
					next = u.parent;
					if (next != null) {
						if (next.left == u) {
							y -= 2;
						}
					}
				}
			}
			//right part of tree
			else if (prev == u.left) {
				if (u.right != nil) {
					next = u.right;
					int LRL = 0;
					for (int i = 0; i <= x; i++) {
						if ("*".equals(tree[y][i])) {
							LRL = i;
						}
					}
					for (int i = LRL; i <= x; i++) {
						if (null==(tree[y][i])) {
							tree[y][i] = "-";
						}
					}
					tree[y][x + 1] = "-";
					x += 2;
				} else {
					next = u.parent;
					if (next != null) {
						if (next.left == u) {
							y -= 2;
						}
					}
				}
			} else {
				next = u.parent;
				if (u.parent != nil && u == u.parent.left) {
					y -= 2;
				}
			}
			prev = u;
			u = next;
			depthprint = Math.max(y, depthprint);
			//System.out.println("dep is"+y);
		}
		for (int i = 0; i <= depthprint; i++) {
			for (int j = 0; j <= x; j++) {
				if(tree[i][j] == null){
						tree[i][j]=" ";
				}
				w.print(tree[i][j]);
			}
			w.println();
		}
	}
	public static void main (String[]args){

			BinaryTree tree = randomBST(30);
//			PrintWriter w = new PrintWriter(System.out);
//			tree.prettyPrint(w);
//			w.flush();
	}


}

