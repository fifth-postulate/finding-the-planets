# Finding the Planets
A workshop that allows one to discover exo-planets.

## Trappist-1
[Trappist-1][trappist] is a

> planetary system, located 12 parsecs away from the Solar system (39 light
> years), near the ecliptic, within the constellation of Aquarius. 

Trappist-1 appeared in the news because it was [announced][press-release] that
is orbited by at least seven planets. 

## Workshop
During this workshop you will discover the planets around Trappist-1 on your
own.

We will find the planets with data made available by
NASA's [K2/Kepler telescope][K2]. The mission is

> search for Earth-like planets around Sun-like stars

The [Trappist-1 data][data] is publicly accessible. [Geert Barentsen][barentsen]
has made some interesting tools to analyze the data. For example, take a look at
this [IPython notebook][notebook].

## Info
The following information is presented in the headers of the data

### Long

```plain
  Binary Table
      Header Information:
          12 fields, 3599 rows of length 2932
           1:Name=TIME; Format=D; 
           2:Name=TIMECORR; Format=E; 
           3:Name=CADENCENO; Format=J; 
           4:Name=RAW_CNTS; Format=121J; Dimens=(11,11); 
           5:Name=FLUX; Format=121E; Dimens=(11,11); 
           6:Name=FLUX_ERR; Format=121E; Dimens=(11,11); 
           7:Name=FLUX_BKG; Format=121E; Dimens=(11,11); 
           8:Name=FLUX_BKG_ERR; Format=121E; Dimens=(11,11); 
           9:Name=COSMIC_RAYS; Format=121E; Dimens=(11,11); 
           10:Name=QUALITY; Format=J; 
           11:Name=POS_CORR1; Format=E; 
           12:Name=POS_CORR2; Format=E; 
      Data Information:
          Number of rows=3599
          Number of columns=12
           0:double[3599]
           1:float[3599]
           2:int[3599]
           3:int[435479]
           4:float[435479]
           5:float[435479]
           6:float[435479]
           7:float[435479]
           8:float[435479]
           9:int[3599]
           10:float[3599]
           11:float[3599]
```


### Short

```plain
  Binary Table
      Header Information:
          12 fields, 107968 rows of length 2932
           1:Name=TIME; Format=D; 
           2:Name=TIMECORR; Format=E; 
           3:Name=CADENCENO; Format=J; 
           4:Name=RAW_CNTS; Format=121J; Dimens=(11,11); 
           5:Name=FLUX; Format=121E; Dimens=(11,11); 
           6:Name=FLUX_ERR; Format=121E; Dimens=(11,11); 
           7:Name=FLUX_BKG; Format=121E; Dimens=(11,11); 
           8:Name=FLUX_BKG_ERR; Format=121E; Dimens=(11,11); 
           9:Name=COSMIC_RAYS; Format=121E; Dimens=(11,11); 
           10:Name=QUALITY; Format=J; 
           11:Name=POS_CORR1; Format=E; 
           12:Name=POS_CORR2; Format=E; 
      Data Information:
          Number of rows=107968
          Number of columns=12
           0:double[107968]
           1:float[107968]
           2:int[107968]
           3:int[13064128]
           4:float[13064128]
           5:float[13064128]
           6:float[13064128]
           7:float[13064128]
           8:float[13064128]
           9:int[107968]
           10:float[107968]
           11:float[107968]
```

## Table Column Formats
```plain
FITS format code         Description                     8-bit bytes

L                        logical (Boolean)               1
X                        bit                             *
B                        Unsigned byte                   1
I                        16-bit integer                  2
J                        32-bit integer                  4
K                        64-bit integer                  4
A                        character                       1
E                        single precision floating point 4
D                        double precision floating point 8
C                        single precision complex        8
M                        double precision complex        16
P                        array descriptor                8
Q                        array descriptor                16
```

## Results
### Images
#### First image created
Below you find the first image that I created from the *long cadence* FITS data.
To me it is still amazing that from a series of these 11 by 11 images one can
obtain so much information.

![First image created](https://cdn.rawgit.com/fifth-postulate/finding-the-planets/67b084085e476b2d43aff269dc272996e8b0a4ed/java/src/main/resources/first-image.png)

#### Collage
There are 3599 such images which fit nicely into a 61 times 59 rectangle.
Starring at the image you could start to notice certain peculiarities. Which do
you see? 

![Collage made the number of images is 61*59](https://cdn.rawgit.com/fifth-postulate/finding-the-planets/fd091991ad956f572d583f902b635a572d509968/java/src/main/resources/collage.png)

### Average
The average brightness, as can be seen from the collage, seems to steadily increase.

![The average brightness](https://cdn.rawgit.com/fifth-postulate/finding-the-planets/5a3fd3fb99d0ff60a7bf567c2e3eedf1ea69ff88/java/src/main/resources/average.png)

### Smoothed Average
The average brightness and the exponentially smoothed average, plotted against the time.

![The smoothed average brightness against time](https://cdn.rawgit.com/fifth-postulate/finding-the-planets/2fdd37a4ce17afb97dca1752fbcd530ee1f6c58a/java/src/main/resources/average-long.png)

### Detrended
Detrending the light curve produces the following images

![Detrended light curve](https://cdn.rawgit.com/fifth-postulate/finding-the-planets/417e873f6901d7f428e158187872af6aff741f00/java/src/main/resources/detrended-long.png)

### Discrete Fourier Transform
Calculating the discrete Fourier transform produces

![Discrete Fourier Transform](https://cdn.rawgit.com/fifth-postulate/finding-the-planets/7b49b7a7a6d2f785a3f978ce7082f00a0f7e45ea/java/src/main/resources/fft.png)

### Detrended Filtered
Removing how frequency components from the Fourier transform and inverting the
transformation produces a filtered sequence.

![Filtered detrended light curve](https://cdn.rawgit.com/fifth-postulate/finding-the-planets/5b87ce9d3985ce7699a1d26afaa2996840fae089/java/src/main/resources/ifft.png)

![Zoomed in filtered detrended light curve](https://cdn.rawgit.com/fifth-postulate/finding-the-planets/f4e3162d44bdb22a438589e7dc9371aa241e2d71/java/src/main/resources/ifft-zoom.png)

[trappist]: www.trappist.one
[press-release]: https://www.nasa.gov/press-release/nasa-telescope-reveals-largest-batch-of-earth-size-habitable-zone-planets-around
[K2]: https://keplerscience.arc.nasa.gov/
[data]: https://keplerscience.arc.nasa.gov/raw-data-for-k2-campaign-12-and-trappist-1-now-available.html
[barentsen]: https://github.com/barentsen
[notebook]: https://github.com/mrtommyb/trappist-lc/blob/master/TRAPPIST-1-transit-code.ipynb
