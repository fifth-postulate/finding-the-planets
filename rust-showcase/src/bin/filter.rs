extern crate simple_csv;

use std::env;
use std::fs::File;
use std::io::BufReader;
use std::str::FromStr;
use simple_csv::{SimpleCsvWriter, SimpleCsvReader};

fn main(){
    let args : Vec<String> = env::args().collect();
    let threshold = f64::from_str(&args[1]).expect("first argument should be the threshold");

    let f = File::open("assets/detrend.csv").expect("input CSV to exist.");
    let buf = BufReader::new(f);
    let reader = SimpleCsvReader::new(buf);

    let result: Vec<(f64, f64)> = reader
        .map (|r| r.unwrap())
        .map(data)
        .filter(|&(_,difference)| difference.abs() <= threshold)
        .collect();

    let o = File::create("assets/filter.csv").expect("could not create output CSV.");
    let mut writer = SimpleCsvWriter::new(o);

    for (time, filtered) in result {
        writer.write(
            &vec!(time.to_string(), filtered.to_string())
        ).unwrap();
    }
}

fn data(row: Vec<String>) -> (f64, f64) {
    let iter = row.iter();

    let raw: Vec<f64> = iter
        .map(|s| f64::from_str(s).unwrap())
        .collect();

    (raw[0], raw[3])
}
