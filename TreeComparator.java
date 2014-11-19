import java.util.*;




public class TreeComparator<E> implements Comparator<E> {
	
	public TreeComparator() {
	}

	public int compare(Object o1, Object o2) {
		BinaryTree<Singleton> t1 = (BinaryTree<Singleton>) o1;
		BinaryTree<Singleton> t2 = (BinaryTree<Singleton>) o2;

		if (t1.getValue().myCount > t2.getValue().myCount)
			return 1;
		if (t1.getValue().myCount < t2.getValue().myCount)
			return -1;
		else
		return 0;
	}

	
}
