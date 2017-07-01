import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

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

def test() {
  val photoIn = ImageIO.read(new File("test.jpg"))

  val photoOut = mirror(photoIn, true, true)

  ImageIO.write(photoOut, "jpg", new File("mirror.jpg"))
}

test()
