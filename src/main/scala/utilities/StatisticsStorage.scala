package utilities

object StatisticsStorage {

  private var scannedFileMap: Map[String, Long] = scala.collection.immutable.HashMap()

  /**
    * Add a new file to the map.
    *
    * @param filePath the path of the scanned file.
    * @param value    the value of the file calculated.
    */
  def addFile(filePath: String, value: Long): Unit =
    scannedFileMap = scannedFileMap + (filePath -> value)


  /**
    * Retrieve the number of file which match at least once.
    *
    * @return an Integer containing the value.
    */
  def getNumberOfFileMatching(): Int = scannedFileMap.filter(_._2 > 0).keys size

  /**
    * Retrieve the number of total matches in the storage.
    *
    * @return a Long containing the value.
    */
  def getTotalMatching(): Long = scannedFileMap.filter(_._2 > 0).values sum

  /**
    * Retrieve the mean number of matching.
    *
    * @return an integer with the percentage value
    */
  def meanNumberOfMatching(): Int = (getNumberOfFileMatching() / scannedFileMap.keys.size) * 100

}
