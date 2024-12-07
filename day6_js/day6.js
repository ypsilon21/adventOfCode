import { dir } from 'node:console';
import fs from 'node:fs/promises';

async function main(){
    try{
        var input = await readFile();
        var position = getPosition(input);
        var direction = toDirection(input[position[0]][position[1]]);
        input = exchange(input, position, '.');
        var trail = simulateRun(input, position, direction);
        console.log("First result:  " + countTrail(trail));
        var loopPositions = findLoops(input, trail, position, direction);
        console.log("Second Result: " + loopPositions);
    }
    catch(err){
        console.log(err);
    }
    
}

async function readFile() {
    try {
        const data = await fs.readFile('input.txt', 'utf8');
        return to2DArray(data);
    } catch (err) {
        console.error(err);
        throw err;
    }
}

function to2DArray(data){
    var lines = data.split('\n');
    var res = [];
    for(let i = 0; i < lines.length; i++){
        res.push(lines[i].trim().split(''));
    }
    return res;
}

function getPosition(matrix){
    for(let i = 0; i < matrix.length; i++){
        for(let j = 0; j < matrix[0].length; j++){
            if(['<', '>', '^', 'v'].includes(matrix[i][j])){
                return [i, j];
            }
        }
    }
}

function exchange(matrix, pos, newChar){
    var i = pos[0];
    var j = pos[1];
    matrix[i][j] = newChar;
    return matrix;
}

function toDirection(char){
    switch(char){
        case '^':
            return 0;
        case '>':
            return 1;
        case 'v':
            return 2;
        case '<':
            return 3;
        default:
            console.log("couldnt match direction");
            return -1;
    }
}

function simulateRun(matrix, pos, dir){
    let visited = [];
    let previousMoves = [pos[0], pos[1], dir];
    for(let i = 0; i < matrix.length; i++){
        var temp = [];
        for(let j = 0; j < matrix[0].length; j++){
            temp.push(0);
        }
        visited.push(temp);
    }
    visited[pos[0]][pos[1]] = true;
    while(true){
        let dirPrev = dir;
        let nextMove = getNextMove(matrix, pos, dir);

        pos = nextMove[0];
        dir = nextMove[1];

        if(dirPrev != dir){
            if(scanForLoop(previousMoves, [nextMove[0][0], nextMove[0][1], nextMove[1]])) return false;
            else{
                previousMoves.push([nextMove[0][0], nextMove[0][1], nextMove[1]]);
            }
        }

        if(pos[0] == -1) break;
        else visited[pos[0]][pos[1]] = 1;
    }
    return visited;
}

function getNextMove(matrix, pos, dir){
    switch(dir){
        case 0: 
            if(pos[0] == 0) return [[-1, -1], 0];
            else if(matrix[pos[0] - 1][pos[1]] == '.') return [[pos[0] - 1, pos[1]], 0];
            else return [[pos[0], pos[1]], 1];
        case 1: 
            if(pos[1] == matrix[0].length - 1) return [[-1, -1], 0];
            else if(matrix[pos[0]][pos[1] + 1] == '.') return [[pos[0], pos[1] + 1], 1];
            else return [[pos[0], pos[1]], 2];
        case 2: 
            if(pos[0] == matrix.length - 1) return [[-1, -1], 0];
            else if(matrix[pos[0] + 1][pos[1]] == '.') return [[pos[0] + 1, pos[1]], 2];
            else return [[pos[0], pos[1]], 3];
        case 3: 
            if(pos[1] == 0) return [[-1, -1], 0];
            else if(matrix[pos[0]][pos[1] - 1] == '.') return [[pos[0], pos[1] - 1], 3];
            else return [[pos[0], pos[1]], 0];
        default: 
            console.log("error getNextMove " +  dir);
            return [[-1, -1], 0];
    }
}

function countTrail(matrix){
    var res = 0;
    for(let i = 0; i < matrix.length; i++){
        for(let j = 0; j < matrix[0].length; j++){
            if(matrix[i][j] == 1) res++;
        }
    }
    return res;
}

function findLoops(matrix, trail, pos, dir){
    var res = 0;
    for(let i = 0; i < matrix.length; i++){
        for(let j = 0; j < matrix[0].length; j++){
            if(trail[i][j] == 0 || (pos[0] == i && pos[1] == j)) continue;
            if(matrix[i][j] == '.'){
                matrix[i][j] = '#';
                if(!simulateRun(matrix, pos, dir)){
                    res ++;
                }
                matrix[i][j] = '.';
            }
        }
    }
    return res;
}

function scanForLoop(list, next){
    for(let i = list.length - 1; i >= 0; i--){
        if(list[i][0] == next[0] && list[i][1] == next[1] && list[i][2] == next[2])return true;
    }
    return false;
}

main();