package janczer.filters

import java.awt.{Color, RenderingHints}
import java.awt.image.BufferedImage
import scala.util.Sorting
import scala.util.Random

object Filters {
  def main(args: Array[String]) = {
  }

  def mirror(img: BufferedImage, ox: Boolean, oy: Boolean): BufferedImage =
    buffered_image_with_pixels(img, (x: Int, y: Int) => {
      img.getRGB(
        if (ox) img.getWidth - x - 1 else x,
        if (oy) img.getHeight - y - 1 else y
      )
    })

  def gray(img: BufferedImage, typ: String): BufferedImage =
    buffered_image_with_pixels(img, (x: Int, y: Int) => color2gray(img.getRGB(x, y), typ))

  def color_accent(img: BufferedImage, hue: Int, range: Int): BufferedImage =
    buffered_image_with_pixels(img, (x: Int, y: Int) => {
      val pixel = img.getRGB(x, y)

      val (h, _, _) = rgb2hsv_pixel(pixel)

      val (h1, h2) = {
        (360 - range/2, range/2)
      }

      val h_normal = (h - hue + 360) % 360

      if (h_normal >= h2 && h_normal <= h1) {
        color2gray(pixel, "luminosity")
      } else {
        pixel
      }
    })

  def buffered_image_with_pixels(img: BufferedImage, fx : (Int, Int) => Int) : BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight
    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    (0 until w).foreach( x => {
      (0 until h).foreach( y => {
        out.setRGB(x, y, fx(x, y))
      })
    })

