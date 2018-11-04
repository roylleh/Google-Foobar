package level3;

import java.util.ArrayList;

public class Ore
{
	public static int statesNum;
	public static boolean[] terminalStates;
	public static ArrayList< ArrayList<Double> > rMatrix = new ArrayList< >();
	public static ArrayList< ArrayList<Double> > qMatrix = new ArrayList< >();

	public static void setTerminalStates(int[][] m, boolean[] terminalStates)
	{
		for(int i = 0; i < statesNum; i++)
		{
			boolean isTerminal = true;
			for(int j = 0; j < statesNum; j++)
				if(m[i][j] != 0)
				{
					isTerminal = false;
					break;
				}

			if(isTerminal)
				terminalStates[i] = true;
			else
				terminalStates[i] = false;
		}
	}

	public static void buildRQMatrices(int[][] m)
	{
		for(int i = 0; i < statesNum; i++)
		{
			if(terminalStates[i])
				continue;

			rMatrix.add(new ArrayList<Double>());
			qMatrix.add(new ArrayList<Double>());

			double denominator = 0;
			for(int temp : m[i])
				denominator += temp;

			for(int j = 0; j < statesNum; j++)
			{
				double fraction = m[i][j] / denominator;

				if(terminalStates[j])
					rMatrix.get(rMatrix.size() - 1).add(fraction);
				else
					qMatrix.get(qMatrix.size() - 1).add(fraction);
			}
		}
	}

	public static double[][] buildFMatrix()
	{
		int sz = qMatrix.size();
		double[][] matrix_A = new double[sz][sz];

		for(int i = 0; i < sz; i++)
			for(int j = 0; j < sz; j++)
				matrix_A[i][j] = qMatrix.get(i).get(j);

		for(int i = 0; i < sz; i++)
			for(int j = 0; j < sz; j++)
				if(i == j)
					matrix_A[i][j] = 1 - matrix_A[i][j];
				else
					matrix_A[i][j] = 0 - matrix_A[i][j];

		double[][] fMatrix = invert(matrix_A);
		return fMatrix;
	}

	public static double[][] invert(double[][] matrix_A)
	{
		int sz = matrix_A.length;
		double[][] matrix_x = new double[sz][sz];
		double[][] matrix_b = new double[sz][sz];

		int[] triangle = new int[sz];
		triangify(matrix_A, triangle);

		for(int i = 0; i < sz; i++)
			matrix_b[i][i] = 1;

		for(int i = 0; i < sz; i++)
			for(int j = i + 1; j < sz; j++)
				for(int k = 0; k < sz; k++)
				{
					int ii = triangle[i];
					int jj = triangle[j];
					matrix_b[jj][k] -= matrix_A[jj][i] * matrix_b[ii][k];
				}

		for(int i = 0; i < sz; i++)
		{
			int ii = triangle[sz - 1];
			matrix_x[sz - 1][i] = matrix_b[ii][i] / matrix_A[ii][sz - 1];

			for(int j = sz - 2; j >= 0; j--)
			{
				matrix_x[j][i] = matrix_b[triangle[j]][i];

				for(int k = j + 1; k < sz; k++)
					matrix_x[j][i] -= matrix_A[triangle[j]][k] * matrix_x[k][i];

				matrix_x[j][i] /= matrix_A[triangle[j]][j];
			}
		}

		return matrix_x;
	}

