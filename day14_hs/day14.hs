height::Int
height = 103

width::Int
width = 101

main::IO()
main = do
    input <- readFile "input.txt"
    let formattedInput = formatInput (lines input)
    let after100 = simulate formattedInput 100
    let res1 = multiplyQuadrants after100
    let res2 = findLowestFactor formattedInput
    print ("First result:  " ++ (show res1))
    print ("Second result: " ++ (show res2))
    where
        formatInput::[[Char]]->[((Int, Int),(Int, Int))]
        formatInput []     = []
        formatInput (x:xs) = format (words x) : formatInput xs

        format::[[Char]]->((Int, Int),(Int, Int))
        format [x, y] = (splitInts (split (drop 2 x)), splitInts (split (drop 2 y)))

        split::[Char]->[[Char]]
        split [] = []
        split xs = takeWhile (/= ',') xs : split (drop 1 (dropWhile (/= ',') xs))

        splitInts::[[Char]]->(Int, Int)
        splitInts [x, y] = (read x::Int, read y::Int)

simulate::[((Int, Int),(Int, Int))]->Int->[((Int, Int),(Int, Int))]
simulate xs 0 = xs
simulate xs i = simulate (updatePositions xs) (i-1)
    where
        updatePositions::[((Int, Int),(Int, Int))]->[((Int, Int),(Int, Int))]
        updatePositions [] = []
        updatePositions (x:xs) = (applyVector x):updatePositions xs

        applyVector::((Int, Int),(Int, Int))->((Int, Int),(Int, Int))
        applyVector ((a, b), (c, d)) = (((a + c) `mod` width, (b + d) `mod` height), (c, d))

multiplyQuadrants::[((Int, Int),(Int, Int))]->Int
multiplyQuadrants xs = multiply (divide xs [0,0,0,0])
    where
        divide::[((Int, Int),(Int, Int))]->[Int]->[Int]
        divide [] ys = ys
        divide (((a, b), (c, d)):xs) [w, x, y, z]
            | a < (width `div` 2) && b < (height `div` 2) = divide xs [w + 1, x, y, z]
            | a < (width `div` 2) && b > (height `div` 2) = divide xs [w, x + 1, y, z]
            | a > (width `div` 2) && b < (height `div` 2) = divide xs [w, x, y + 1, z]
            | a > (width `div` 2) && b > (height `div` 2) = divide xs [w, x, y, z + 1]
            | otherwise                                   = divide xs [w , x, y, z]
        multiply::[Int]->Int
        multiply []     = 1
        multiply (x:xs) = x * (multiply xs)

findLowestFactor::[((Int, Int),(Int, Int))]->Int
findLowestFactor xs = fst (_min (getFactors xs 10402))
    where
        getFactors::[((Int, Int),(Int, Int))]->Int->[(Int, Int)]
        getFactors xs 0 = []
        getFactors xs i = 
            let xss = simulate xs 1
            in ((10402 - i + 1), multiplyQuadrants xss) : getFactors xss (i-1)

        _min::[(Int, Int)]->(Int, Int)
        _min [(x, y)]    = (x, y)
        _min ((x, y):xs) =
            let minxs = _min xs
            in if snd minxs > y then (x, y) else minxs