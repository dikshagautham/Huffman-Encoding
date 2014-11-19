import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Generic binary tree, storing data of a parametric type in each node
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Scot Drysdale, Winter 2012.  Numerous modifications.
 * @author Tom Cormen, Spring 2014.  Several changes.
 */

public class BinaryTree<E> {
  private BinaryTree<E> left, right;  // children, can be null
  private E data;

  /**
   * Construct a leaf node; left and right are null.
   */
  public BinaryTree(E data) {
    this.data = data;
    this.left = null;
    this.right = null;
  }

  /**
   * Construct a node with the given children.
   */
  public BinaryTree(E data, BinaryTree<E> left, BinaryTree<E> right) {
    this.data = data;
    this.left = left;
    this.right = right;
  }

  /**
   * Is it an internal node?
   */
  public boolean isInternal() {
    return left != null || right != null;
  }

  /**
   * Is it a leaf node?
   */
  public boolean isLeaf() {
    return left == null && right == null;
  }

  /**
   * Does it have a left child?
   */
  public boolean hasLeft() {
    return left != null;
  }

  /**
   * Does it have a right child?
   */
  public boolean hasRight() {
    return right != null;
  }

  /**
   * @return its left child 
   */
  public BinaryTree<E> getLeft() {
    return left;
  }

  /**
   * @return its right child 
   */
  public BinaryTree<E> getRight() {
    return right;
  }

  /**
   * Set its left child to be newLeft.
   * @param newLeft the new left child
   */
  public void setLeft(BinaryTree<E> newLeft) {
    left = newLeft;
  }

  /**
   * Sets its right child to be newRight. 
   * @param newRight the new right child
   */
  public void setRight(BinaryTree<E> newRight) {
    right = newRight;
  }

  /**
   * @return its data value 
   */
  public E getValue() {
    return data;
  }

  /**
   * Set its data value.
   * @param newValue the new data value
   */
  public void setValue(E newValue) {
    data = newValue;
  }

  /**
   * @return the number of nodes (internal and leaf) in the subtree rooted at this node.
   */
  public int size() {
    return 1 + (hasLeft() ? left.size() : 0) + (hasRight() ? right.size() : 0);
  }

  /**
   * @return the length of a longest path to a leaf node from this node.
   */
  public int height() {
    if (isLeaf()) 
      return 0;
    else
      return 1 + Math.max(hasLeft() ? left.height() : 0, hasRight() ? right.height() : 0);
  } 

  /**
   * @return true if the subtrees rooted at this node and other have the same structure and data, false otherwise
   */
  public boolean equals(Object other) {
    if (other instanceof BinaryTree<?>) {
      @SuppressWarnings("unchecked")
      BinaryTree<E> t = (BinaryTree<E>) other;
      return (hasLeft() == t.hasLeft() && hasRight() == t.hasRight()
          && data.equals(t.data) && (!hasLeft() || left.equals(t.left))
          && (!hasRight() || right.equals(t.right)));
    }
    else
      return false;
  }

  /**
   * @return an ArrayList of the data in the leaves of the subtree rooted at this node, in order from left to right
   */
  public ArrayList<E> fringe() {
    ArrayList<E> f = new ArrayList<E>();
    addToFringe(f);
    return f;
  }

  /**
   * Helper method for fringe, adding fringe data to the ArrayList.
   */
  private void addToFringe(ArrayList<E> fringe) {
    if (isLeaf())
      fringe.add(data);
    else {
      if (hasLeft()) 
        left.addToFringe(fringe);
      if (hasRight()) 
        right.addToFringe(fringe);
    }
  }

  /**
   * @return a String representation of the subtree rooted at this node.
   */
  public String toString() {
    return toStringHelper("");
  }

  /**
   * Recursively construct a String representation of the subtree rooted at this node, 
   * starting with the given indentation and indenting further going down the tree.
   * @param indent a String for indenting this node
   * @return the String representation
   */
  private String toStringHelper(String indent) {
    String ret = "";
    
    // If this node has children, indent them two more spaces than this node.
    if (hasRight()) 
      ret += right.toStringHelper(indent + "  ");
    ret += indent + data + "\n";
    if (hasLeft()) 
      ret += left.toStringHelper(indent + "  ");
    return ret;
    
    // Could also write this method as
    //    return (hasRight() ? right.toStringHelper(indent + "  ") : "")
    //        + (indent + data + "\n")
    //        + (hasLeft() ? left.toStringHelper(indent + "  ") : "");
  }

  /** 
   * Create a List storing the the data values in the subtree rooted at this node,
   * ordered according to the preorder traversal of the subtree. 
   * @param dataList the list to be returned.
   */
  public void preorder(List<E> dataList) {
    dataList.add(data);
    if (this.hasLeft())
      left.preorder(dataList);  // recurse on left child
    if (this.hasRight())
      right.preorder(dataList); // recurse on right child
  }

