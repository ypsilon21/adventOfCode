<?php
$file = fopen("input.txt", "r");
$linesAsStrings = [];
if ($file){
    while (($line = fgets($file)) !== false) {
        $linesAsStrings[] = rtrim($line);
    }
}
$matrix = turnToMatrix($linesAsStrings);

print "First result: ".countAll($matrix);

function turnToMatrix($linesAsStrings){
    $matrix = [];
    foreach($linesAsStrings as $line){
        $matrix[] = str_split($line);
    }
    return $matrix;
}

function countAll($matrix){
    $count = countLines($matrix);
    $count += countCollumns($matrix);
    $count += countDiagonals($matrix);
    return $count;
}

function countLines($matrix){
    $res = 0;
    foreach($matrix as $line){
        for($i = 0; $i < count($line) - 3; $i++){
            if(($line[$i] == "X" AND $line[$i+1] == "M" AND $line[$i+2] == "A" AND $line[$i+3] == "S") 
                    OR ($line[$i] == "S" AND $line[$i+1] == "A" AND $line[$i+2] == "M" AND $line[$i+3] == "X")){
                $res++;
            }
        }
    }
    return $res;
}

function countCollumns($matrix){
    $res = 0;
    for($i = 0; $i < count($matrix[0]); $i++){
        for($j = 0; $j < count($matrix) - 3; $j++){
            if(($matrix[$j][$i] == "X" AND $matrix[$j+1][$i] == "M" AND $matrix[$j+2][$i] == "A" AND $matrix[$j+3][$i] == "S")
                    OR ($matrix[$j][$i] == "S" AND $matrix[$j+1][$i] == "A" AND $matrix[$j+2][$i] == "M" AND $matrix[$j+3][$i] == "X")){
                $res++;
            }
        }
    }
    return $res;
}

function countDiagonals($matrix){
    $matrix2 = $matrix;
    for($i = 0; $i < count($matrix); $i++){
        $matrix[$i] = shiftChars($matrix[$i], $i);
    }
    for($i = 0; $i < count($matrix2); $i++){
        $matrix2[$i] = shiftChars($matrix2[$i], -$i-1);
    }
    return countCollumns($matrix) + countCollumns($matrix2);
}

function shiftChars($line, $n){
    $res = [];
    $length = count($line);
    
    if ($n >= 0) {
        for($i = 0; $i <= $n; $i++){
            $res[] = "0";
        }
        foreach($line as $char){
            $res[] = $char;
        }
        for($i = 0; $i < $length - $n - 1; $i++){
            $res[] = "0";
        }
    } else {
        $n = -($n+1);
        for($i = 0; $i < $length - $n - 1; $i++){
            $res[] = "0";
        }
        foreach($line as $char){
            $res[] = $char;
        }
        for($i = 0; $i <= $n; $i++){
            $res[] = "0";
        }
    }
    return $res;
}
?>