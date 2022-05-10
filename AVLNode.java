public class AVLNode<T> {
	private T data;
	private AVLNode<T> father;
	private AVLNode<T> left;
	private AVLNode<T> right;
	private int key;
	private int height;

	/**
	 * constructor for AVL node
	 * @param key int represent in tree
	 * @param data the data contain in node
	 */
	public AVLNode(int key,T data){
		this.key = key;
		this.height =-1;
		this.father =null;
		this.right= null;
		this.left = null;
		this.data = data;
	}

	/**
	Getters for private fields
	 */
	public AVLNode<T> getLeftChild(){
		if (this.left != null)
			return this.left;
		return null;
	}

	public AVLNode<T> getRightChild(){
		if (this.right != null)
			return this.right;
		return null;
	}
	
	public AVLNode<T> getFather(){
		if (this.father != null)
			return this.father;
		return null;
	}
	public int getHeight(){

		return this.height;}

	public int getKey(){
		return this.key;
	}
	
	public T getData(){
		return this.data;
	}
	/**
	Setters for private fields
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	public void setFather(AVLNode<T> father) {
		this.father = father;
	}

	public void setLeftChild(AVLNode<T> left) {
		this.left = left;
		if (left != null)
			left.setFather(this);
	}

	public void setRightChild(AVLNode<T> right) {
		this.right = right;
		if (right != null)
			right.setFather(this);
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public String toString() {
		String s = "";
		if (getLeftChild() != null){
			s+="(";
			s+=getLeftChild().toString();
			s+=")";
		}
		s+=getKey();
		if (getRightChild() != null){
			s+="(";
			s+=getRightChild().toString();
			s+=")";
		}
		return s;
	}
}

