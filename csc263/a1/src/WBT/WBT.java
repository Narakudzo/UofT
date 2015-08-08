package WBT;

public class WBT<K extends Comparable<K>>
{
  Node<K> root;
  /* Normally this should be private or protected. But I'm doing whitebox
     testing (of the tree shape and content), I need access from the outside.
  */

  public WBT() {
    root = null;
  }

  private static int size(Node<?> u) {
    return u == null ? 0 : u.num;
  }

  public void insert(K x) {
	  
	  if (root != null) {
		  root.num += 1;
	  }
	  
	  root = addNode(root, null, x);
	  root = balanceNode(root);

	  // When single-rotation did not balance root,
	  // re-balance root again with the second case.
	  int leftCount = 0;
	  int rightCount = 0;
	  if (root.left != null) {
		  leftCount = size(root.left);
	  }
	  if (root.right != null) {
		  rightCount = size(root.right);
	  }
	  if (((leftCount + 1) * 3) < (rightCount + 1) ||
			  (leftCount + 1) > ((rightCount + 1) * 3)) {
		  root = balanceNode2(root);
	  }
	  
	  // Check if the left-subtree requires case 2 re-balancing.
	  leftCount = 0;
	  rightCount = 0;
	  if (root.left != null && root.left.left != null) {
		  leftCount = size(root.left.left);
	  }
	  if (root.left != null && root.left.right != null) {
		  rightCount = size(root.left.right);
	  }
	  if (((leftCount + 1) * 2) < (rightCount + 1) ||
			  (leftCount + 1) > ((rightCount + 1) * 2)) {
		  root.left = balanceNode2(root.left);
	  }
	  
	// Check if the left-subtree requires case 2 rebalancing.
	  leftCount = 0;
	  rightCount = 0;
	  if (root.right != null && root.right.left != null) {
		  leftCount = size(root.right.left);
	  }
	  if (root.right != null && root.right.right != null) {
		  rightCount = size(root.right.right);
	  }
	  if (((leftCount + 1) * 2) < (rightCount + 1) ||
			  (leftCount + 1) > ((rightCount + 1) * 2)) {
		  root.right = balanceNode2(root.right);
	  }
	  
				  
  }
  
  // Insert node to an appropriate position.
  private Node<K> addNode(Node<K> tree, Node<K> parent, K item) {
	  if (tree == null) {
		  Node<K> node = new Node<K>();
		  node.left = null;
		  node.right = null;
		  node.key = item;
		  node.num = 1;
		  node.parent = parent;
		  return node;
	  }
	  else if (item.compareTo(tree.key) < 0) {
		  if(tree.left != null) {
			  tree.left.num += 1;
		  }
		  tree.left = addNode(tree.left, tree, item);
	  } else {
		  if(tree.right != null) {
			  tree.right.num += 1;
		  }
		  tree.right = addNode(tree.right, tree, item);
	  }
	  return tree;
  }
  
  // Single rotation recursively.
  private Node<K> balanceNode(Node<K> v) {
	  if (v == null) {
		  return null;
	  }
	  
	  int leftCount = 0;
	  int rightCount = 0;
	  if (v.left != null) {
		  leftCount = v.left.num;
	  }
	  if (v.right != null) {
		  rightCount = v.right.num;
	  }
	  
	  if (((leftCount + 1) * 3) < (rightCount + 1)) {
		  v.right = balanceNode(v.right);
		  v = leftRotate(v, v.left, v.right);
	  }
	  else if ((leftCount + 1) > ((rightCount + 1) * 3)) {
		  v.left = balanceNode(v.left);
		  v = rightRotate(v, v.left, v.right);
	  }
	  else if (((leftCount + 1) * 2) < (rightCount + 1)) {
		  v.right = balanceNode(v.right);
	  }
	  else if ((leftCount + 1) > ((rightCount + 1) * 2)) {
		  v.left = balanceNode(v.left);
	  }
	  return v;
  }
  
  // Double rotation recursively (case 2 rotation for subtrees).
  private Node<K> balanceNode2(Node<K> v) {
	  if (v == null) {
		  return null;
	  }
	  
	  int leftCount = 0;
	  int rightCount = 0;
	  if (v.left != null) {
		  leftCount = v.left.num;
	  }
	  if (v.right != null) {
		  rightCount = v.right.num;
	  }
	  
	  if (((leftCount + 1) * 3) < (rightCount + 1)) {
		  v.right = balanceNode2(v.right);
		  v = leftRotate(v, v.left, v.right);
	  }
	  else if ((leftCount + 1) > ((rightCount + 1) * 3)) {
		  v.left = balanceNode2(v.left);
		  v = rightRotate(v, v.left, v.right);
	  }
	  else if (((leftCount + 1) * 2) < (rightCount + 1)) {
		  v.right = balanceNode2(v.right);
		  v = leftRotate(v, v.left, v.right);
	  }
	  else if ((leftCount + 1) > ((rightCount + 1) * 2)) {
		  v.left = balanceNode2(v.left);
		  v = rightRotate(v, v.left, v.right);
	  }
	  return v;
  }
  
  // Rotate to the left.
  private Node<K> leftRotate(Node<K> v, Node<K> left, Node<K> right) {
	  v.left = left;
	  v.right = right.left;
	  v.num -= right.num;
	  if (right.left != null) {
		  v.num += right.left.num;
		  right.num -= right.left.num;
	  }
	  right.num += v.num;
	  right.parent = v.parent;
	  v.parent = right;
	  right.left = v;
	  //v = right;
	  return right;
  }
  
  // Rotate to the right.
  private Node<K> rightRotate(Node<K> v, Node<K> left, Node<K> right) {
	  v.left = left.right;
	  v.right = right;
	  v.num -= left.num;
	  if (left.right != null) {
		  v.num += left.right.num;
		  left.num -= left.right.num;
	  }
	  left.num += v.num;
	  left.parent = v.parent;
	  v.parent = left;
	  left.right= v;
	  //v = left;
	  return left;
  }
}
