extern crate simple_csv;
extern crate find_planets;

use std::fs::File;
use std::io::BufReader;
use std::str::FromStr;
use std::iter::Iterator;
use std::f64;
use simple_csv::{SimpleCsvWriter,SimpleCsvReader};
use find_planets::fit::iteration::{FloatRange, TransitParametersIterator};
use find_planets::fit::transit::Transit;
use find_planets::fit::score::least_squares;

fn main(){
    let f = File::open("assets/median.csv").expect("input CSV to exist.");
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

    let period_range = FloatRange::new(0.0, 1.0, 0.1);
    let base_range = FloatRange::new(0.0, 1.0, 0.1);
    let depth_range = FloatRange::new(0.0, 1.0, 0.1);
    let duration_range = FloatRange::new(0.0, 1.0, 0.1);
    let decay_range = FloatRange::new(0.0, 1.0, 0.1);
    let phase_range = FloatRange::new(0.0, 1.0, 0.1);

    let mut best_score = f64::MAX;
    let mut best_transit: Option<Transit> = None;
    for parameters in TransitParametersIterator::new(
        period_range, base_range, depth_range, duration_range, decay_range, phase_range
    ) {
        let transit = Transit::new(parameters);
        let transit_values: Vec<f64> = times.iter().map(|t| transit.value(t)).collect();
        let score = least_squares(&transit_values, &values);

        if score < best_score {
            best_score = score;
            best_transit = Some(transit.clone());
        }
    }

    let best_transit = best_transit.unwrap();
    let best_transit_values: Vec<f64> = times.iter().map(|t| best_transit.value(t)).collect();
    let result = times.iter().zip(best_transit_values);

    println!("{:?}", best_transit);
    let o = File::create("assets/fit.csv").expect("could not write output CSV.");
    let mut writer = SimpleCsvWriter::new(o);

    for (time, transit) in result {
        writer.write(
            &vec!(time.to_string(), transit.to_string())
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
