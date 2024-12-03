fileReader = open("input.txt", "r")
lines = fileReader.readlines()
fileReader.close()

ll = []

for line in lines:
    line = line.strip()
    ll.append(line.split(' '))

res = 0

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
    for x in lineVersions(line):
        if asc_desc(x) and validate(x): 
            res += 1 
            break


print(res)


"""
fileReader = open("input.txt", "r")
lines = fileReader.readlines()
fileReader.close()

ll = []

for line in lines:
    line = line.strip()
    ll.append(line.split(' '))

res = 0

def validate(line, iteration, comp):
    if iteration >= 2: return False
    for i in range(len(line) - 1):
        if (comp and int(line[i]) > int(line[i+1])) or (not comp and int(line[i]) < int(line[i+1])) or not 1 <= abs(int(line[i]) - int(line[i+1])) <= 3:
            newline = line[:i] + line[i+1:]
            return validate(newline, iteration + 1, comp)
    return True
    
            
for line in ll:
    if validate(line, 0, True) or validate(line, 0, False): 
        res += 1  

print(res)
"""