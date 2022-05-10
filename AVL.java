import java.util.LinkedList;
import AVLNode;

public class AVL<T> {

	AVLNode<T> root;
	int size;

	public AVL(){
		this.root = null;
		this.size = 0;
	}

	/**
	 * wrapper function that mainly call insert recursive function
	 * @param key int to represent node
	 * @param data the data for the node to contain
	 */
	public void insert(int key, T data){
		// create node to insert the tree
		AVLNode<T> node_to_insert = new AVLNode<>(key,data);
		this.root = insert_recursive(node_to_insert,this.root);
		this.size += 1;
	}

	/**
	 * function that recursive insert node to tree and keep it balance
	 * @param node_to_add AVL node we want to insert
	 * @param root_rec the current root of the tree
	 * @return the tree after balanced
	 */
	public AVLNode<T> insert_recursive(AVLNode<T> node_to_add, AVLNode<T> root_rec){
		// tree is empty
		if (root_rec == null){

			return node_to_add;}

		else if (root_rec.getKey() < node_to_add.getKey())
			root_rec.setRightChild(insert_recursive(node_to_add,root_rec.getRightChild()));
		else if (root_rec.getKey() > node_to_add.getKey())
			root_rec.setLeftChild(insert_recursive(node_to_add,root_rec.getLeftChild()));
		else // Nimrod said that we can assume that there will be not equal keys
			return null;
		return subtree_rotation(root_rec);
	}

	/**
	 * help function to rotate node right
	 * @param unbalance_node node need to rotate
	 * @return subtree after rotation right
	 */


	public AVLNode<T> right_rotation(AVLNode<T> unbalance_node){
		AVLNode<T> temp_L = unbalance_node.getLeftChild();
		AVLNode<T> temp_L_R = unbalance_node.getLeftChild().getRightChild();
		temp_L.setRightChild(unbalance_node);
		unbalance_node.setLeftChild(temp_L_R);
		// now temp_L is the "root"
		this.update_height(unbalance_node);
		this.update_height(temp_L);
		return temp_L;
	}

	/**
	 * help function to rotate node left
	 * @param unbalance_node node need to rotate
	 * @return subtree after rotation left
	 */
	public AVLNode<T> left_rotation(AVLNode<T> unbalance_node){
		AVLNode<T> temp_R = unbalance_node.getRightChild();
		AVLNode<T> temp_R_L = unbalance_node.getRightChild().getLeftChild();
		temp_R.setLeftChild(unbalance_node);
		unbalance_node.setRightChild(temp_R_L);
		// now temp_R is the "root"
		this.update_height(unbalance_node);
		this.update_height(temp_R);
		return temp_R;
	}

	/**
	 * balance the tree using rotations cover all cases
	 * @param unbalance_node - the unbalance subtree
	 * @return AVL node in balance
	 */
	public AVLNode<T> subtree_rotation(AVLNode<T> unbalance_node){
		// the balance level can be 2 or 1 in right side because the balance check goes height(R)-height(L)
		// (-1) or (-2) in this case its in the left side.
		this.update_height(unbalance_node);
		int unbalance_level = this.balance_check(unbalance_node);
		AVLNode<T> left = unbalance_node.getLeftChild();
		AVLNode<T> right = unbalance_node.getRightChild();
		// the balance level 2: we have 2 cases.
		if (unbalance_level > 1){
			// first case the unbalance "go right then left"
			if (this.child_height(right,'R') < this.child_height(right,'L'))
				// right(to right child) --> left rotate. will fix it
				unbalance_node.setRightChild(this.right_rotation(right));
			// otherwise  the unbalance go all the way right so all we need to do is left rotation
			unbalance_node = this.left_rotation(unbalance_node);
		}// same idea the to other way
		else if (unbalance_level <-1) {
			// first case here : left(to left child) --> right rotate. will fix it
			if (this.child_height(left, 'R') > this.child_height(left, 'L'))
				unbalance_node.setLeftChild(this.left_rotation(left));
			// otherwise  the unbalance go all the way left so all we need to do is right rotation
			unbalance_node = this.right_rotation(unbalance_node);
		}// now all cases covers for all 4 kind of unbalance all we need to do is return the "root"
		return unbalance_node;
	}

	/**
	 * update the height that if : take the max height of children and (+1) for itself
	 * @param check_node AVL node
	 */
	public void update_height(AVLNode<T> check_node){
		if (check_node != null){
			// if not null take the max of the children heights, + 1 for the node itself
			int max_height = Math.max(this.child_height(check_node,'R'),this.child_height(check_node,'L'));
			check_node.setHeight(max_height+1);}
	}

	/**
	 * function give us the child height if there is no child return (-1)
	 * @param check_node AVL node to check
	 * @param side - char represent the which child to check 'R' for right otherwise left ('L')
	 * @return the height of the child
	 */
	public int child_height(AVLNode<T> check_node,char side){
		// check for not null right child
		if (side == 'R'){
			if (check_node.getRightChild() == null)
				return -1;
			return check_node.getRightChild().getHeight();}
		// check for not null left child
		else {
			if (check_node.getLeftChild()==null)
				return -1;
			return check_node.getLeftChild().getHeight(); }
	}

