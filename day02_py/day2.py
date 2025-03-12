fileReader = open("input.txt", "r")
lines = fileReader.readlines()
fileReader.close()

ll = []

for line in lines:
    line = line.strip()
    ll.append(line.split(' '))

res = 0
res2 = 0

def validate(line):
    for i in range(len(line) - 1):
        if not (1 <= abs(int(line[i]) - int(line[i+1])) <= 3): return False
    return True

def asc_desc(line):
    for i in range(len(line) - 1):
        if int(line[i]) < int(line[i+1]):
            if i == len(line) - 2:
                return True
        else: break

    for i in range(len(line) - 1):
        if int(line[i]) > int(line[i+1]):
            if i == len(line) - 2:
                return True
        else: break

    return False

def lineVersions(line):
    res = []
    res.append(line)

    for i in range(len(line)):
        newline = line[:i] + line[i+1:]
        res.append(newline)
    return res

for line in ll:
    if asc_desc(line) and validate(line): 
        res += 1  

for line in ll:
    for x in lineVersions(line):
        if asc_desc(x) and validate(x): 
            res2 += 1 
            break

print(f"First result:  {res}")
print(f"Second result: {res2}")