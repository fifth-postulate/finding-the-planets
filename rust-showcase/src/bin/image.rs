extern crate simple_csv;
extern crate png;

use std::env;
use std::fs::File;
use std::io::BufWriter;
use png::HasParameters;

fn main(){
    let mut path = env::current_dir().unwrap();
    path.push("trappist-1.0.png");

    let file = File::create(path).unwrap();
    let ref mut w = BufWriter::new(file);

    let mut encoder = png::Encoder::new(w, 2, 1);
    encoder.set(png::ColorType::RGBA).set(png::BitDepth::Eight);
    let mut writer = encoder.write_header().unwrap();

    let data = [255, 0, 0, 255, 0, 0, 0, 255];
    writer.write_image_data(&data).unwrap();
}
