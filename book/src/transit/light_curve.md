# What To Look For
We are forming a model of a planet transit in order to make some calculations
what to expect. Our model will be fairly crude, but it will suffices for
understanding key characteristics of light curves.

This section has some math in it. It is used to understand how a light curve
will look when a planet transits. Feel free to skip to the result if you feel
inclined. 

## Light Curve
A [light curve](https://en.wikipedia.org/wiki/Light_curve) is

> a graph of light intensity of a celestial object or region, as a function of
> time. 

It graphs how bright some object appears in the sky over time. Our goal is to
understand what the light curve looks like when a planet transits a star.

## Model
We are modeling our planet transit in the following crude manner. We assume the
star to be a square which radiates uniformly. The total luminosity is
\\(I_{0}\\). So the luminosity per area \\(\rho = \frac{I_{0}}{A}\\), where \\(A\\)
is the total area of the star.

We model our planet as a square as well. We will also assume that the planet
will move with uniform speed across the stars image during the transit. When the
planet is fully in front of the star, it block some of the rays of the star
diminishing the luminosity to \\(I_{t} = \rho \left(A - a\right)\\), where
\\(a\\) is the area of the planet.

We are interested in the relative drop in luminosity so we will divide
\\(I_{t}\\) by \\(I_{0}\\) to get

\\[
\frac{I_{t}}{I_{0}} = \frac{\rho \left(A - a\right)}{\rho A} = 1 - \frac{a}{A}
\\]

So the entire light curve looks something like this.

![A light curve for a planet transition](image/light-curve.png)
