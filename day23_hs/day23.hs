main::IO()
main = do
    input <- readFile "input.txt"
    let formattedInput = toGraph (lines input)
    let res1 = countTs (search3Cliques formattedInput)
    print ("First result:  " ++ (show res1))
        where
            toGraph::[[Char]]->[([Char],[Char])]
            toGraph []                 = []
            toGraph ([a,b,'-',c,d]:ls) = ([a,b],[c,d]) : toGraph ls 

search3Cliques::[([Char],[Char])]->[([Char],[Char],[Char])]
search3Cliques [] = []
search3Cliques ((x,y):ls) = searchForThird x y ls ++ search3Cliques ls 
    where
        searchForThird::[Char]->[Char]->[([Char],[Char])]->[([Char],[Char],[Char])]
        searchForThird _ _ [] = []
        searchForThird x y ((a,b):ls)
            | x == a    = if isConnected y b ls then (x,y,b):searchForThird x y ls else searchForThird x y ls
            | x == b    = if isConnected y a ls then (x,y,a):searchForThird x y ls else searchForThird x y ls 
            | y == a    = if isConnected x b ls then (x,y,b):searchForThird x y ls else searchForThird x y ls
            | y == b    = if isConnected x a ls then (x,y,a):searchForThird x y ls else searchForThird x y ls 
            | otherwise = searchForThird x y ls 
        
        isConnected::[Char]->[Char]->[([Char],[Char])]->Bool
        isConnected _ _ []         = False
        isConnected x y ((a,b):ls)
            | x == a && y == b = True
            | x == b && y == a = True
            | otherwise = isConnected x y ls

countTs::[([Char],[Char],[Char])]->Int 
countTs []                       = 0
countTs (([a,_],[b,_],[c,_]):ls) = (if a == 't' || b == 't' || c == 't' then 1 else 0) + countTs ls
