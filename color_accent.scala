import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.Color

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

def test() {
  val photoIn = ImageIO.read(new File("test2.jpg"))

  val photoOut = color_accent(photoIn, 200, 50)

  ImageIO.write(photoOut, "jpg", new File("color_accent.jpg"))
}

test()
