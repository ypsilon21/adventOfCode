import Data.Bits

main::IO()
main = do
    input <- readFile "input.txt"
    let formattedInput = map (\x -> read x::Int) (lines input)
    let after2000 = simulate formattedInput 2000
    let res1 = _sum after2000
    print res1

_sum::[Int]->Int
_sum []     = 0
_sum (x:xs) = x + _sum xs

simulate::[Int]->Int->[Int]
simulate ls 0 = ls
simulate ls i = simulate (map applyFunction ls) (i - 1)

applyFunction::Int->Int
applyFunction x = apply 2048 (apply 32 (apply 64 x))
    where
        apply::Int->Int->Int
        apply m x 
            | m == 32   = prune (mix (x `div` m) x)
            | otherwise = prune (mix (x * m) x)

        mix::Int->Int->Int
        mix a x = a `xor` x 

        prune::Int->Int
        prune x = x `mod` 16777216