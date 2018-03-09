const fs = require('fs');
const parse = require('csv-parse');
const stringify = require('csv-stringify');
const transform = require('stream-transform');

var input = fs.createReadStream('detrend.csv');
var output = fs.createWriteStream('filter.csv');
var parser = parse();
var stringifier = stringify();

const threshold = 10.0;
var previous_detrend_data = undefined;
var transformer = transform(function(data){
    const time = parseFloat(data[0]);
    const brightness = parseFloat(data[1]);
    const trend = parseFloat(data[2]);
    const difference = parseFloat(data[3]);

    if (Math.abs(difference) <= threshold) {
        return data;
    } else {
        return null;
    }
});

input
    .pipe(parser)
    .pipe(transformer)
    .pipe(stringifier)
    .pipe(output);
