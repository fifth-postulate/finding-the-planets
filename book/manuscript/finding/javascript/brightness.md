# Brightness
We are going to detect the planets by observing drops in overall brightness.
Before we are able to do this, we need to calculate the brightness. That is
precisely the objective in this chapter.

We are going to create a CSV file with the first column the time of the
measurement and the second column the brightness at that time.

## Processing
For each row of data we would like to know how much Trappist-1 is radiating.
What we are going to do is the following.

Take a row of data and sum all the values to get the overall brightness.

The summation of all the values can be written down very succinctly because the
[`Array`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/prototype)
prototype has a trick up it's sleeve. The `Array.prototype` has a `reduce` method. We
can use it to calculate the sum of all the brightness values. If we have our values
in the variable `brightness`, we can determine the sum with 

```javascript
const sum = brightness.
    reduce(function(partial_sum, value){
        return partial_sum + value;
    });
```

### Removing Background
If we take a look at one of the images

![Enlargement of an image of Trappist-1](image/trappist-1.0.large.png)

we see that the background is not pitch black. This means that the background
adds to the brightness, even though it does not contribute to the signal. So we
start our journey with something we will come very familiar with, we will clean
up our data.

What we are going to do is ignore the brightness value of anything below the
average brightness. This transforms the image from above into the image below.

![Enlargement of an image of Trappist-1 with background removed](image/trappist-1.0.nobg.large.png)

Still not perfect, but it is better than nothing.

In order to filter out the unwanted background we are going to need to know the
average. We already know the sum, we just calculated it, so the average can be
determined by

```javascript
const average = sum / brightness.length;
```

Calculating the contribution of the values above the average can still be done
succinctly. What we need to do is filter out the values that we want to sum.
I.e. the values above the average.

```javascript
const filtered_sum_= brightness
    .filter(function(value){ return value >= average; })
    .reduce(function(partial_sum, value){ return partial_sum + value; });
```

What we want to return in our transformer is the pair of the `time`, we is the
first value of our row of data i.e. `const time = data[0]`, and our
`filtered_sum`.

```javascript
return [time, filtered_sum];
```

## Graphing Results
Once you wrote your brightness results to a CSV file, they are ready for the
following step. But if you are like me you probably want to see your results.
This is where gnuplot comes in.

If you have saved your results as `brightness.csv`, the following gnuplot
session will plot your data.

```
set datafile separator ','

plot [2905:2985] "brightness.csv" using 1:2
```

We will annotate the above example a little, so that you can use gnuplot on your
own. The `simple_csv` library outputs CSV files with a comma as separator. This
difference from the default assumption of gnuplot. Luckily this can be remedied
with the first line.

The second line display the core of gnuplot; the `plot` command. The first
argument, i.e. `[2905:2985]`, defines the range on the x-axis. It is optional
and will be inferred by gnuplot if it isn't present. If there would be a second
argument of that form, i.e. `[min:max]`, that would be the range on the y-axis.
Here it is inferred.

The `"brightness.csv"` argument you probably recognize as the file you wrote
your data to. The `plot` command will use data in this file to plot.

The last refers to columns in the data. `using 1:2` tells the plot command to
plot point with the first column as x-coordinate and the second column as
y-coordinate.

For a more extensive explanation of gnuplot we refer you to the 
[gnuplot homepage](http://www.gnuplot.info/).

If you have gone to the trouble of outputting the brightness with and without
the background, your plot could look like the one below.

![Plot of brightness, with and without background contribution](image/brightness-both.png)

## Further Considerations
Take a look at your data and write down what stands out to you. Discuss this
with a neighbor.

Why is the average taken as a cut-off value? What are other options? 

There is an obvious gap in our data. This is where the Kepler satellite stopped
recording data due to a software reboot initiated by a cosmic ray event.
Although the data was lost, the satellite still operates nominally.

Furthermore there is a trend in the overall brightness, more pronounced in the
data with the background. This is also seen in our collage. We will have to
smooth out that trend and that is precisely what we will in one of the next chapters.
