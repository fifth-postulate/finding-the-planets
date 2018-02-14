# FITS
The results of the NASA Kepler mission on observing Trappist-1 are
[available](https://keplerscience.arc.nasa.gov/raw-data-for-k2-campaign-12-and-trappist-1-now-available.html)
to the public. For your ease of use we downloaded the FITS files before hand. 

## What are FITS files
A [FITS](https://en.wikipedia.org/wiki/FITS) file is a

>  open standard defining a digital file format useful for storage,
>  transmission and processing of scientific and other images. FITS is the most
>  commonly used digital file format in astronomy. Unlike many image formats,
>  FITS is designed specifically for scientific data and hence includes many
>  provisions for describing photometric and spatial calibration information,
>  together with image origin metadata. 

We are going to use the [fits-rs](https://crates.io/crates/fits-rs) crate to
process the FITS files. Unfortunatly the crate is not production ready. Although we can use the crate to read the headers, it doesn't parse the table data just yet. For now the relevant data is converted into Comma Seperated Values (CVS).

For an example of what you can do with that crate see below. Note that it needs
a other libraries, not included in the workshop material.

```rust
    let args: Vec<String> = env::args().collect();
    let filename = &args[1];
    let header_index = u64::from_str(&args[2]).expect("should be a non-negative number");

    let mut f = File::open(filename).expect("file not found");
    let mut buffer: Vec<u8> = vec!();
    let _ = f.read_to_end(&mut buffer);

    let result = fits(&buffer);

    match result {
        IResult::Done(_, trappist1) => {
            let header: &Header = if header_index = 0 {
                &trappist1.primary_hdu.header
            } else {
                &trappist1.extensions[header_index].header
            }

            for record in header.keyword_records {
                println!("{:?}", record);
            }
        },
        _ => panic!("Whoops, something went wrong")
    }
```
