package janczer.filters

import java.awt.{Color, RenderingHints}
import java.awt.image.BufferedImage
import scala.util.Sorting
import scala.util.Random

object Filters {
  def main(args: Array[String]) = {
  }

  def mirror(img: BufferedImage, ox: Boolean, oy: Boolean): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight

    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    for (x <- 0 until w)
      for (y <- 0 until h) {
        val wOut = if (ox) w - x - 1 else x
        val hOut = if (oy) h - y - 1 else y
        out.setRGB(x, y, img.getRGB(wOut, hOut) & 0xffffff)
      }

    out
  }

  def gray(img: BufferedImage, typ: String): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight

    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    for (x <- 0 until w)
      for (y <- 0 until h) {
        val pixel = img.getRGB(x, y)

        val red = (pixel & 0xff0000) / 65536
        val green = (pixel & 0xff00) / 256
        val blue = pixel & 0xff

        val mono = typ match {
          case "avarage" => (red + green + blue)/3
          case "lightness" => (Set(red, green, blue).max  + Set(red, green, blue).min)/2
          case "luminosity" => (0.21*red + 0.72*green + 0.07*blue).toInt
        }

        val gray = (mono * 65536) + (mono * 256) + mono

        out.setRGB(x, y, gray.toInt & 0xffffff)
      }

    out
  }

  def color_accent(img: BufferedImage, hue: Int, range: Int): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight

    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    for (x <- 0 until w)
      for (y <- 0 until h) {
        val pixel = img.getRGB(x, y)

        val red = (pixel & 0xff0000) / 65536
        val green = (pixel & 0xff00) / 256
        val blue = pixel & 0xff

        val (h, sat, l) = rgb2hsv(red, green, blue)

        val h1 = (hue - range/2 + 360) % 360
        val h2 = (hue + range/2 + 360) % 360

        var c: Int = 0

        if (h1 <= h2 && (h <= h2 && h >= h1)) {
          c = (red * 65536) + (green * 256) + blue
        } else if (h1 > h2 && (h <= h2 || h >= h1)) {
          c = (red * 65536) + (green * 256) + blue
        } else {
          val mono = (0.21*red + 0.72*green + 0.07*blue).toInt
          c = (mono * 65536) + (mono * 256) + mono
        }


        out.setRGB(x, y, c.toInt & 0xffffff)
      }

    out
  }

  def histogram(img: BufferedImage, grid: Boolean): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight


    var hRed = new Array[Int](256)
    var hGreen = new Array[Int](256)
    var hBlue = new Array[Int](256)

    for (x <- 0 until w)
      for (y <- 0 until h) {
        val pixel = img.getRGB(x, y)

        val red = (pixel & 0xff0000) / 65536
        val green = (pixel & 0xff00) / 256
        val blue = pixel & 0xff

        hRed(red) += 1
        hGreen(green) += 1
        hBlue(blue) += 1
      }

    val maxx = Set(hRed.max, hGreen.max, hBlue.max).max
    val minn = Set(hRed.min, hGreen.min, hGreen.min).min

    val height = 768
    val factor = 4
    val width = 256*factor

    val out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val g2d = out.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON)
    g2d.setBackground(Color.WHITE)
    g2d.fillRect(0, 0, 256*factor, width)

    //add grid
    if (grid) {
      g2d.setColor(Color.GRAY)
      for (i <- 1 to 10) {
        g2d.drawLine(width*i/10, 0, width*i/10, height)
        g2d.drawLine(0, height*i/10, width, height*i/10)
      }
    }

    for (x <- 1 to 255) {
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
    }

    out
  }

  def inverse(img: BufferedImage): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight

    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    for (x <- 0 until w)
      for (y <- 0 until h) {
        val pixel = img.getRGB(x, y)

        val red = (pixel & 0xff0000) / 65536
        val green = (pixel & 0xff00) / 256
        val blue = pixel & 0xff


        val inverse = ((255 - red) * 65536) + ((255 - green) * 256) + 255 - blue

        out.setRGB(x, y, inverse.toInt & 0xffffff)
      }

    out
  }

  def rgb_channels(img: BufferedImage, r: Boolean, g: Boolean, b: Boolean): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight

    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    for (x <- 0 until w)
      for (y <- 0 until h) {
        val pixel = img.getRGB(x, y)

        var red = (pixel & 0xff0000) / 65536
        var green = (pixel & 0xff00) / 256
        var blue = pixel & 0xff

        if (!r)
          red = 0
        if (!g)
          green = 0
        if (!b)
          blue = 0

        val inverse = (red * 65536) + (green * 256) + blue

        out.setRGB(x, y, inverse.toInt & 0xffffff)
      }

    out
  }

  def sepia(img: BufferedImage, sp: Int): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight

    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    for (x <- 0 until w)
      for (y <- 0 until h) {
        val pixel = img.getRGB(x, y)

        val red = (pixel & 0xff0000) / 65536 + 2 * sp
        val green = (pixel & 0xff00) / 256 + sp
        val blue = pixel & 0xff

        val redOut = if (red > 255) 255 else red
        val greenOut = if (green > 255) 255 else green

        val sepia = (redOut * 65536) + (greenOut * 256) + blue

        out.setRGB(x, y, sepia.toInt & 0xffffff)
      }

    out
  }

  def median(img: BufferedImage): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight

    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    for (x <- 1 until w - 1)
      for (y <- 1 until h - 1) {

        val matrix = new Array[Int](9)

        var i = 0
        for (xn <- x until x + 2)
          for (yn <- y until y + 2) {
            matrix(i) = img.getRGB(xn, yn) & 0xff
            i += 1
          }
          
        Sorting.quickSort(matrix)

        val median = (matrix(5) * 65536) + (matrix(5) * 256) + matrix(5)
            
        out.setRGB(x, y, median.toInt & 0xffffff)
      }

    out
  }

  def noise(img: BufferedImage): BufferedImage = {
    val w = img.getWidth
    val h = img.getHeight

    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    for (x <- 0 until w)
      for (y <- 0 until h) {
        val r = Random
        val n = r.nextFloat
        if (n < 0.01) {
          out.setRGB(x, y, 0xffffff)
        } else {
          out.setRGB(x, y, img.getRGB(x, y))
        }
      }


    out
  }

  def rgb2hsv(red: Int, green: Int, blue:Int) = {
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
        case `g` => (b - r) / d + 2;
        case `b` => (r - g) / d + 4;
      }
      h / 6f
    }

    (h * 360f, s, v)
  }
}

