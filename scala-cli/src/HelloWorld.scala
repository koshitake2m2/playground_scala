def add(x: Int, y: Int): Int = x + y

@main
def hello(): Unit =
    val result = add(3, 4)
    println(s"result: $result")
    
