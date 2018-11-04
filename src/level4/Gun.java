package level4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Gun
{
	public static int mirrorPoint(int dimension, int position, int originalPoint)
	{
		int result = position;
		int[] rotation = new int[2];

		rotation[0] = position * 2;
		rotation[1] = (dimension - position) * 2;

		if(originalPoint < 0)
			for(int i = originalPoint; i < 0; i++)
			{
				int j = Math.abs((i + 1) % 2);
				result -= rotation[j];
			}
		else
			for(int i = originalPoint; i > 0; i--)
			{
				int j = Math.abs(i % 2);
				result += rotation[j];
			}

		return result;
	}

	public static ArrayList< ArrayList<Integer> > buildMirror(int[] dimensions, int[] position, int distance)
	{
		ArrayList< ArrayList<Integer> > mirror = new ArrayList< >();
		for(int i = 0; i < 2; i++)
		{
			ArrayList<Integer> row = new ArrayList<>();
			int left = -distance / dimensions[i] - 1;
			int right = distance / dimensions[i] + 1;

			for(int j = left; j <= right; j++)
			{
				int mirrorPoint = mirrorPoint(dimensions[i], position[i], j);
				row.add(mirrorPoint);
			}

			mirror.add(row);
		}

		return mirror;
	}

	public static int answer(int[] dimensions, int[] your_position, int[] guard_position, int distance)
	{
		ArrayList< ArrayList<Integer> > yourMirror = buildMirror(dimensions, your_position, distance);
		ArrayList< ArrayList<Integer> > guardMirror = buildMirror(dimensions, guard_position, distance);

		ArrayList< ArrayList<ArrayList<Integer>> > mirrorPlanes = new ArrayList< >();
		mirrorPlanes.add(yourMirror);
		mirrorPlanes.add(guardMirror);

		HashMap<Double, Double> distances = new HashMap<>();
		HashSet<Double> angles = new HashSet<>();

		for(int i = 0; i < 2; i++)
			for(int j : mirrorPlanes.get(i).get(0))
				for(int k : mirrorPlanes.get(i).get(1))
				{
					double beamAngle = Math.atan2(your_position[0] - j, your_position[1] - k);
					double beamDistance = Math.sqrt( Math.pow(your_position[0] - j, 2) + Math.pow(your_position[1] - k, 2) );

					if(!(j == your_position[0] && k == your_position[1]) && beamDistance <= distance)
						if(!distances.containsKey(beamAngle) || distances.containsKey(beamAngle) && beamDistance <= distances.get(beamAngle))
						{
							distances.put(beamAngle, beamDistance);
							if(i == 1)
								angles.add(beamAngle);
						}
				}

		return angles.size();
	}

	public static void main(String[] args)
	{
		int[] dimensions_1 = new int[] {3, 2};
		int[] your_position_1 = new int[] {1, 1};
		int[] guard_position_1 = new int[] {2, 1};
		int distance_1 = 4;

		int[] dimensions_2 = new int[] {300, 275};
		int[] your_position_2 = new int[] {150, 150};
		int[] guard_position_2 = new int[] {185, 100};
		int distance_2 = 500;

		System.out.println(answer(dimensions_1, your_position_1, guard_position_1, distance_1));	//7
		System.out.println(answer(dimensions_2, your_position_2, guard_position_2, distance_2));	//9
	}
}