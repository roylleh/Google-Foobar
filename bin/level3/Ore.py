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
