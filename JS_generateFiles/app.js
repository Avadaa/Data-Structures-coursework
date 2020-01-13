let fs = require('fs');

// Create two files, each with 'amount' amount of lines. 
// Each line contains a number between 1 and 'amount'
let stream = fs.createWriteStream('./testA.txt');
let amount = 1000
for (let i = 0; i < amount; i++) {
    let line = Math.floor(Math.random() * amount + 1).toString()
    stream.write(line + '\n')
}

stream = fs.createWriteStream('./testB.txt');
for (let i = 0; i < amount; i++) {
    let line = Math.floor(Math.random() * amount + 1).toString()
    stream.write(line + '\n')
}
stream.close();
