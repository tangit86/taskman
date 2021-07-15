import core.AbstractTaskManager
import core.Process

class AClientTaskManager(private val maxProcess:Int) : AbstractTaskManager(maxProcess) {

    override fun handleRejection(process: Process) {
        //nothing here, default behavior covered in parent
    }
}