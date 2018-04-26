package janczer.filters

import java.awt.image.BufferedImage

import collection.mutable.Stack
import org.scalatest._
import janczer.filters.Filters._

class FilterSpec extends FlatSpec {

  "Red channel" should "Return only first 2 digits" in {
    val test = 0x112233
    val red = red_channel(test)
    assert(red === 0x11)
  }

  "Green channel" should "Return only 2 digits in middle" in {
    val test = 0x112233
    val green = green_channel(test)
    assert(green === 0x22)
  }

  "Blue channel" should "Return only 2 last digits" in {
    val test = 0x112233
    val blue = blue_channel(test)
    assert(blue === 0x33)
  }

  "Func all_channels" should "Get all channels together" in {
    assert(0x112233 === all_channels(0x11, 0x22, 0x33))
  }

  "Func normalize_pixel" should "return values from 0 to 255" in {
    assert(0 === normalize_pixel(-100))
    assert(0 === normalize_pixel(-1))
    assert(255 === normalize_pixel(1000))
    assert(255 === normalize_pixel(256))
  }

  "Func buffered_image_with_pixels" should "return the same image" in {
    val img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    img.setRGB(0, 0, 0x112233)

    val pixel = buffered_image_with_pixels(img, (x: Int, y: Int) => img.getRGB(x, y)).getRGB(0, 0)

    assert(0x11 === red_channel(pixel))
    assert(0x22 === green_channel(pixel))
    assert(0x33 === blue_channel(pixel))
  }

  "Func mirror" should "return mirroring image" in {
    val img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB)
    // 0x1 0x2
    // 0x3 0x4
    img.setRGB(0, 0, 0x000001)
    img.setRGB(1, 0, 0x000002)
    img.setRGB(0, 1, 0x000003)
    img.setRGB(1, 1, 0x000004)

    val out = mirror(img, true, true)


    // 0x4 0x3
    // 0x2 0x1
    assert(0x4 === blue_channel(out.getRGB(0, 0)))
    assert(0x3 === blue_channel(out.getRGB(1, 0)))
    assert(0x2 === blue_channel(out.getRGB(0, 1)))
    assert(0x1 === blue_channel(out.getRGB(1, 1)))
  }

  "Func color_accent" should "return image with only one on color" in {
    val img = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB)
    // 0xff0000 0x00ff00 0x0000ff
    img.setRGB(0, 0, 0xff0000)
    img.setRGB(1, 0, 0x00ff00)
    img.setRGB(2, 0, 0x0000ff)

    val out = color_accent(img, 0, 50)
    assert(0xff === red_channel(out.getRGB(0, 0)))
    assert(0xff !== green_channel(out.getRGB(1, 0)))
    assert(0xff !== blue_channel(out.getRGB(2, 0)))
  }

  "Func inverse" should "return image with inverted colors" in {
    val img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    img.setRGB(0, 0, 0xffffff)

    val pixel = inverse(img).getRGB(0, 0)
    assert(0 === red_channel(pixel))
    assert(0 === green_channel(pixel))
    assert(0 === blue_channel(pixel))
  }

  "Func rgb_channels" should "return image with some channel" in {
    val img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    img.setRGB(0, 0, 0xffffff)

    var pixel = rgb_channels(img, true, false, false).getRGB(0, 0)
    assert(0xff === red_channel(pixel))
    assert(0 === green_channel(pixel))
    assert(0 === blue_channel(pixel))

    pixel = rgb_channels(img, true, true, false).getRGB(0, 0)
    assert(0xff === red_channel(pixel))
    assert(0xff === green_channel(pixel))
    assert(0 === blue_channel(pixel))

    pixel = rgb_channels(img, false, true, true).getRGB(0, 0)
    assert(0 === red_channel(pixel))
    assert(0xff === green_channel(pixel))
    assert(0xff === blue_channel(pixel))
  }

  "Func sepia with sepia factor = 0" should "return the same image" in {
    val img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    img.setRGB(0, 0, 0xffffff)

    val pixel = sepia(img, 0).getRGB(0, 0)
    assert(0xff === red_channel(pixel))
    assert(0xff === green_channel(pixel))
    assert(0xff === blue_channel(pixel))
  }
}
