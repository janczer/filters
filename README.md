# Image

Repository with filters for image.

## Table of contents
1. [Mirror image](#mirror-image)
2. [Gray filter](#gray-filter)
3. [Sepia filter](#sepia-filter)
4. [Inverse filter](#inverse-filter)
5. [Split image to RGB channels](#split-image-to-rgb-channels)
6. [Color accent filter](#color-accent-filter)
7. [Histogram](#histogram)


## Mirror image

Function `mirror` get 3 parameters:

```
func mirror(img: BuffereImage, ox: Boolean, oy: Boolean): BuffereImage
```

- `img` - Image object
- `ox` - if `true` when image mirrored horizontally
- `oy` - if `true` when image mirrored vertically

And you will have one file `mirror.jpg`.

![eagle](images/test.jpg)
![eagle](images/mirror.jpg)

## Gray filter

Function gray get 2 parameters:

```
def gray(img: BufferedImage, typ: String): BufferedImage
```

- `img` - Image object
- `typ` - type of filter gray (avarage, lightness, luminosity)

And you will have one file `gray.jpg`.

![eagle](images/test.jpg)
![eagle](images/gray.jpg)

## Sepia filter

Sepia filter work on gray scale images.

Function sepia get 2 parameters:

```
def gray(img: BufferedImage, sp: Int): BufferedImage
```

- `img` - Image object
- `sp` - factor of sepia effect (20-40)

And you will have one file `sepia.jpg`.

![eagle](images/gray.jpg)
![eagle](images/sepia.jpg)

## Inverse filter

Function inverse get 1 parameter:

```
def inverse(img: BufferedImage): BufferedImage
```

- `img` - Image object

And you will have one file `inverse.jpg`.

![eagle](images/test.jpg)
![eagle](images/inverse.jpg)

## Split image to RGB channels

Function rgb get 3 parameters:

```
def rgb(img: BufferedImage, r: Boolean, g: Boolean, b: Boolean): BufferedImage
```

- `img` - Image object
- `r` - Show only red channel
- `g` - Show only green channel
- `b` - Show only blue channel

And you will have 3 files `red.jpg`, `green.jpg` and `blue.jpg`.

![eagle](images/test.jpg)
![eagle](images/red.jpg)
![eagle](images/green.jpg)
![eagle](images/blue.jpg)

## Color accent filter

Function color_accent get 3 parameters:

```
def color_accent(img: BufferedImage, hue: Int, range: Int): BufferedImage
```

- `img` - Image object
- `hue` - Color in HSV
- `range` - Range around color `hue`

| Original | Hue = 0, Range = 50 | Hue = 70, Range = 50 | Hue = 200, Range = 50 |
| -------- | ------------------- | -------------------- | --------------------- |
| ![poppies](images/test2.jpg) | ![eagle](images/color_accent2.jpg) | ![eagle](images/color_accent1.jpg) | ![eagle](images/color_accent.jpg) |

## Histogram

Function histogram get 1 parameter:

```
def histogram(img: BufferedImage): BufferedImage
```

- `img` - Image object

| Original | Histogram |
| -------- | ------------------- |
| ![eagle](images/test.jpg) | ![eagle](images/histogram.png) |
| ![poppies](test2.jpg) | ![poppies](images/histogram2.png) |
| ![poppies gray](images/gray2.jpg) | ![poppies](images/histogram_gray.png) |
