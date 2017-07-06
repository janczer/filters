import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import java.awt.Color

def histogram(img: BufferedImage): BufferedImage = {
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

  val width = 768
  val factor = 4
  val out = new BufferedImage(256*factor, width, BufferedImage.TYPE_INT_RGB)
  val g2d = out.createGraphics();
  g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON)
  g2d.setBackground(Color.WHITE)
  g2d.fillRect(0, 0, 256*factor, width)

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
    val forRedFrom = width*(1 - hRed(xFrom)/hRed.max.toFloat)
    val forRedTo = width*(1 - hRed(xTo)/hRed.max.toFloat)
    g2d.drawLine(xFrom*factor, forRedFrom.toInt, xTo*factor, forRedTo.toInt)

    g2d.setColor(Color.GREEN)
    val forGreenFrom = width*(1 - hGreen(xFrom)/hGreen.max.toFloat)
    val forGreenTo = width*(1 - hGreen(xTo)/hGreen.max.toFloat)
    g2d.drawLine(xFrom*factor, forGreenFrom.toInt, xTo*factor, forGreenTo.toInt)

    g2d.setColor(Color.BLUE)
    val forBlueFrom = width*(1 - hBlue(xFrom)/hBlue.max.toFloat)
    val forBlueTo = width*(1 - hBlue(xTo)/hBlue.max.toFloat)
    g2d.drawLine(xFrom*factor, forBlueFrom.toInt, xTo*factor, forBlueTo.toInt)
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

  val photoOut = histogram(photoIn)

  ImageIO.write(photoOut, "png", new File("histogram.png"))
}

test()
