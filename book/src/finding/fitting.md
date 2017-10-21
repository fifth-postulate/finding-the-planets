# Fitting 
We have created a plot of the median.

![The median of the filtered brightness](image/median.png)

We would like to find planets in it. Finding planets amounts to selecting a
transit curve that nicely fits our data. We our going to divide that task in the
following sub-tasks.

1. Generating a transit curve series
2. Iterating over all transit curve parameters
3. Scoring each candidate transit curve and selecting the best

