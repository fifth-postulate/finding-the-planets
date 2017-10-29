extern crate simple_csv;

use std::env;
use std::fs::File;
use std::io::BufReader;
use std::str::FromStr;
use simple_csv::{SimpleCsvWriter, SimpleCsvReader};

fn main(){
    let args : Vec<String> = env::args().collect();
    let alpha = f64::from_str(&args[1]).expect("first argument should be the threshold");

    let f = File::open("assets/brightness.csv").expect("input CSV to exist.");
    let buf = BufReader::new(f);
    let reader = SimpleCsvReader::new(buf);

    let raw: Vec<(f64, f64)> = reader
        .map (|r| r.unwrap())
        .map(data)
        .collect();

    let mut sequence: Vec<DetrendData> = vec!();
    let mut data: Option<DetrendData> = None;
    for candidate in raw {
        match data{
            Some(previous) => {
                let next = previous.next(candidate, alpha);
                sequence.push(previous);
                data = Some(next);
            }

            None => {
                data = Some(DetrendData::initial(candidate))

            }
        }
    }

    let o = File::create("assets/detrend.csv").expect("could not write output CSV.");
    let mut writer = SimpleCsvWriter::new(o);

    for data in sequence {
        writer.write(
            &data.as_vec()
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

struct DetrendData {
    time: f64,
    brightness: f64,
    trend: f64,
    difference: f64,
}

impl DetrendData {
    fn initial((time, brightness): (f64,f64)) -> DetrendData {
        DetrendData {
            time: time,
            brightness: brightness,
            trend: brightness,
            difference: 0f64,
        }
    }

    fn next(&self, (time, brightness): (f64, f64), alpha: f64) -> DetrendData {
        let trend = alpha * brightness + (1f64 - alpha) * self.trend;
        DetrendData {
            time: time,
            brightness: brightness,
            trend: trend,
            difference: brightness - trend,
        }

    }

    fn as_vec(&self) -> Vec<String> {
        vec!(
            self.time.to_string(),
            self.brightness.to_string(),
            self.trend.to_string(),
            self.difference.to_string(),
        )
    }
}
