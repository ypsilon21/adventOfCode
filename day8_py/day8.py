def main():
    filereader = open("input.txt", "r")
    lines = filereader.readlines()
    lines = list(map(lambda x: x.rstrip('\n'), lines))
    filereader.close()
    coordsList = getCoordinates(lines)
    antinodes = filterBounds(getAntinodes(coordsList, 0, 0), len(lines), len(lines[0]))
    harmonicAntinodes = remDublicates(getAntinodes(coordsList, len(lines), len(lines[0])))

    print(f"First result:  {len(antinodes)}")
    print(f"Second result: {len(harmonicAntinodes)}")

def getCoordinates(lines):
    res = {}
    ll = len(lines) - 1
    for i in range(len(lines)):
        for j in range(len(lines[0])):
            if lines[i][j] != '.':
                if lines[i][j] in res:
                    res.get(lines[i][j]).append([j,ll-i])
                else:
                    res[lines[i][j]] = [[j,ll-i]]
    return res

def getAntinodes(coords, height, width):
    antinodes = []
    pairs = []
    for list in coords.values():
        pairs.append(getMatchings(list, list))
    for pairlist in pairs:
        for pair in pairlist:
            if not(height == 0):
                for p in pair:
                    if not(p in antinodes): antinodes.append(p)
            if height == 0: antinodes.extend(calculateAntinode(pair))
            else: antinodes.extend(calculateHarmonicAntinodes(pair, height, width))
    return antinodes

def getMatchings(list, initList):
    if list == [] and initList == [] : return []
    elif list == []: return getMatchings(initList[1:], initList[1:])
    elif list[0] != initList[0]: return [[initList[0], list[0]]] + getMatchings(list[1:], initList)
    else: return getMatchings(list[1:], initList)

def calculateAntinode(coords):
    x1 = coords[0][0]
    y1 = coords[0][1]
    x2 = coords[1][0]
    y2 = coords[1][1]
    if x1 > x2:
        x1, x2 = x2, x1
        y1, y2 = y2, y1

    distance = x2 - x1
    m = (y2 - y1) / distance
    b = coords[0][1] - m * coords[0][0]

    ax1 = x1 - distance
    ax2 = x2 + distance
    ay1 = m * ax1 + b
    ay2 = m * ax2 + b

    return [[ax1,round(ay1)],[ax2,round(ay2)]]

def calculateHarmonicAntinodes(coords, height, width):
    x1 = coords[0][0]
    y1 = coords[0][1]
    x2 = coords[1][0]
    y2 = coords[1][1]
    if x1 > x2:
        x1, x2 = x2, x1
        y1, y2 = y2, y1

    distance = x2 - x1
    m = (y2 - y1) / distance
    b = coords[0][1] - m * coords[0][0]

    res = []
    for x in range(width):
        if x in [x1,x2]: continue
        else:
            y = m * x + b 
            if abs(round(y) - y) < 0.001 and 0 <= y < height:
                res.append([x,round(y)])
    return res

def filterBounds(antinodes, height, width):
    if antinodes == []: return []
    elif 0 <= antinodes[0][0] < height and 0 <= antinodes[0][1] < width and not antinodes[0] in antinodes[1:]: 
        return [antinodes[0]] + filterBounds(antinodes[1:],height,width)
    else: return filterBounds(antinodes[1:],height,width)

def remDublicates(ls):
    res = []
    for i in range(len(ls)):
        if ls[i] not in ls[i+1:]: res.append(ls[i])
    return res

main()