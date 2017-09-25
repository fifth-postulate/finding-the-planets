extern crate simple_csv;

use std::fs::File;
use std::io::BufReader;
use std::str::FromStr;
use simple_csv::{SimpleCsvWriter, SimpleCsvReader};

fn main(){
    let f = File::open("../long-cadence.csv").unwrap();
    let buf = BufReader::new(f);
    let reader = SimpleCsvReader::new(buf);

    let o = File::create("brightness.csv").unwrap();
    let mut writer = SimpleCsvWriter::new(o);

    for (_, r) in reader.enumerate() {
        let row = r.unwrap();
        let (time, brightness, filtered) = sum(row);

        writer.write(
            &vec!(time.to_string(), brightness.to_string(), filtered.to_string())
        ).unwrap();
    }
}

fn sum(row: Vec<String>) -> (f64, f64, f64) {
    let mut iter = row.iter();
    let time: f64 = f64::from_str(iter.next().unwrap()).unwrap();

    let raw: Vec<f64> = iter
        .map(|s| f64::from_str(s).unwrap())
        .collect();
    let sum: f64 = raw
        .iter()
        .fold(0f64, |acc, v| acc+v);
    let average = sum / (row.len() as f64);
    let filtered: f64 = raw
        .iter()
        .filter(|&v| *v >= average)
        .fold(0f64, |acc, v| acc+v);

    (time, sum, filtered)
}
