extern crate simple_csv;
extern crate png;

use std::env;
use std::fs::File;
use std::io::{BufWriter, BufReader};
use std::str::FromStr;
use png::HasParameters;
use simple_csv::SimpleCsvReader;

fn main(){
    let f = File::open("../long-cadence.csv").expect("input CSV to exist");
    let buf = BufReader::new(f);
    let mut reader = SimpleCsvReader::new(buf);
    let row = reader.next_row().unwrap().unwrap();
    let mut current_row = row.iter();
    current_row.next(); // dropping time

    let raw: Vec<f64> = current_row
        .map(|s| f64::from_str(s).unwrap())
        .collect();
    let sum: f64 = raw
        .iter()
        .sum();
    let average = sum/(raw.len() as f64);
    let maximum = raw
        .iter()
        .fold(std::f64::MIN, |acc, v| acc.max(*v));
    let data: Vec<u8> = raw
        .iter()
        .map(|s| if s >= &average { s*1.0 } else { s*0.0 })
        .map(|s| s/maximum)
        .map(|s| 255.0 * s)
        .map(|s| s.floor() as u8)
        .collect();

    let mut path = env::current_dir().expect("could not read current directory.");
    path.push(format!("assets/trappist-1.{}.nobg.png", 0));

    let file = File::create(path).expect("could not write output CSV.");
    let ref mut w = BufWriter::new(file);

    let mut encoder = png::Encoder::new(w, 11, 11);
    encoder.set(png::ColorType::Grayscale).set(png::BitDepth::Eight);
    let mut writer = encoder.write_header().unwrap();

    writer.write_image_data(data.as_slice()).unwrap();
}
