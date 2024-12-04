import Data.Char (ord)

main :: IO ()
main = do
    content <- readFile "input.txt" 
    let result1 = filterMuls (removeDoubleParenths content)
    let res1 = add result1
    print ("First Result:  " ++ show res1)
    let result2 = filterMuls2 (removeDoubleParenths content) True
    let res2 = add result2
    print ("Second Result: " ++ show res2)

filterMuls::[Char]->[Int]
filterMuls []           = []
filterMuls ('m':xs)     | getMul ('m':xs) == 0 = filterMuls xs
                        | otherwise = (getMul ('m':xs)) : filterMuls xs
filterMuls (x:xs)       = filterMuls xs

filterMuls2::[Char]->Bool->[Int]
filterMuls2 []       b                  = []
filterMuls2 ('d':'o':'n':'\'':'t':xs) b = filterMuls2 xs False
filterMuls2 ('d':'o':xs) b              = filterMuls2 xs True
filterMuls2 ('m':xs) True               | getMul ('m':xs) == 0 = filterMuls2 xs True
                                        | otherwise = (getMul ('m':xs)) : filterMuls2 xs True
filterMuls2 (x:xs)   b                  = filterMuls2 xs b

getMul :: [Char]->Int
getMul ('m':'u':'l':'(':a:','       :d:')':_)       = multiplyChars [[a],[d]]
getMul ('m':'u':'l':'(':a:b:','     :d:e:')':_)     = multiplyChars [[a,b],[d,e]]
getMul ('m':'u':'l':'(':a:b:c:','   :d:e:f:')':_)   = multiplyChars [[a,b,c],[d,e,f]]
getMul ('m':'u':'l':'(':a:','       :d:e:')':_)     = multiplyChars [[a],[d,e]]
getMul ('m':'u':'l':'(':a:b:','     :d:e:f:')':_)   = multiplyChars [[a,b],[d,e,f]]
getMul ('m':'u':'l':'(':a:b:c:','   :d:')':_)       = multiplyChars [[a,b,c],[d]]
getMul ('m':'u':'l':'(':a:','       :d:e:f:')':_)   = multiplyChars [[a],[d,e,f]]
getMul ('m':'u':'l':'(':a:b:','     :d:')':_)       = multiplyChars [[a,b],[d]]
getMul ('m':'u':'l':'(':a:b:c:','   :d:e:')':_)     = multiplyChars [[a,b,c],[d,e]]
getMul _ = 0

multiplyChars::[[Char]]->Int
multiplyChars (x:y:[]) = if allInts (x++y) then (toInt (_reverse x)) * (toInt (_reverse y)) else 0

allInts::[Char]->Bool
allInts []      = True
allInts (x:xs)  = if ord x >= ord '0' && ord x <= ord '9' then allInts xs else False

toInt::[Char]->Int
toInt ([x])     = ord x - ord '0'
toInt (x:xs)    = ord x - ord '0' + 10 * (toInt xs)

_reverse::[a]->[a]
_reverse []     = []
_reverse (x:xs) = (_reverse xs) ++ [x]

add::[Int]->Int
add []      = 0
add (x:xs)  = x + add xs

removeDoubleParenths::[Char]->[Char]
removeDoubleParenths []             = []
removeDoubleParenths (')':')':xs)   = ')':(removeDoubleParenths xs) 
removeDoubleParenths (x:xs)         = x:(removeDoubleParenths xs)