  /** 
   * Create a List storing the the data values in the subtree rooted at this node,
   * ordered according to the inorder traversal of the subtree. 
   * @param dataList the list to be returned.
   */
  public void inorder(List<E> dataList) {
    if (this.hasLeft())
      left.inorder(dataList);   // recurse on left child
    dataList.add(data);
    if (this.hasRight())
      right.inorder(dataList);  // recurse on right child
  }

  /** 
   * Create a List storing the the data values in the subtree rooted at this node,
   * ordered according to the postorder traversal of the subtree. 
   * @param dataList the list to be returned.
   */
  public void postorder(List<E> dataList) {
    if (this.hasLeft())
      left.postorder(dataList);   // recurse on left child
    if (this.hasRight())
      right.postorder(dataList);  // recurse on right child
    dataList.add(data);
  }

  /**
   * Reconstruct a tree from preorder and inorder traversals, assuming that
   * no value is repeated.
   * Precondition: the traversals are valid.  (Otherwise need lots of error checks.)
   * @param preorder the preorder traversal
   * @param inorder the inorder traversal
   * @return the reconstructed tree
   */
  public static <E> BinaryTree<E> reconstructTree (List<E> preorder, List<E> inorder) {
    if (preorder.size() > 0) {  // is this tree non-empty?
      // Create iterators to walk through lists and new lists for recursive calls.
      Iterator<E> iterPre = preorder.iterator();
      Iterator<E> iterIn = inorder.iterator();
      List<E> leftPre = new LinkedList<E>();  // left subtree in preorder
      List<E> rightPre = new LinkedList<E>(); // right subtree in preorder
      List<E> leftIn = new LinkedList<E>();   // left subtree in inorder
      List<E> rightIn = new LinkedList<E>();  // right subtree in inorder

      E rootValue = iterPre.next();  // the first value in the preorder list is from the root
      E inValue = iterIn.next();

      // Find values in the left subtree and copy them into leftPre and leftIn.
      while (!rootValue.equals(inValue)) {
        // Because the root appears before the left subtree, the iteration through
        // the preorder list needs to be one spot ahead of the iteration through
        // the inorder list.
        leftPre.add(iterPre.next());
        leftIn.add(inValue);
        inValue = iterIn.next();        
      }

      // Recursively reconstruct the left subtree.
      BinaryTree<E> leftSubtree = reconstructTree(leftPre, leftIn);

      // Copy values in the right subtree into rightPre and rightIn.
      // The iteration through the inorder list has caught up to the preorder
      // list's iteration.  In other words, the next values in each of these
      // lists come from the right subtree.
      while (iterPre.hasNext()) {
        rightPre.add(iterPre.next());
        rightIn.add(iterIn.next());     
      }

      // Recursively reconstruct the right subtree.
      BinaryTree<E> rightSubtree = reconstructTree(rightPre, rightIn);
      
      // Put the two reconstructed subtrees together with the root,
      // and return the resulting tree.
      return new BinaryTree<E>(rootValue, leftSubtree, rightSubtree);  
    }
    else
      return null;    // empty tree in this case
  }

  /** 
   * Testing program
   */
  public static void main(String [] args) { 
    BinaryTree<Integer> tree = new BinaryTree<Integer>(3,
        new BinaryTree<Integer>(2, new BinaryTree<Integer>(4),
            new BinaryTree<Integer>(1, new BinaryTree<Integer>(6), null)),
        new BinaryTree<Integer>(7, new BinaryTree<Integer>(5), null));

    // Some tests of methods.
    System.out.println("tree: \n" + tree); 
    System.out.println("Size of tree = " + tree.size());
    System.out.println("Height of tree = " + tree.height());
    System.out.println("Fringe of tree = " + tree.fringe());

    // Build a tree from traversals.
    List<Integer> preList = new LinkedList<Integer>();
    tree.preorder(preList);
    System.out.println("Preorder traversal of the tree = " + preList);

    List<Integer> inList = new LinkedList<Integer>();
    tree.inorder(inList);
    System.out.println("Inorder traversal of the tree = " + inList);

    List<Integer> postList = new LinkedList<Integer>();
    tree.postorder(postList);
    System.out.println("Postorder traversal of the tree = " + postList);

    BinaryTree<Integer> tree1 = reconstructTree(preList, inList);
    System.out.println("tree1: \n" + tree1); 
    System.out.println("tree and tree1 are equal? " + tree.equals(tree1));

    tree1.getRight().setValue(8);
    System.out.println("tree: \n" + tree); 
    System.out.println("modified tree1: \n" + tree1); 
    System.out.println("tree and tree1 are equal? " + tree.equals(tree1));

    BinaryTree<Integer> tree2 = reconstructTree(preList, inList);
    tree2.getLeft().setLeft(null);
    System.out.println("tree2: \n" + tree2); 
    System.out.println("Size of tree2 = " + tree2.size());
    System.out.println("Height of tree2 = " + tree2.height());
    System.out.println("Fringe of tree2 = " + tree2.fringe());
    System.out.println("tree and tree2 are equal? " + tree.equals(tree2));
  }   
}
