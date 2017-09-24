# Collage
In this chapter we will create a Collage of all the image in the long cadence
file. Although it is a bit of a side-track, we will learn valuable things by
looking at the image.

If you want, you can take a [sneak peek](image/collage.png).

We will not go into details of reading the data and writing the transformed
data. We assume that the previous chapters have given enough examples to learn
from. Instead we are going to focus on processing the data.

## Processing
There are a few questions we need to answer before we can create our collage.

1. For a row of data and a column in that row, which pixel should we paint?
2. What color should we paint that pixel?

### Color
From our experience from creating an image we have a fairly good idea which
color to use. The only difference between the collage and the single image is
that we want to use the same scale for each image.

So instead of dividing our value by the maximum value of a single image, we
should divide by the global maximum.

Create a separate executable that will determine the global maximum of all the
measurements that we can use in determining the color of the pixel.
