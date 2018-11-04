package level3;

public class Stair
{
	public static int answer(int n)
	{
		int[] memo = new int[n + 1];
		memo[0] = 1;
		memo[1] = 1;

		for(int i = 2; i <= n; i++)
			for(int j = n; j >= i; j--)
				memo[j] += memo[j - i];

		return memo[n] - 1;
	}

	public static void main(String[] args)
	{
		System.out.println(answer(3));		//1
		System.out.println(answer(4));		//1
		System.out.println(answer(5));		//2
		System.out.println(answer(6));		//3
		System.out.println(answer(7));		//4
		System.out.println(answer(8));		//5
		System.out.println(answer(9));		//7
		System.out.println(answer(10));		//9
		System.out.println(answer(20));		//63
		System.out.println(answer(200));	//487067745
	}
}