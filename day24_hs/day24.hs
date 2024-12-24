import Data.Char (ord)

main::IO()
main = do
    input <- readFile "input.txt"
    let formattedInput = format (lines input)
    let solved = solveExpressions formattedInput
    let filtered = filter startsWithZ solved 
    let res1 = toIntStr filtered
    print ("First result:  " ++ (show res1))
        where
            format::[[Char]]->([([Char],[Char])],[([Char],[Char],[Char],[Char])])
            format ls = (formatExp ls, formatFun ls)

            formatExp::[[Char]]->[([Char],[Char])]
            formatExp ([]:_) = []
            formatExp ([a,b,c,_,_,d]:ls) = ([a,b,c],[d]) : formatExp ls 

            formatFun::[[Char]]->[([Char],[Char],[Char],[Char])]
            formatFun [] = []
            formatFun (l:ls) 
                | len l <= 6 = formatFun ls 
                | otherwise  = toFunction l : formatFun ls 
            
            toFunction::[Char]->([Char],[Char],[Char],[Char])
            toFunction [a,b,c,_,'A',_,_,_,x,y,z,_,_,_,_,q,r,s] = ([a,b,c],"AN",[x,y,z],[q,r,s])
            toFunction [a,b,c,_,'X',_,_,_,x,y,z,_,_,_,_,q,r,s] = ([a,b,c],"XO",[x,y,z],[q,r,s])
            toFunction [a,b,c,_,'O',_,_,x,y,z,_,_,_,_,q,r,s]   = ([a,b,c],"OR",[x,y,z],[q,r,s])

            startsWithZ::([Char],[Char])->Bool 
            startsWithZ ('z':str,_) = True
            startsWithZ _           = False

            toIntStr::[([Char],[Char])]->Int
            toIntStr []                 = 0
            toIntStr (([_,a,b],[y]):ls) = (2 ^ (10 * (toInt a) + toInt b)) * (toInt y) + toIntStr ls 

solveExpressions::([([Char],[Char])],[([Char],[Char],[Char],[Char])])->[([Char],[Char])]
solveExpressions (exp, [])   = exp
solveExpressions (exp, funs) = solveExpressions (tryToSolve (exp, funs)) 
    where
        tryToSolve::([([Char],[Char])],[([Char],[Char],[Char],[Char])])->([([Char],[Char])],[([Char],[Char],[Char],[Char])])
        tryToSolve (exp, [])       = (exp, [])
        tryToSolve (exp, (f:funs)) = 
            let solv = build f exp 
            in if not (solv == []) then tryToSolve (solv ++ exp, funs) else tryToSolve (exp, funs++[f])
        
        build::([Char],[Char],[Char],[Char])->[([Char],[Char])]->[([Char],[Char])]
        build (a1,op,a2,re) [] = if (a1 == "1" || a1 == "0") && (a2 == "1" || a2 == "0") then [(re, solveBool (read a1::Int) op (read a2::Int))] else []
        build (a1,op,a2,re) ((e1,er):exp)
            | (a1 == "1" || a1 == "0") && (a2 == "1" || a2 == "0") = [(re, solveBool (read a1::Int) op (read a2::Int))]
            | a1 == e1  = if a2 == e1 then build (er,op,er,re) exp else build (er,op,a2,re) exp 
            | a2 == e1  = build (a1,op,er,re) exp 
            | otherwise = build (a1,op,a2,re) exp
        
        solveBool::Int->[Char]->Int->[Char]
        solveBool x "AN" y = if x + y == 2 then "1" else "0"
        solveBool x "XO" y = if x + y == 1 then "1" else "0"
        solveBool x "OR" y = if x + y >= 1 then "1" else "0"

len::[a]->Int 
len []     = 0
len (l:ls) = 1 + len ls

toInt::Char->Int
toInt x = ord x - ord '0'