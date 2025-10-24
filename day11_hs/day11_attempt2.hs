main::IO()
main = do
    content <- readFile("input.txt")
    let initList = simplify (strToIntList (words content))
    let after25Blinks = blink 25 initList
    let res1 = countStones after25Blinks
    print ("First Result:  " ++ show res1)
    let after75Blinks = blink 75 initList
    let res2 = countStones after75Blinks
    print ("Second Result: " ++ show res2)
        where
            countStones::[(Int, Int)]->Int
            countStones []     = 0
            countStones ((x,c):xs) = c + countStones xs  

strToIntList::[[Char]]->[(Int, Int)]
strToIntList []     = []
strToIntList (x:xs) = (read x::Int, 1) : strToIntList xs

simplify::[(Int, Int)]->[(Int, Int)]
simplify []          = []
simplify ((x, c):xs) = (x, count x c xs) : simplify (remove x xs)
    where
        count::Int->Int->[(Int, Int)]->Int
        count _ c []           = c
        count x c ((y, cy):ys) | x == y    = count x (c+cy) ys 
                               | otherwise = count x c ys 

        remove::Int->[(Int, Int)]->[(Int, Int)]
        remove _ []         = []
        remove x ((y,c):ys) | x == y    = remove x ys 
                            | otherwise = (y,c) : remove x ys  

blink::Int->[(Int, Int)]->[(Int, Int)]
blink 0 xs = xs 
blink n xs = blink (n-1) (simplify (performBlink xs))
    where
        performBlink::[(Int, Int)]->[(Int, Int)]
        performBlink []         = []
        performBlink ((0,c):xs) = (1,c) : performBlink(xs)
        performBlink ((x,c):xs) | evenDigits x = (splitStone (show x) ((_len (show x)) `div` 2) c) ++ performBlink xs  
                                | otherwise    = (x*2024, c) : performBlink xs 

        evenDigits::Int->Bool
        evenDigits x = (length (show x)) `mod` 2 == 0

        splitStone::[Char]->Int->Int->[(Int, Int)]
        splitStone xs l c = (read (take l xs)::Int, c) : [(read (drop l xs)::Int, c)]

_len::[a]->Int
_len [] = 0
_len (x:xs) = 1 + _len xs

