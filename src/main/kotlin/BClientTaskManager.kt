import core.AbstractTaskManager
import core.Process

class BClientTaskManager(private val maxProcess:Int) : AbstractTaskManager(maxProcess) {

    override fun handleRejection(process: Process) {
        val oldestProcess = this.list().first()
        this.doKill(oldestProcess)
        log("Killed ${oldestProcess.pid}  so to put ${process.pid}")
    }
}