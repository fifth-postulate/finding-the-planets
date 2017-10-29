extern crate simple_csv;
extern crate find_planets;

use std::env;
use std::fs::File;
use std::io::BufReader;
use std::str::FromStr;
use std::iter::Iterator;
use simple_csv::{SimpleCsvWriter, SimpleCsvReader};
use find_planets::median::median_filter;

fn main(){
    let args : Vec<String> = env::args().collect();
    let window_size = usize::from_str(&args[1]).expect("first argument should be the window size");

    let f = File::open("assets/filter.csv").expect("input CSV to exist.");
    let buf = BufReader::new(f);
    let reader = SimpleCsvReader::new(buf);

    let raw: Vec<(f64, f64)> = reader
        .map (|r| r.unwrap())
        .map(data)
        .collect();

    let (times, values): (Vec<f64>, Vec<f64>) = raw
        .iter()
        .cloned()
        .unzip();

    let median_times = median_filter(&times, window_size);
    let median_values = median_filter(&values, window_size);

    let result = median_times.iter().zip(median_values);

    let o = File::create("assets/median.csv").expect("could not write output CSV.");
    let mut writer = SimpleCsvWriter::new(o);

    for (time, median) in result {
        writer.write(
            &vec!(time.to_string(), median.to_string())
        ).unwrap();
    }
}

fn data(row: Vec<String>) -> (f64, f64) {
    let iter = row.iter();

    let raw: Vec<f64> = iter
        .map(|s| f64::from_str(s).unwrap())
        .collect();

    (raw[0], raw[1])
}