	public static void triangify(double[][] matrix_A, int[] triangle)
	{
		int sz = triangle.length;
		double[] matrix_c = new double[sz];

		for(int i = 0; i < sz; i++)
			triangle[i] = i;

		for(int i = 0; i < sz; i++)
		{
			double c1 = 0;
			for(int j = 0; j < sz; j++)
			{
				double c0 = Math.abs(matrix_A[i][j]);
				if(c0 > c1)
					c1 = c0;
			}

			matrix_c[i] = c1;
		}

		int k = 0;
		for(int j = 0; j < sz; j++)
		{
			double pivot1 = 0;
			for(int i = j; i < sz; i++)
			{
				int ii = triangle[i];
				double pivot0 = Math.abs(matrix_A[ii][j]);
				pivot0 /= matrix_c[ii];

				if(pivot0 > pivot1)
				{
					pivot1 = pivot0;
					k = i;
				}
			}

			int temp = triangle[j];
			triangle[j] = triangle[k];
			triangle[k] = temp;

			for(int i = j + 1; i < sz; i++)
			{
				int ii = triangle[i];
				int jj = triangle[j];
				double pivot = matrix_A[ii][j] / matrix_A[jj][j];
				matrix_A[ii][j] = pivot;

				for(int l = j + 1; l < sz; l++)
					matrix_A[ii][l] -= pivot * matrix_A[jj][l];
			}
		}
	}

	public static double[][] buildFRMatrix(double[][] fMatrix)
	{
		int fRows = fMatrix.length;
		int fCols = fMatrix[0].length;
		int rCols = rMatrix.get(0).size();

		double[][] frMatrix = new double[fRows][rCols];
		for(int i = 0; i < fRows; i++)
			for(int j = 0; j < rCols; j++)
				frMatrix[i][j] = 0;

		for(int i = 0; i < fRows; i++)
			for(int j = 0; j < rCols; j++)
				for(int k = 0; k < fCols; k++)
					frMatrix[i][j] += fMatrix[i][k] * rMatrix.get(k).get(j);

		return frMatrix;
	}

	public static double[] answer(int[][] m)
	{
		statesNum = m.length;
		terminalStates = new boolean[statesNum];
		setTerminalStates(m, terminalStates);
		buildRQMatrices(m);

		double[][] fMatrix = buildFMatrix();
		double[][] frMatrix = buildFRMatrix(fMatrix);

		int sz = frMatrix[0].length;
		double[] result = new double[sz];

		for(int i = 0; i < sz; i++)
			result[i] = frMatrix[0][i];

		cleanup();
		return result;
	}

	public static void cleanup()
	{
		statesNum = 0;
		terminalStates = null;
		rMatrix.clear();
		qMatrix.clear();
	}

	public static void main(String[] args)
	{
		int[][] m1 = new int[][] {
			{0, 1, 0, 0, 0, 1},
			{4, 0, 0, 3, 2, 0},
			{0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0}
		};
		//[0, 3, 2, 9, 14]

		int[][] m2 = new int[][] {
			{0, 2, 1, 0, 0},
			{0, 0, 0, 3, 4},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0}
		};
		//[7, 6, 8, 21]

		double[] a1 = answer(m1);
		double[] a2 = answer(m2);

		for(double d : a1)
			System.out.println(d);

		System.out.println();

		for(double d : a2)
			System.out.println(d);
	}
}



