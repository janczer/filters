import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

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
        case "lightness" => (max(red, max(green, blue)) + min(red, min(green, blue)))/2
        case "luminosity" => (0.21*red + 0.72*green + 0.07*blue).toInt
      }

      val gray = (mono * 65536) + (mono * 256) + mono

      out.setRGB(x, y, gray.toInt & 0xffffff)
    }

  out
}

def max(x: Int, y: Int): Int = {
  if (x > y) x else y
}

def min(x: Int, y: Int): Int = {
  if (x < y) x else y
}

def test() {
  val photoIn = ImageIO.read(new File("test.jpg"))

  val photoOut = gray(photoIn, "lightness")

  ImageIO.write(photoOut, "jpg", new File("gray.jpg"))
}

test()
