const fs = require('fs');
const parse = require('csv-parse');
const stringify = require('csv-stringify');
const transform = require('stream-transform');

var input = fs.createReadStream('brightness.csv');
var output = fs.createWriteStream('detrend.csv');
var parser = parse();
var stringifier = stringify();

const alpha = 0.8;
var previous_detrend_data = undefined;
var transformer = transform(function(data){
    const time = parseFloat(data[0]);
    const brightness = parseFloat(data[1]);

    var detrend_data;
    if (previous_detrend_data) {
        const trend = alpha * brightness + (1 - alpha) * previous_detrend_data.trend;
        const difference = brightness - trend;
        detrend_data = {
            'time': time,
            'brightness': brightness,
            'trend': trend,
            'difference': difference
        };
    } else {
        detrend_data = {
            'time': time,
            'brightness': brightness,
            'trend': brightness,
            'difference': 0
        };
    }
    previous_detrend_data = detrend_data;

    return [detrend_data.time, detrend_data.brightness, detrend_data.trend, detrend_data.difference];
});

input
    .pipe(parser)
    .pipe(transformer)
    .pipe(stringifier)
    .pipe(output);
