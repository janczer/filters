import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

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

      val redOut = if (red + 2 * sp > 255) 255 else red + 2 * sp
      val greenOut = if (green + sp > 255) 255 else green + sp

      val sepia = (redOut * 65536) + (greenOut * 256) + blue

      out.setRGB(x, y, sepia.toInt & 0xffffff)
    }

  out
}

def test() {
  val photoIn = ImageIO.read(new File("gray.jpg"))

  val photoOut = sepia(photoIn, 20)

  ImageIO.write(photoOut, "jpg", new File("sepia.jpg"))
}

test()
