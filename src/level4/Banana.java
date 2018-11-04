package level4;

import java.util.ArrayList;
import java.util.HashSet;

public class Banana
{
	public static int gcd(int x, int y)
	{
		if(y == 0)
			return x;

		return gcd(y, x % y);
	}

	public static boolean isLoop(int x, int y)
	{
		int z = (x + y) / gcd(x, y);
		return (z & z - 1) >= 1;
	}

	public static int answer(int[] banana_list)
	{
		int n = banana_list.length;
		ArrayList< HashSet<Integer> > guards = new ArrayList< >();

		for(int i = 0; i < n; i++)
		{
			guards.add(new HashSet<Integer>());
			for(int j = 0; j < n; j++)
				if(isLoop(banana_list[i], banana_list[j]))
					guards.get(i).add(j);
		}

		for(int i = 0; i < n; i++)
		{
			int currPairs = guards.get(i).size();
			if(currPairs <= 1)
				continue;

			while(currPairs >= 2)
			{
				int indexWithMostPairs = -1;
				for(int j : guards.get(i))
					if(indexWithMostPairs == -1 || guards.get(j).size() > guards.get(indexWithMostPairs).size())
						indexWithMostPairs = j;

				guards.get(i).remove(indexWithMostPairs);
				guards.get(indexWithMostPairs).remove(i);
				currPairs--;
			}
		}

		int result = 0;
		for(HashSet<Integer> row : guards)
			if(row.isEmpty())
				result++;

		return result;
	}

	public static void main(String[] args)
	{
		int[] banana_list_1 = new int[] {1, 1};
		int[] banana_list_2 = new int[] {1, 7, 3, 21, 13, 19};

		System.out.println(answer(banana_list_1));	//2
		System.out.println(answer(banana_list_2));	//0
	}
}