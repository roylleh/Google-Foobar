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
