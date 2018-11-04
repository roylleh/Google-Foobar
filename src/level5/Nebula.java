package level5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Nebula
{
	public static HashMap<String, Integer> cache = new HashMap<>();

	public static int singleGas(int i, int j, int cols)
	{
		String key = i + "_" + j + "_" + cols;
		if(cache.containsKey(key))
			return cache.get(key);

		int a = i & ~(1 << cols);
		int b = j & ~(1 << cols);
		int c = i >> 1;
		int d = j >> 1;

		int e = a & ~b & ~c & ~d | ~a & b & ~c & ~d | ~a & ~b & c & ~d | ~a & ~b & ~c & d;
		cache.put(key, e);
		return e;
	}

	public static HashMap<String, HashSet<Integer>> buildMap(ArrayList<Integer> nums, int cols)
	{
		HashSet<Integer> set = new HashSet<>();
		for(int i : nums)
			set.add(i);

		HashMap<String, HashSet<Integer>> map = new HashMap<>();
		for(int i = 0; i < 1 << cols + 1; i++)
			for(int j = 0; j < 1 << cols + 1; j++)
			{
				int singleGas = singleGas(i, j, cols);
				if(set.contains(singleGas))
				{
					String key = singleGas + "_" + i;
					if(!map.containsKey(key))
						map.put(key, new HashSet<>());

					map.get(key).add(j);
				}
			}

		return map;
	}

	public static int answer(boolean[][] g)
	{
		int rows = g.length;
		int cols = g[0].length;

		boolean[][] transpose = new boolean[cols][rows];
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++)
				transpose[j][i] = g[i][j];

		rows = transpose.length;
		cols = transpose[0].length;

		ArrayList<Integer> nums = new ArrayList<>();
		for(int i = 0; i < rows; i++)
		{
			int sum = 0;
			for(int j = 0; j < cols; j++)
				if(transpose[i][j])
					sum += 1 << j;

			nums.add(sum);
		}

		HashMap<String, HashSet<Integer>> map = buildMap(nums, cols);
		HashMap<Integer, Integer> prev = new HashMap<>();
		for(int i = 0; i < 1 << cols + 1; i++)
			prev.put(i, 1);

		for(int i : nums)
		{
			HashMap<Integer, Integer> next = new HashMap<>();
			for(int j : prev.keySet())
			{
				if(!map.containsKey(i + "_" + j))
					continue;

				if(!prev.containsKey(j))
					prev.put(j, 1);

				int prevValue = prev.get(j);
				HashSet<Integer> row = map.get(i + "_" + j);
				for(int k : row)
				{
					if(!next.containsKey(k))
						next.put(k, 0);

					next.put(k, next.get(k) + prevValue);
				}
			}

			prev = next;
		}

		int result = 0;
		for(int value : prev.values())
			result += value;

		return result;
	}

	public static void main(String[] args)
	{
		boolean[][] g1 = new boolean[][] {
			{true, false, true},
			{false, true, false},
			{true, false, true}
		};

		boolean[][] g2 = new boolean[][] {
			{true, false, true, false, false, true, true, true},
			{true, false, true, false, false, false, true, false},
			{true, true, true, false, false, false, true, false},
			{true, false, true, false, false, false, true, false},
			{true, false, true, false, false, true, true, true}
		};

		boolean[][] g3 = new boolean[][] {
			{true, true, false, true, false, true, false, true, true, false},
			{true, true, false, false, false, false, true, true, true, false},
			{true, true, false, false, false, false, false, false, false, true},
			{false, true, false, false, false, false, true, true, false, false}
		};

		System.out.println(answer(g1));	//4
		System.out.println(answer(g2));	//254
		System.out.println(answer(g3));	//11567
	}
}



/*
combinations = [(0, 0), (0, 1), (1, 0), (1, 1)]
possibilities = {}


def buildGrid2(firstState, row):
    grid2 = []
    for key in firstState:
        prevState = []
        for combination in combinations:
            if possibilities[((key[0], key[1]), combination)] == row[0]:
                prevState.append(combination)

        for i in range(1, len(row)):
            nextState = []
            for state in prevState:
                for j in range(2):
                    if possibilities[((key[i], key[i + 1]), (state[i], j))] == row[i]:
                        temp = list(state)
                        temp.append(j)
                        nextState.append(temp)

            prevState = nextState

        for state in prevState:
            grid2.append((key, tuple(state)))

    return grid2


def buildGrid1(row):
    prevState = []
    currState = row[0]
    for key, val in possibilities.items():
        if currState == val:
            prevState.append(key)

    for i in range(1, len(row)):
        nextState = []
        for state in prevState:
            for combination in combinations:
                if possibilities[(state[i], combination)] == row[i]:
                    temp = list(state)
                    temp.append(combination)
                    nextState.append(temp)

        prevState = nextState

    return [tuple(zip(*state)) for state in prevState]


def answer(g):
    for i in range(2):
        for j in range(2):
            for k in range(2):
                for l in range(2):
                    possibilities[((i, j), (k, l))] = i & ~j & ~k & ~l | ~i & j & ~k & ~l | ~i & ~j & k & ~l | ~i & ~j & ~k & l

    transpose = tuple(zip(*g))
    grid1 = buildGrid1(transpose[0])
    firstState = {}
    for row in grid1:
        key = row[1]
        if key in firstState:
            firstState[key] += 1
        else:
            firstState[key] = 1

    for i in range(1, len(transpose)):
        secondState = {}
        grid2 = buildGrid2(firstState, transpose[i])
        for row in grid2:
            key1 = row[0]
            key2 = row[1]
            if key1 in firstState:
                if key2 in secondState:
                    secondState[key2] += firstState[key1]
                else:
                    secondState[key2] = firstState[key1]

        firstState = secondState

    result = 0
    for key, value in firstState.items():
        result += value

    return result
 */