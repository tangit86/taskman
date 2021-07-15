import core.*

fun main(args: Array<String>) {
    println("Hello World!")

    val p:MutableList<Process> = ArrayList()

    for(i:Int in 1..10){
        p.add(DefaultProcess(i.toString(), i * 200))
    }

    //val c = BClientTaskManager(4);
    val c = AClientTaskManager(4);

    for(pro:Process in p){
        Thread.sleep(500)
        c.add(pro)
    }

    Thread.sleep(8000)

    for(pro:Process in p.take(10)){
        Thread.sleep(500)
        c.kill(pro.pid)
    }
}