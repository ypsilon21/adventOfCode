main::IO()
main = do
    content <- readFile("input.txt")
    let initList = strLstToIntLst (words content)
    --res1
    let after25Blinks = blink initList 25
    let res1 = _len after25Blinks
    print ("First Result:  " ++ show res1)
    --res2
    let res2 = lenIndv after25Blinks 50
    print("Second Result: doesnt work :(")
    print ("Second Result: " ++ show res2)
        where 
            lenIndv::[Int]->Int->Int
            lenIndv [] _     = 0
            lenIndv (x:xs) i = _len (blink [x] i) + lenIndv xs i 

strLstToIntLst::[[Char]]->[Int]
strLstToIntLst []     = []
strLstToIntLst (x:xs) = (read x::Int) : (strLstToIntLst xs) 
        
blink::[Int]->Int->[Int]
blink xs 0 = xs
blink xs i = blink (performBlink xs) (i-1)
    where   
        performBlink::[Int]->[Int]
        performBlink []     = []
        performBlink (x:xs) 
            | x == 0                   = 1 : performBlink xs
            | evenNumOfDigits (show x) = let sx = show x 
                                         in splitStone sx ((_len sx) `div` 2) ++ performBlink xs
            | otherwise                = (x * 2024) : performBlink xs
        
evenNumOfDigits::[Char]->Bool
evenNumOfDigits str 
    | _mod2  (_len str) == 0 = True
    | otherwise              = False

splitStone::[Char]->Int->[Int]
splitStone xs l = (read (take l xs)::Int) : [read (drop l xs)::Int]

_mod2::Int->Int
_mod2 x
    |x == 2 || x == 0 = 0
    |x == 1           = 1
    |otherwise        = _mod2 (x - 2)

_len::[a]->Int
_len []     = 0
_len (x:xs) = 1 + (_len xs)

