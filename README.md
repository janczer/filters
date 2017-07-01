# Image

Repository with filter for image.


## Mirror image

Function `mirror` get 3 parameters:

```
func mirror(img: BuffereImage, ox: Boolean, oy: Boolean): BuffereImage
```

- `in` - object for mirror
- `ox` - if `true` when image mirrored horizontally
- `oy` - if `true` when image mirrored vertically

## Simple usage

```
$ git clone https://github.com/janczer/filters
$ cd filters
$ scala mirror.scala
```

And you have two files `test.jpg` and `mirror.jpg`.

![eagle](test.jpg)
![eagle](mirror.jpg)


