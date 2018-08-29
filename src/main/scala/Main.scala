
object Test extends App {
  import VertxLoader._
  val noExistingFile = VertxFile("/bin/bash.com")
  println(noExistingFile)
  val existingFile = VertxFile("src/main/resources/cat.txt")
  println(existingFile)
  val existingDirectory = VertxFile("/felides")
  println(existingDirectory)
}