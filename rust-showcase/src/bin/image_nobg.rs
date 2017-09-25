extern crate simple_csv;
extern crate png;

use std::env;
use std::fs::File;
use std::io::{BufWriter, BufReader};
use std::str::FromStr;
use png::HasParameters;
use simple_csv::SimpleCsvReader;

fn main(){
    let f = File::open("../long-cadence.csv").unwrap();
    let buf = BufReader::new(f);
    let mut reader = SimpleCsvReader::new(buf);
    let row = reader.next_row().unwrap().unwrap();
    let mut iter = row.iter();
    iter.next(); // dropping time

    let raw: Vec<f64> = iter
        .map(|s| f64::from_str(s).unwrap())
        .collect();
    let sum = raw
        .iter()
        .fold(0f64, |acc, v| acc+v);
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

    let mut path = env::current_dir().unwrap();
    path.push(format!("assets/trappist-1.{}.nobg.png", 0));

    let file = File::create(path).unwrap();
    let ref mut w = BufWriter::new(file);

    let mut encoder = png::Encoder::new(w, 11, 11);
    encoder.set(png::ColorType::Grayscale).set(png::BitDepth::Eight);
    let mut writer = encoder.write_header().unwrap();

    writer.write_image_data(data.as_slice()).unwrap();
}
