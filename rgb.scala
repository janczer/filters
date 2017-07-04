import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

def rgb(img: BufferedImage, r: Boolean, g: Boolean, b: Boolean): BufferedImage = {
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

def test() {
  val photoIn = ImageIO.read(new File("test.jpg"))

  val photoRed = rgb(photoIn, true, false, false)
  ImageIO.write(photoRed, "jpg", new File("red.jpg"))

  val photoGreen = rgb(photoIn, false, true, false)
  ImageIO.write(photoGreen, "jpg", new File("green.jpg"))


  val photoBlue = rgb(photoIn, false, false, true)
  ImageIO.write(photoBlue, "jpg", new File("blue.jpg"))
}

test()
