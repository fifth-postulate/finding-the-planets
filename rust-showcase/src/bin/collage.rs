extern crate simple_csv;
extern crate png;

use std::env;
use std::fs::File;
use std::io::{BufWriter, BufReader};
use std::str::FromStr;
use png::HasParameters;
use simple_csv::SimpleCsvReader;

const BASE: usize = 11;
const HORIZONTAL_TILES: usize = 61;
const VERTICAL_TILES: usize = 59;
const WIDTH: usize = HORIZONTAL_TILES * BASE;
const HEIGHT: usize = VERTICAL_TILES * BASE;
const SIZE: usize = WIDTH * HEIGHT;
const MAXIMUM: f64 = 3923.0;

fn main(){
    let f = File::open("../long-cadence.csv").unwrap();
    let buf = BufReader::new(f);
    let reader = SimpleCsvReader::new(buf);

    let mut data: [u8; SIZE] = [0u8; SIZE];
    for (row_index, r) in reader.enumerate() {
        let row = r.unwrap();
        let partial_data = image_data(row);
        for origin_index in 0..BASE*BASE {
            let offset_x = row_index % HORIZONTAL_TILES;
            let offset_y = row_index / HORIZONTAL_TILES;
            let target_index = (origin_index / BASE) * WIDTH + origin_index % BASE +
                offset_y * BASE * WIDTH + offset_x * BASE;
            data[target_index] = partial_data[origin_index];
        }
    }

    let mut path = env::current_dir().unwrap();
    path.push("assets/collage.png");

    let file = File::create(path).unwrap();
    let ref mut w = BufWriter::new(file);

    let mut encoder = png::Encoder::new(w, 61*11, 59*11);
    encoder.set(png::ColorType::Grayscale).set(png::BitDepth::Eight);
    let mut writer = encoder.write_header().unwrap();

    writer.write_image_data(&data).unwrap();
}

fn image_data(row: Vec<String>) -> Vec<u8> {
    let mut iter = row.iter();
    iter.next(); // dropping time

    let raw: Vec<f64> = iter
        .map(|s| f64::from_str(s).unwrap())
        .collect();
    let data: Vec<u8> = raw
        .iter()
        .map(|s| s/MAXIMUM)
        .map(|s| 255.0 * s)
        .map(|s| s.floor() as u8)
        .collect();

    data
}
