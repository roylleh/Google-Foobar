package level3;

public class Pellet
{
	public static int answer(String n)
	{
		int num = Integer.parseInt(n);
		int ops = 0;

		while(num > 1)
		{
			if(num % 2 == 0)
				num /= 2;
			else if((num - 1 & num - 2) == 0)
				num--;
			else if((num + 1 & num) == 0)
				num++;
			else if((num - 1) / 2 % 2 == 0)
				num--;
			else
				num++;

			ops++;
		}

		return ops;
	}

	public static void main(String[] args)
	{
		for(int i = 1; i <= 8192; i++)
			System.out.println(i + " -> " + answer(Integer.toString(i)));
	}
}