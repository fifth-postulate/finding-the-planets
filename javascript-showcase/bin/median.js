const fs = require('fs');
const parse = require('csv-parse');
const stringify = require('csv-stringify');
const transform = require('stream-transform');

const lib = require('../lib');
const median_of = lib.median_of;
const SlidingWindow = lib.sliding_window;

var input = fs.createReadStream('filter.csv');
var output = fs.createWriteStream('median.csv');
var parser = parse();
var stringifier = stringify();

var size = 10;
var times = new SlidingWindow(size);
var values = new SlidingWindow(size);
var transformer = transform(function(data){
    const time = parseFloat(data[0]);
    const value = parseFloat(data[3]);

    times.push(time);
    values.push(value);

    var tw = times.result();
    var vw = value.result();
    if (tw != null && vw != null) {
        var tm = median_of(tw);
        var vm = median_of(vw);

        return [tm, vm];
    } else {
        return null;
    }
});

input
    .pipe(parser)
    .pipe(transformer)
    .pipe(stringifier)
    .pipe(output);
