import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

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

def test() {
  val photoIn = ImageIO.read(new File("test.jpg"))

  val photoOut = inverse(photoIn)

  ImageIO.write(photoOut, "jpg", new File("inverse.jpg"))
}

test()
