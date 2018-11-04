package level2;

public class Tree
{
	/*public static class Node
	{
		public int key;
		public Node left;
		public Node right;
		public Node(int key)
		{
			this.key = key;
		}
	}

	public static int find(Node root, int num)
	{
		Node prev = null;
		Node curr = root;

		while(curr != null)
		{
			if(num == curr.key)
				break;
			else if(curr.left != null && num <= curr.left.key)
			{
				prev = curr;
				curr = curr.left;
			}
			else if(curr.right != null && num <= curr.right.key)
			{
				prev = curr;
				curr = curr.right;
			}
		}

		return prev != null ? prev.key : -1;
	}

	public static Node constructTree(int left, int right, int h)
	{
		if(h < 0)
			return null;

		Node root = new Node(right);
		root.left = constructTree(left, right - (int)Math.pow(2, h), h - 1);
		root.right = constructTree(right - (int)Math.pow(2, h) + 1, right - 1, h - 1);

		return root;
	}*/

	public static int find(int left, int right, int h, int num)
	{
		int prev = -1;
		while(h >= 0)
		{
			int leftUpperBound = right - (int)Math.pow(2, h);
			int rightUpperBound = right - 1;

			if(num == right)
				break;
			else if(num <= leftUpperBound)
			{
				prev = right;
				right = leftUpperBound;
				h--;
			}
			else if(num <= rightUpperBound)
			{
				prev = right;
				left = leftUpperBound + 1;
				right = rightUpperBound;
				h--;
			}
		}

		return prev;
	}

	public static void main(String[] args)
	{
		int h1 = 3;
		int root1 = (int)Math.pow(2, h1) - 1;
		int[] q1 = {7, 3, 5, 1};
		//-1, 7, 6, 3

		int h2 = 5;
		int root2 = (int)Math.pow(2, h2) - 1;
		int[] q2 = {19, 14, 28};
		//21, 15, 29

		for(int i : q1)
			System.out.print(find(1, root1, h1 - 1, i) + ", ");

		System.out.println();

		for(int i : q2)
			System.out.print(find(1, root2, h2 - 1, i) + ", ");
	}
}