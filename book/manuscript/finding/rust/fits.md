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

We would like to use the FITS files directly, but unfortunately the
[library](https://crates.io/crates/fits-rs) is not production ready yet. We
created a Comma Seperated Values (CSV) file with the relevant data. 
