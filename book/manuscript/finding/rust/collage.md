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

### Position
When we created the single image, we did not have to think about positioning
explicitly. Because we want to make a collage we have some work to do.

First of all, lets state some facts.

1. Each image is 11x11 pixels.
2. There are 3599 rows of images.

The interesting thing about 3599 is that is 61x59. So we could make our collage
almost a square with 61 columns and 59 rows of single images. With 11x11 images
as base our collage will come in at 61x11 = 671 by 59x11 = 649.

Let's start by giving names to things. We start out with the tile base size,
i.e. the size of the original image. We are going to call that `BASE`. Next we
want 61 of our tiles to go horizontally, and we want 59 of our tiles to go
vertically. We will call these `HORIZONTAL_TILES` and `VERTICAL_TILES`
respectively.

```rust
const BASE: usize = 11;
const HORIZONTAL_TILES: usize = 61;
const VERTICAL_TILES: usize = 59;
```

Now we can express all the other dimensions in terms of our `BASE` and
`HORIZONTAL_TILES` and `VERTICAL_TILES`.

```rust
const WIDTH: usize = HORIZONTAL_TILES * BASE;
const HEIGHT: usize = VERTICAL_TILES * BASE;
const SIZE: usize = WIDTH * HEIGHT;
```

For example, `SIZE` is the number of pixels in our base tile. Let's continue and
figure out where the pixels go. There are two factors that determine the
position of the pixel. The which row that data is from, and which column the
data is in. 

We will start with the row. Because we have 61 images along the x-axis of our
collage, the `X`-offset will be

```rust
let offset_X = row_index % HORIZONTAL_TILES;
```

The `Iter` trait has a very nice method: 
[`enumerate`](https://doc.rust-lang.org/std/iter/trait.Iterator.html#method.enumerate).
What it does is besides iterating over the `row`, it also provides us with the
`row_index`. We should keep this in mind when we are putting things together.

After `HORIZONAL_TILES` rows, we need to increase the `Y`-offset with one. This amounts to

```rust
let offset_Y = row_index / HORIZONTAL_TILES;
```

Now for the offset within the image. The image is `BASE`x`BASE`. So given an original
index in the row, we have for the 

```rust
let offset_x = original_index % BASE;
let offset_y = original_index / BASE;
```

Now we can calculate the target index. For each `offset_Y` we need to go down an
entire `BASE` rows in our collage. This is `BASE`x`HORIZONTAL_TILES`x`BASE` (=
7381). For each `offset_X` we need to shift `BASE` pixels down. For each
`offset_y` we need to go down an entire row. This is `HORIZONTAL_TILES`x`BASE`
(= 671). Finally, for each `offset_x` we need to shift 1 pixel 
down. All together this is

```rust
let target_index = offset_Y * (BASE * HORIZONTAL_TILES * BASE) +
                   offset_X * BASE +
                   offset_y * (HORIZONTAL_TILES * BASE) +
                   offset_x
```

With these calculations we know where to paint the image pixel.

### Color
From our experience from creating an image we have a fairly good idea which
color to use. The only difference between the collage and the single image is
that we want to use the same scale for each image.

So instead of dividing our value by the maximum value of a single image, we
should divide by the global maximum.

Create a separate executable that will determine the global maximum of all the
measurements that we can use in determining the color of the pixel.

## Further Considerations
The following suggestions might help your understanding of the problem we
facing, i.e. detecting planets in our image.

Take a long good look at your collage. Write down what you notice about the
image. Ask yourself some questions and discuss your observations with somebody
else. 

Why do we need a global maximum? What would happen if we would stick to the
maximum per image? What would that look like, and what would it tell you?
