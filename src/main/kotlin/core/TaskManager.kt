package core

interface TaskManager {

    fun add(process: Process)

    fun list(sortBy:SortBy?=null) : List<Process>

    fun kill(processId: String)

    fun killGroup(priority : Int)

    fun killAll()
}