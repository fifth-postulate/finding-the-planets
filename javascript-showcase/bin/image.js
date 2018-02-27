const fs = require('fs');
const parse = require('csv-parse');
const stringify = require('csv-stringify');

var input = fs.createReadStream('../workshop/long-cadence.csv');
var parser = parse({ delimiter: ',' });
var stringifier = stringify({ delimiter: ',' });

input.pipe(parser).pipe(stringifier).pipe(process.stdout);
