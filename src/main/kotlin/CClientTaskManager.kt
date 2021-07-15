import core.AbstractTaskManager
import core.Process

class CClientTaskManager(private val maxProcess:Int) : AbstractTaskManager(maxProcess) {

    override fun handleRejection(process: Process) {
        val processes = this.list()
        var curMin: Process = processes.first()

        for(existingProcess: Process in processes){
            if(process.priority <= existingProcess.priority){
                return
            }
            if(existingProcess.priority<curMin.priority){
                curMin = existingProcess
            }
        }
        this.doKill(curMin)
        log("Killed ${curMin.pid}  so to put ${process.pid}")
    }
}