	/**
	 * balance check by R-L to apply the height relation
	 * (height < 0 unbalance "go left" || height > 0 unbalance "go right")
	 * @param check_node AVL node
	 * @return the balance of node
	 */

	public int balance_check(AVLNode<T> check_node ){
		if (check_node == null)
			return 0;
		return this.child_height(check_node,'R')-this.child_height(check_node,'L');
	}

	/**
	 * wrapper function: search for value (key) in the AVL mainly call search_rec
	 * @param key key to find
	 * @return data of node with given key
	 */

	public T search(int key){
		if (this.root == null || search_rec(this.getRoot(),key) == null)
			return null;
		return search_rec(this.root,key).getData();
	}

	/**
	 * search recursively in AVL tree
	 * @param the_root root of tree
	 * @param key key need to find
	 * @return the node with given key
	 */
	public AVLNode<T> search_rec(AVLNode<T> the_root,int key){
		// return the node
		if (the_root==null || key == the_root.getKey())
			return the_root;
		//key is smaller then node go right
		else if (key < the_root.getKey())
			return search_rec(the_root.getLeftChild(), key);
		// key is greatest then node go right
		else
			return search_rec(the_root.getRightChild(), key);
	}

	/**
	 * recursive function to find closest keys for range search. runtime: O(log(n))
	 * @param root_check the root of tree to check
	 * @param key key need closest value
	 * @param a low bounder of range
	 * @param b high bounder of range
	 * @return the closest key for given side OR (-5) if dont have key in range
	 */
	public int find_close_key(AVLNode<T> root_check, int key ,int a ,int b) {


		AVLNode<T> temp_node = root_check;
		boolean flag = false;
		int closest = temp_node.getKey();
		int min_difference = 2147483646;

		while (temp_node != null){

			int temp_difference = Math.abs(key - temp_node.getKey());
			// check to update differance
			if (temp_difference < min_difference)
				if (temp_node.getKey() <= b && temp_node.getKey() >= a){
					closest = temp_node.getKey();
					min_difference = temp_difference;
					flag = true;
				}
			if (key > temp_node.getKey())
				temp_node = temp_node.getRightChild();
			else if (key < temp_node.getKey())
				temp_node = temp_node.getLeftChild();
			else
				break;
		}
		if (flag) // we update the original value return closest
			return closest;
		return -5; // dont have item in in range return (-5)
	}

	/**
	 * wrapper function call range_recursive with closest value in range.
	 * @param a low bounder of the close range
	 * @param b high bounder of the close range
	 * @return linked list with the data of nodes in range.
	 */
	public LinkedList<T> nodes_in_range(int a,int b ){

		int x = Math.min(a,b);
		int y = Math.max(a,b);

		int close_to_min = find_close_key(this.root,x,x,y);
		int close_to_max =find_close_key(this.root,y,x,y  );
		LinkedList<T> result_list = new LinkedList<>();
		// (-5) mean that we could not find any key in range close to the given one
		if (close_to_min == -5 || close_to_max == -5)
			return null;

		return range_recursive(close_to_min,close_to_max,this.root,result_list);
	}

	/**
	 * recursive function to find node in range as we learn in practise 6 question 7. we know for sure keys in tree
	 * because we assure it wrapper function.
	 * @param a low bounder - we sure he is in tree
	 * @param b high bounder - we sure is in tree
	 * @param root_check - the root of tree
	 * @param result - empty list to store the results.
	 * @return linked list with the data of nodes in range.
	 */
	public LinkedList<T> range_recursive(int a,int b, AVLNode<T> root_check,LinkedList<T> result){
		if (root_check == null)
			return result;
		if (root_check.getKey() > a)
			range_recursive(a,b,root_check.getLeftChild(),result);
		if (root_check.getKey() >= a && root_check.getKey() <= b)
			result.addFirst(root_check.getData());
		if (root_check.getKey() < b)
			range_recursive(a,b,root_check.getRightChild(),result);
		return result;
	}

	/**
	 * same idea as the range_recursive function but, with another condition to make sure the y value is matching his range
	 * @param x1 low bounder x
	 * @param x2 high bounder x
	 * @param y1 low bounder y
	 * @param y2 high bounder y
	 * @param root_check AVL node the root of the tree we want to check
	 * @param result empty linked list to add the match item
	 * @return linked list with nodes in range
	 */
	public LinkedList<ObjectWithCoordinates> range_2d(int x1,int x2,int y1,int y2,AVLNode<CoordinatesImpl> root_check,LinkedList<ObjectWithCoordinates> result){
		if (root_check == null) // Base case
			return result;
		if (root_check.getKey() > x1)
			range_2d(x1,x2,y1,y2,root_check.getLeftChild(),result);
		if (root_check.getKey() >= x1 && root_check.getKey() <= x2){
			// second condition to check y value in x we find.
			if (root_check.getData().getY() >= y1 && root_check.getData().getY() <= y2)
				result.addFirst(root_check.getData());}
		if (root_check.getKey() < x2)
			range_2d(x1,x2,y1,y2,root_check.getRightChild(),result);
		return result;


	}

	/**
	 * getter for Root
	 * @return root
	 */
	public AVLNode<T> getRoot(){
		return this.root;
	}

	/**
	 * getter for size
	 * @return number nodes in tree
	 */
	public int getSize() {
		return size;
	}

}