    out
  }

  def rgb2hsv_pixel(pixel: Int): (Float, Float, Float) = rgb2hsv(red_channel(pixel), green_channel(pixel), blue_channel(pixel))
  def red_channel(pixel: Int): Int = (pixel & 0xff0000) / 0x10000
  def green_channel(pixel: Int): Int = (pixel & 0xff00) / 0x100
  def blue_channel(pixel: Int): Int = pixel & 0xff
  def all_channels(red: Int, green: Int, blue: Int) : Int =
    ((normalize_pixel(red) * 0x10000) +
      (normalize_pixel(green) * 0x100) +
      normalize_pixel(blue)).toInt & 0xffffff
  def normalize_pixel(c: Int) : Int = c match {
      case x if x > 255 => 255
      case x if x < 0 => 0
      case _ => c
  }

  def histogram(img: BufferedImage, grid: Boolean): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight

    var hRed = new Array[Int](256)
    var hGreen = new Array[Int](256)
    var hBlue = new Array[Int](256)

    (0 until w).foreach( x => {
      (0 until h).foreach( y => {
        hRed(red_channel(img.getRGB(x, y))) += 1
        hGreen(green_channel(img.getRGB(x, y))) += 1
        hBlue(blue_channel(img.getRGB(x, y))) += 1
      })
    })

    val maxx = Set(hRed.max, hGreen.max, hBlue.max).max
    val minn = Set(hRed.min, hGreen.min, hGreen.min).min

    val height = 768
    val factor = 4
    val width = 256*factor

    val out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val g2d = out.createGraphics()
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON)
    g2d.setBackground(Color.WHITE)
    g2d.fillRect(0, 0, 256*factor, width)

    //add grid
    if (grid) {
      g2d.setColor(Color.GRAY)
      (0 to 10).foreach( i => {
        g2d.drawLine(width*i/10, 0, width*i/10, height)
        g2d.drawLine(0, height*i/10, width, height*i/10)
      })
    }

    (1 to 255).foreach( x => {
      val xFrom = x - 1
      val xTo = x
      //g2d.setColor(Color.RED)
      //val forRed = width*(1 - hRed(x)/hRed.max.toFloat)
      //g2d.drawLine(x*factor, width, x*factor, forRed.toInt)

      //g2d.setColor(Color.GREEN)
      //val forGreen = width*(1 - hGreen(x)/hGreen.max.toFloat)
      //g2d.drawLine(x*factor, width, x*factor, forGreen.toInt)

      //g2d.setColor(Color.BLUE)
      //val forBlue = width*(1 - hBlue(x)/hBlue.max.toFloat)
      //g2d.drawLine(x*factor, width, x*factor, forBlue.toInt)

      g2d.setColor(Color.RED)
      val forRedFrom = height*(1 - hRed(xFrom)/hRed.max.toFloat)
      val forRedTo = height*(1 - hRed(xTo)/hRed.max.toFloat)
      g2d.drawLine(xFrom*factor, forRedFrom.toInt, xTo*factor, forRedTo.toInt)

      g2d.setColor(Color.GREEN)
      val forGreenFrom = height*(1 - hGreen(xFrom)/hGreen.max.toFloat)
      val forGreenTo = height*(1 - hGreen(xTo)/hGreen.max.toFloat)
      g2d.drawLine(xFrom*factor, forGreenFrom.toInt, xTo*factor, forGreenTo.toInt)

      g2d.setColor(Color.BLACK)
      val forBlueFrom = height*(1 - hBlue(xFrom)/hBlue.max.toFloat)
      val forBlueTo = height*(1 - hBlue(xTo)/hBlue.max.toFloat)
      g2d.drawLine(xFrom*factor, forBlueFrom.toInt, xTo*factor, forBlueTo.toInt)
    })

    out
  }

  def inverse(img: BufferedImage): BufferedImage =
    buffered_image_with_pixels(img, (x: Int, y: Int) =>
      all_channels(
        255 - red_channel(img.getRGB(x, y)),
        255 - green_channel(img.getRGB(x, y)),
        255 - blue_channel(img.getRGB(x, y))
      )
    )

  def rgb_channels(img: BufferedImage, r: Boolean, g: Boolean, b: Boolean): BufferedImage =
    buffered_image_with_pixels(img, (x: Int, y: Int) =>
      all_channels(
        if (r) red_channel(img.getRGB(x, y)) else 0,
        if (g) green_channel(img.getRGB(x, y)) else 0,
        if (b) blue_channel(img.getRGB(x, y)) else 0
      )
    )

  def sepia(img: BufferedImage, sp: Int): BufferedImage =
    buffered_image_with_pixels(img, (x: Int, y: Int) => all_channels(
      red_channel(img.getRGB(x, y)) + 2 * sp,
      green_channel(img.getRGB(x, y)) + sp,
      blue_channel(img.getRGB(x, y))
    ))

  def median(img: BufferedImage): BufferedImage =
    buffered_image_with_pixels(img, (x: Int, y: Int) => {
      val matrix = new Array[Int](9)

      var i = 0
      (x until x + 2).foreach( xn => {
        (y until y + 2).foreach( yn => {
          matrix(i) = img.getRGB(xn, yn) & 0xff
          i += 1
        })
      })

      Sorting.quickSort(matrix)

      all_channels(matrix(5), matrix(5), matrix(5))
    })

  def noise(img: BufferedImage): BufferedImage =
    buffered_image_with_pixels(img, (x: Int, y: Int) => {
      val r = Random
      val n = r.nextFloat
      if (n < 0.01) {
        0xffffff
      } else {
        img.getRGB(x, y)
      }
    })

  def sort_zig_zag(img: BufferedImage, gray: Boolean): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight

    var a = new Array[Int](w*h)
    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)


    var i = 0
    (0 until w).foreach( x => {
      (0 until h).foreach(y => {
        a(i) = img.getRGB(x, y)
        i += 1
      })
    })

    a = a.map((x) => if (gray) color2gray(x, "average") else x)

    Sorting.quickSort(a)

    i = 1
    var j = 1

    (0 until w*h).foreach( e => {
      out.setRGB(i-1, j-1, a(e))
      if ((i + j) % 2 == 0) {
        if (j < h)
          j += 1
        else
          i += 2

        if (i > 1)
          i -= 1
      } else {
        
        if (i < w)
          i += 1
        else
          j += 2

        if (j > 1)
          j -= 1
      }
    })
    
    out
  }

  def color2gray(rgb: Int, typ: String):Int = {
    val red = red_channel(rgb)
    val green = green_channel(rgb)
    val blue = blue_channel(rgb)

    val mono = typ match {
      case "average" => (red + green + blue)/3
      case "lightness" => (Set(red, green, blue).max  + Set(red, green, blue).min)/2
      case "luminosity" => (0.21*red + 0.72*green + 0.07*blue).toInt
    }

    all_channels(mono, mono, mono)
  }

  def rgb2hsv(red: Int, green: Int, blue:Int): (Float, Float, Float) = {
    val r = red / 255f
    val g = green / 255f
    val b = blue / 255f

    val max = Set(r, g, b).max
    val min = Set(r, g, b).min

    val d = max - min
    val s = if (max == 0) 0 else d / max
    val v = max

    val h = if (max == min) {
      0f
    } else {
      val h = max match {
        case `r` => (g - b) / d + (if (g < b) 6 else 0)
        case `g` => (b - r) / d + 2
        case `b` => (r - g) / d + 4
      }
      h / 6f
    }

    (h * 360f, s, v)
  }
}

