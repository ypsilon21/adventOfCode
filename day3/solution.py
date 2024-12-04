def part1_solution(data: str):
    # valid mul commands look like this:
    # mul([1-3 digit number], [1-3 digit number])
    # any deviation from this pattern is invalid
    valid_start = "mul("
    i = 0
    total = 0
    out = []
    while i < len(data):
        if len(data) - i < 4:
            break
        if data[i : i + 4] == valid_start:
            i += 4
            num1 = ""
            while i < len(data) and data[i].isdigit():
                num1 += data[i]
                i += 1
            if not num1 or len(num1) > 3 or i >= len(data) or data[i] != ",":
                continue

            i += 1
            num2 = ""
            while i < len(data) and data[i].isdigit():
                num2 += data[i]
                i += 1
            if not num2 or len(num2) > 3 or i >= len(data) or data[i] != ")":
                continue

            total += int(num1) * int(num2)
            out.append(int(num1) * int(num2))
            if int(num1) * int(num2) in [26015,5002,84875]: print(num1, num2)

        i += 1
    print(out)
    return total


def part2_solution(data: str):
    # new changes: toggle that enables (do()) or disables (don't()) future mul commands
    # so now in addition to the mul command, need to check for do() and don't()
    valid_start = "mul("
    do_str = "do()"
    dont_str = "don't()"
    i = 0
    total = 0
    mul_enabled = True

    while i < len(data):
        if len(data) - i < 7:
            break

        if data[i : i + 4] == valid_start:
            i += 4
            num1 = ""
            while i < len(data) and data[i].isdigit():
                num1 += data[i]
                i += 1
            if not num1 or len(num1) > 3 or i >= len(data) or data[i] != ",":
                continue

            i += 1
            num2 = ""
            while i < len(data) and data[i].isdigit():
                num2 += data[i]
                i += 1
            if not num2 or len(num2) > 3 or i >= len(data) or data[i] != ")":
                continue

            if mul_enabled:
                total += int(num1) * int(num2)

        elif data[i : i + 4] == do_str:
            mul_enabled = True
            i += 4
            continue

        elif data[i : i + 7] == dont_str:
            mul_enabled = False
            i += 7
            continue

        i += 1

    return total


if __name__ == "__main__":
    with open("input.txt", "r") as f:
        data = f.read()
    print(f"The solution to part 1 is {part1_solution(data)}")
    print(f"The solution to part 2 is {part2_solution(data)}")