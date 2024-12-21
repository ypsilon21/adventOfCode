import Data.Bits

main::IO()
main = do
    input <- readFile "input.txt"
    let formattedInput = format (lines input)
    let executed = simulate formattedInput
    let res1 = getOutput executed
    print ("First result:  " ++ (show res1))
        where
            --the Computer's type means: (3 registers, instructions as tuples, PC, output List)
            format::[[Char]]->((Int, Int, Int), [(Int,Int)], Int, [Int])
            format [a, b, c, _, d] = (((read (drop 12 a)::Int), (read (drop 12 b)::Int), 
                                     (read (drop 12 c)::Int)), 
                                     instructions (map (\x -> read x::Int) (split (drop 9 d))), 0, [])

            instructions::[Int]->[(Int, Int)]
            instructions []       = []
            instructions (x:y:xs) = (x, y) : instructions xs

            split::[Char]->[[Char]]
            split [] = []
            split xs = takeWhile (/= ',') xs : split (drop 1 (dropWhile (/= ',') xs))

            getLine::Int->[[Char]]->[Char]
            getLine 1 (x:xs) = x
            getLine i (x:xs) = getLine (i-1) xs

getOutput::((Int, Int, Int), [(Int,Int)], Int, [Int])->[Int]
getOutput (_, _, _, out) = out 

simulate::((Int, Int, Int), [(Int,Int)], Int, [Int])->((Int, Int, Int), [(Int,Int)], Int, [Int])
simulate ((a, b, c), ins, pc, out)
    | pc >= (len ins) * 2 = ((a, b, c), ins, pc, out)
    | otherwise           = simulate (executeInstruction ((a, b, c), ins, pc, out) (indexAt (pc `div` 2) ins))
    where   
        indexAt::Int->[a]->a
        indexAt 0 (x:xs) = x
        indexAt i (x:xs) = indexAt (i-1) xs 

        len::[a]->Int
        len []     = 0
        len (x:xs) = 1 + len xs

executeInstruction::((Int, Int, Int), [(Int,Int)], Int, [Int])->(Int, Int)->((Int, Int, Int), [(Int,Int)], Int, [Int])
executeInstruction ((a, b, c), ins, pc, out) (instr, combo) = 
    let v = comboVal combo (a, b, c)
    in case instr of
        0 -> (((a `div` (2 ^ v)), b, c), ins, pc + 2, out)
        1 -> ((a, (b `xor` combo), c), ins, pc + 2, out)
        2 -> ((a, (v `mod` 8), c), ins, pc + 2, out)
        3 -> if a == 0 then ((a, b, c), ins, pc + 2, out) 
             else ((a, b, c), ins, combo, out)
        4 -> ((a, (b `xor` c), c), ins, pc + 2, out)
        5 -> ((a, b, c), ins, pc + 2, out ++ [v `mod` 8])
        6 -> ((a, (a `div` (2 ^ v)), c), ins, pc + 2, out)
        7 -> ((a, b, (a `div` (2 ^ v))), ins, pc + 2, out)
        where
            comboVal::Int->(Int, Int, Int)->Int
            comboVal x (a, b, c) = case x of
                _ | x <= 3 -> x
                4          -> a 
                5          -> b 
                6          -> c 