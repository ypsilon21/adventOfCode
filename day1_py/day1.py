list1 = []
list2 = []

filereader = open("input.txt", "r")

list12 = filereader.readlines()
filereader.close()

for line in list12:
    list1.append(line[:5])
    list2.append(line[5:])

list1 = sorted(list1)
list2 = sorted(list2)

res = 0

for i in range(len(list1)):
    res += abs(int(list1[i]) - int(list2[i]))

print(res)