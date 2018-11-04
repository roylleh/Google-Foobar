package level2;

import java.util.ArrayList;

public class Code
{
	public static class Result
	{
		public int num = 0;
		public Result() {}
	}

	public static void permute(ArrayList<Integer> input, ArrayList<Integer> temp, Result result)
	{
		int num = 0;
		for(int i : temp)
			num = num * 10 + i;

		if(num % 3 == 0 && num > result.num)
			result.num = num;

		for(int i = 0; i < input.size(); i++)
		{
			int x = input.get(i);

			temp.add(x);
			input.remove(i);

			permute(input, temp, result);

			input.add(i, x);
			temp.remove(temp.size() - 1);
		}
	}

	public static int answer(int[] l)
	{
		ArrayList<Integer> input = new ArrayList<>();
		for(int i : l)
			input.add(i);

		Result result = new Result();
		permute(input, new ArrayList<Integer>(), result);
		return result.num;
	}

	public static void main(String[] args)
	{
		int[] l1 = {3, 1, 4, 1};
		int[] l2 = {3, 1, 4, 1, 5, 9};

		System.out.println(answer(l1));	//4311
		System.out.println(answer(l2));	//94311
	}
}