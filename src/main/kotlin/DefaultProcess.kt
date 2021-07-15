import core.Process

class DefaultProcess(override val pid: String,override val priority: Int) : Process {

//    override val pid: String
//        get() = UUID.randomUUID().toString();

    @Volatile var isStopped:Boolean = false;

    override fun run() {
       while(!isStopped){
           Thread.sleep(3000)
           break
       }
    }

    override fun kill() {
        isStopped = true
    }

}