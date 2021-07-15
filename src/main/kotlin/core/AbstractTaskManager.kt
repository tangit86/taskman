package core

import java.lang.Exception
import java.util.*
import java.util.concurrent.*

abstract class AbstractTaskManager(private val maxProcess: Int) : TaskManager {

    private val executor: ExecutorService =
        ThreadPoolExecutor(
            maxProcess,
            maxProcess,
            5000L,
            TimeUnit.MILLISECONDS,
            LinkedBlockingQueue(1),
            ThreadPoolExecutor.AbortPolicy()
        )

    private var  processes: MutableList<Process> = LinkedList()

    private val mutex: Any = Any();

    override fun add(process: Process) {

        synchronized(mutex) {
            log("Adding ${process.pid}")
            if (processes.find { it.pid == process.pid } != null) {
                log("Process ${process.pid} has already been added")
                return
            }

            if (shouldReject()) {
                handleRejection(process)
                if (shouldReject()) return
            }

            try {
                processes.add(process)
                runProcess(process)
                log("Process ${process.pid} added")
            } catch (e: Exception) {
                //not focusing here now
                this.doKill(process)
            }
        }
    }

    protected fun shouldReject(): Boolean {
        return processes.size == maxProcess
    }

    abstract fun handleRejection(process:Process)

    private fun runProcess(process: Process) {
        executor.execute(Runnable {
            try {
                process.run()
            } finally {
                this.kill(process.pid)
            }
        })
    }

    override fun list(sortBy: SortBy?): List<Process> {
        with(processes.toList()) {
            return when (sortBy) {
                SortBy.PRIORITY -> this.sortedBy { it.priority }
                SortBy.PID -> this.sortedBy { it.pid }
                else -> this.toList()
            }
        }
    }

    override fun kill(processId: String) {
        list().filter { it.pid == processId }.forEach {
            synchronized(mutex) {
                doKill(it)
            }
        }
    }

    override fun killGroup(priority: Int) {
        list().filter { it.priority == priority }.forEach {
            synchronized(mutex) {
                doKill(it)
            }
        }
    }

    override fun killAll() {
        list().forEach {
            synchronized(mutex) {
                doKill(it)
            }
        }
    }

    protected fun doKill(process: Process) {
        log("Attempting to kill ${process.pid}")
        if (processes.contains(process)) {
            processes.remove(process)
            process.kill()
            log("Killed ${process.pid}")
        }
    }

    protected fun log(msg: String) {
        System.out.printf(Thread.currentThread().name + " " + msg+"\n")
    }
}

public enum class SortBy {
    CREATED,
    PRIORITY,
    PID
}