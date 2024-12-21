import Data.Char (ord)

main::IO()
main = do
    content <- readFile("input.txt")
    let tempres1 = toFileSystem content True 0
    let tempres2 = condenseStorage tempres1
    let checksum1 = checksum tempres2
    print (concat tempres1)
    print (concat tempres2)
    let res1 = "First Result:  " ++ show checksum1
    print res1

--input List -> data=True/empty=False -> data Index -> res
toFileSystem::[Char]->Bool->Int->[[Char]]
toFileSystem [] _ _         = []
toFileSystem (x:xs) True  i = (xTimesCharList (charToInt x) (intToChar i)) ++ toFileSystem xs False (i + 1)
toFileSystem (x:xs) False i = (xTimesCharList (charToInt x) ['.']) ++ toFileSystem xs True i

--how many times? -> char -> res
xTimesCharList::Int->[Char]->[[Char]]
xTimesCharList 0 _  = []
xTimesCharList i x  = x : (xTimesCharList (i - 1) x)

charToInt::Char->Int
charToInt x = ord x - ord '0'

strToInt::[Char]->Int
strToInt x = read x::Int

intToChar::Int->[Char]
intToChar x = show x

condenseStorage::[[Char]]->[[Char]]
condenseStorage xs = condense xs (_reverse xs)
    where
    condense::[[Char]]->[[Char]]->[[Char]]
    condense xs []     = xs
    condense xs (y:ys) = condense (removeLast (stripDotTrail (insert y xs))) ys

    removeLast::[[Char]]->[[Char]]
    removeLast []       = []
    removeLast [x]      = []
    removeLast (x:xs)   = x : removeLast xs

    insert::[Char]->[[Char]]->[[Char]]
    insert y []         = [y]
    insert y (['.']:xs) = y:xs
    insert y (x:xs)     = x : insert y xs

    stripDotTrail::[[Char]]->[[Char]]
    stripDotTrail []         = []
    stripDotTrail (['.']:xs) = stripDotTrail xs
    stripDotTrail (x:xs)     = x:xs

    _reverse::[a]->[a]
    _reverse []     = []
    _reverse (x:xs) = _reverse xs ++ [x]

    

{-condenseStorage::[[Char]]->[[Char]]
condenseStorage xs = condense xs rxs
    where
    rxs = _reverse xs

    stripEndDots::[[Char]]->[[Char]]
    stripEndDots []         = []
    stripEndDots (['.']:xs) = stripEndDots xs
    stripEndDots (x:xs)     = x:xs

    _reverse::[a]->[a]
    _reverse []     = []
    _reverse (x:xs) = _reverse xs ++ [x]

    condense::[[Char]]->[[Char]]->[[Char]]
    condense xs []       = xs
    condense (x:xs) (y:ys)   = [x] ++ _reverse (stripEndDots(_reverse (condense (_reverse (_remove y (_reverse (insert y xs)))) ys)))

    insert::[Char]->[[Char]]->[[Char]]
    insert _ []         = []
    insert x (['.']:ys) = x : ys
    insert x (y:ys)     = y : insert x ys

    _remove::[Char]->[[Char]]->[[Char]]
    _remove _ [] = []
    _remove x (y:xs)    | x == y = ['.']:xs
                        | otherwise = y : (_remove x xs)
-}
checksum::[[Char]]->Int 
checksum xs = checksumHelper (map strToInt (stripDots xs)) 0
    where
    stripDots::[[Char]]->[[Char]]
    stripDots []        = []
    stripDots (['.']:_)   = []
    stripDots (x:xs)    = x : stripDots xs

    checksumHelper::[Int]->Int->Int
    checksumHelper [] _     = 0
    checksumHelper (x:xs) i = i * x + checksumHelper xs (i + 1)