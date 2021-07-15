package core

import java.util.*


interface Process : Runnable{
    val pid : String;
    val priority : Int
    override  fun run() : Unit;
    fun kill() : Unit
}