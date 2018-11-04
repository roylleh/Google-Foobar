package level1;

public class Cake
{
	public static int answer(String s)
	{
		int n = s.length();
		for(int i = 1; i <= n; i++)
		{
			if(n % i != 0)
				continue;

			String prev = s.substring(0, i);
			boolean flag = true;

			int right = i;
			while(right + i <= n)
			{
				String next = s.substring(right, right + i);
				if(!prev.equals(next))
				{
					flag = false;
					break;
				}
				else
				{
					prev = next;
					right += i;
				}
			}

			if(flag)
				return n / i;
		}

		return n;
	}

	public static void main(String[] args)
	{
		String s1 = "abccbaabccba";
		String s2 = "abcabcabcabc";

		System.out.println(answer(s1));	//2
		System.out.println(answer(s2));	//4
	}
}