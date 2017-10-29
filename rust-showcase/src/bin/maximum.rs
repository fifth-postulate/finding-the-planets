extern crate simple_csv;

use std::fs::File;
use std::io::BufReader;
use std::str::FromStr;
use simple_csv::SimpleCsvReader;

fn main(){
    let f = File::open("../long-cadence.csv").expect("input CSV to exist.");
    let buf = BufReader::new(f);
    let reader = SimpleCsvReader::new(buf);

    let mut maximum = std::f64::MIN;
    for r in reader {
        let row = r.unwrap();
        maximum = maximum.max(maximum_of(row));
    }

    println!("{}", maximum);
}


fn maximum_of(row: Vec<String>) -> f64 {
    let mut iter = row.iter();
    iter.next(); // dropping time

    let raw: Vec<f64> = iter
        .map(|s| f64::from_str(s).unwrap())
        .collect();
    let maximum = raw
        .iter()
        .fold(std::f64::MIN, |acc, v| acc.max(*v));

    maximum
}
