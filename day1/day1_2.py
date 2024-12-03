list1 = []
list2 = []

filereader = open("input.txt", "r")

list12 = filereader.readlines()
filereader.close()

for line in list12:
    list1.append(line[:5])
    list2.append(line[5:].strip())

res = 0

for i in range(len(list1)):
    res += int(list1[i]) * list2.count(list1[i])

print(res)