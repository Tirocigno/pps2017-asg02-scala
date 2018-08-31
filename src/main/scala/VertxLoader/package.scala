package object VertxLoader {

  import io.vertx.core._

  /**
    * This implicit function handles allows function defined by scala syntax to be passed as handler for
    * a asynchronous computation.
    * @param f the function to use as handler.
    * @tparam A the input data type of the handler.
    * @return an Handler object.
    */
  implicit def functionToHandler[A](f: A => Unit): Handler[A] = new Handler[A] {
    override def handle(event: A): Unit = {
      f(event)

    }
  }

}
