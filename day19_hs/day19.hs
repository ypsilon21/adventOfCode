main::IO()
main = do
    input <- readFile "input.txt"
    let formattedInput = format (lines input)
    let res1 = simulate formattedInput 
    print ("First result:  " ++ (show res1))
        where
            format::[[Char]]->([[Char]], [[Char]])
            format (x:y:xs) = ((trail (split x)), xs)

            split::[Char]->[[Char]]
            split [] = []
            split xs = takeWhile (/= ',') xs : split (drop 1 (dropWhile (/= ',') xs))
            
            trail::[[Char]]->[[Char]]
            trail []           = []
            trail ((' ':x):xs) = x : trail xs
            trail (x:xs)       = x : trail xs

simulate::([[Char]], [[Char]])->Int 
simulate (ls, [])   = 0
simulate (ls, x:xs) = (if possible ls ls x then 1 else 0) + simulate (ls, xs)
    where
        possible::[[Char]]->[[Char]]->[Char]->Bool
        possible _ _ []       = True
        possible _ [] _       = False
        possible ls (l:lss) x 
            | prefix l x = possible ls ls (remPrefix l x) || possible ls lss x 
            | otherwise  = possible ls lss x
        
        prefix::[Char]->[Char]->Bool
        prefix [] _          = True
        prefix _ []          = False
        prefix (x:xs) (y:ys) = if x == y then prefix xs ys else False

        remPrefix::[Char]->[Char]->[Char]
        remPrefix [] xs         = xs
        remPrefix (l:ls) (x:xs) = remPrefix ls xs