/*
from fractions import Fraction


def setTerminalStates(m, terminalStates):
    for stateRow in m:
        isTerminal = True
        for stateCol in stateRow:
            if stateCol != 0:
                isTerminal = False
                break

        if isTerminal:
            terminalStates.append(True)
        else:
            terminalStates.append(False)


def buildRQMatrices(m, terminalStates, rMatrix, qMatrix):
    sz = len(m)
    for i in range(sz):
        if terminalStates[i] == True:
            continue

        rMatrix.append([])
        qMatrix.append([])
        denominator = sum(m[i])

        for j in range(sz):
            fraction = m[i][j] / float(denominator)
            if terminalStates[j] == True:
                rMatrix[-1].append(fraction)
            else:
                qMatrix[-1].append(fraction)


def buildFMatrix(qMatrix):
    matrix_A = []
    for stateRow in qMatrix:
        matrix_A.append([])
        for stateCol in stateRow:
            matrix_A[-1].append(stateCol)

    sz = len(qMatrix)
    for i in range(sz):
        for j in range(sz):
            if i == j:
                matrix_A[i][j] = 1.0 - matrix_A[i][j]
            else:
                matrix_A[i][j] = 0.0 - matrix_A[i][j]

    fMatrix = invert(matrix_A)
    return fMatrix


def invert(matrix_A):
    matrix_x = []
    matrix_b = []
    triangle = []

    sz = len(matrix_A)
    for i in range(sz):
        matrix_x.append([])
        for j in range(sz):
            matrix_x[-1].append(0.0)

    for i in range(sz):
        matrix_b.append([])
        for j in range(sz):
            if i == j:
                matrix_b[-1].append(1.0)
            else:
                matrix_b[-1].append(0.0)

    for i in range(sz):
        triangle.append(i)
    triangify(matrix_A, triangle)

    for i in range(sz):
        for j in range(i + 1, sz):
            for k in range(sz):
                ii = triangle[i]
                jj = triangle[j]
                matrix_b[jj][k] -= matrix_A[jj][i] * matrix_b[ii][k]

    for i in range(sz):
        ii = triangle[-1]
        matrix_x[-1][i] = matrix_b[ii][i] / matrix_A[ii][-1]

        for j in range(sz - 2, -1, -1):
            matrix_x[j][i] = matrix_b[triangle[j]][i]
            for k in range(j + 1, sz):
                matrix_x[j][i] -= matrix_A[triangle[j]][k] * matrix_x[k][i]

            matrix_x[j][i] /= matrix_A[triangle[j]][j]

    return matrix_x


def triangify(matrix_A, triangle):
    sz = len(triangle)
    matrix_c = []

    for i in range(sz):
        c1 = 0.0
        for j in range(sz):
            c0 = abs(matrix_A[i][j])
            if c0 > c1:
                c1 = c0

        matrix_c.append(c1)

    k = 0
    for j in range(sz):
        pivot1 = 0.0
        for i in range(j, sz):
            ii = triangle[i]
            pivot0 = abs(matrix_A[ii][j])
            pivot0 /= matrix_c[ii]

            if pivot0 > pivot1:
                pivot1 = pivot0
                k = i

        temp = triangle[j]
        triangle[j] = triangle[k]
        triangle[k] = temp

        for i in range(j + 1, sz):
            ii = triangle[i]
            jj = triangle[j]
            pivot = matrix_A[ii][j] / matrix_A[jj][j]
            matrix_A[ii][j] = pivot

            for l in range(j + 1, sz):
                matrix_A[ii][l] -= pivot * matrix_A[jj][l]


def buildFRMatrix(fMatrix, rMatrix):
    fRows = len(fMatrix)
    fCols = len(fMatrix[0])
    rCols = len(rMatrix[0])

    frMatrix = []
    for i in range(fRows):
        frMatrix.append([])
        for j in range(rCols):
            frMatrix[-1].append(0.0)

    for i in range(fRows):
        for j in range(rCols):
            for k in range(fCols):
                frMatrix[i][j] += fMatrix[i][k] * rMatrix[k][j]

    return frMatrix


def gcd(x, y):
    if y == 0:
        return x

    return gcd(y, x % y)


def lcm(x, y):
    return (x * y) // gcd(x, y)


def answer(m):
    if len(m) == 1:
        return [1, 1]

    terminalStates = []
    setTerminalStates(m, terminalStates)

    rMatrix = []
    qMatrix = []
    buildRQMatrices(m, terminalStates, rMatrix, qMatrix)

    fMatrix = buildFMatrix(qMatrix)
    frMatrix = buildFRMatrix(fMatrix, rMatrix)

    fractions = []
    for i in range(len(frMatrix[0])):
        fraction = Fraction(frMatrix[0][i]).limit_denominator()
        fractions.append(fraction)

    multiple = 1
    for fraction in fractions:
        multiple = lcm(multiple, fraction.denominator)

    result = []
    for fraction in fractions:
        result.append(multiple / fraction.denominator * fraction.numerator)

    result.append(multiple)
    return result
 */