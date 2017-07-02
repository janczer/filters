# Image

Repository with filters for image.

## Table of contents
1. [Mirror image](#mirror-image)
2. [Gray filter](#gray-filter)


## Mirror image

Function `mirror` get 3 parameters:

```
func mirror(img: BuffereImage, ox: Boolean, oy: Boolean): BuffereImage
```

- `img` - Image object
- `ox` - if `true` when image mirrored horizontally
- `oy` - if `true` when image mirrored vertically

### Simple usage

```
$ git clone https://github.com/janczer/filters
$ cd filters
$ scala mirror.scala
```

And you have two files `test.jpg` and `mirror.jpg`.

![eagle](test.jpg)
![eagle](mirror.jpg)

## Gray filter

Function gray get 2 parameters:

```
def gray(img: BufferedImage, typ: String): BufferedImage
```

- `img` - Image object
- `typ` - type of filter gray (avarage, lightness, luminosity)

### Simple usage

```
$ git clone https://github.com/janczer/filters
$ cd filters
$ scala gray.scala
```

And you have two files `test.jpg` and `gray.jpg`.

![eagle](test.jpg)
![eagle](gray.jpg)

