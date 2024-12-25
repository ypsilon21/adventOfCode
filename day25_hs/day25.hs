main::IO()
main = do
    input <- readFile "input.txt"
    let formattedInput = seperateLocksKeys ([],(splitBlocks (lines input))) ([],[])
    let intArrays = toIntArrays formattedInput
    let res1 = countCombinations intArrays
    print ("First result:  " ++ (show res1))
        where
            splitBlocks::[String]->[[String]]
            splitBlocks [] = []
            splitBlocks ls = takeFirstBlock ls : (splitBlocks (removeFirstBlock ls))

            takeFirstBlock::[String]->[String]
            takeFirstBlock []      = []
            takeFirstBlock ("":ls) = []
            takeFirstBlock (l:ls)  = l:takeFirstBlock ls 

            removeFirstBlock::[String]->[String]
            removeFirstBlock []      = []
            removeFirstBlock ("":ls) = ls 
            removeFirstBlock (l:ls)  = removeFirstBlock ls

            seperateLocksKeys::([[String]],[[String]])->([[String]],[[String]])->([[String]],[[String]])
            seperateLocksKeys ([],[]) (a,b)   = (a,b)
            seperateLocksKeys ([],k:ks) (a,b) = if head k == "#####" 
                then seperateLocksKeys ([],ks) (k:a,b)
                else seperateLocksKeys ([],ks) (a,k:b)
            seperateLocksKeys (l:ls,ks) (a,b) = if head l == "#####" 
                then seperateLocksKeys ([],ks) (l:a,b)
                else seperateLocksKeys ([],ks) (a,l:b)

toIntArrays::([[String]],[[String]])->([[Int]],[[Int]])
toIntArrays (a,b) = (intArr a, intArr b)
    where
        intArr::[[String]]->[[Int]]
        intArr [] = []
        intArr ls = map (\a -> arrSum a [-1,-1,-1,-1,-1]) (map (map (map (\x -> if x == '#' then 1 else 0))) ls)

        arrSum::[[Int]]->[Int]->[Int]
        arrSum [] res = res
        arrSum ([a1,a2,a3,a4,a5]:a) [r1,r2,r3,r4,r5] = arrSum a [a1+r1,a2+r2,a3+r3,a4+r4,a5+r5]

countCombinations::([[Int]],[[Int]])->Int 
countCombinations ([],ks)     = 0
countCombinations ((l:ls),ks) = countComb l ks + countCombinations (ls,ks)
    where
        countComb::[Int]->[[Int]]->Int 
        countComb l []     = 0
        countComb l (k:ks) = (if smallerThan 6 (addArr l k) then 1 else 0) + countComb l ks 

        addArr::[Int]->[Int]->[Int]
        addArr [a1,a2,a3,a4,a5] [r1,r2,r3,r4,r5] = [a1+r1,a2+r2,a3+r3,a4+r4,a5+r5] 

        smallerThan::Int->[Int]->Bool
        smallerThan a []     = True
        smallerThan a (x:xs) = if x < a then smallerThan a xs else False