package com.anaykamat.examples.kotlin.functional

import arrow.Kind
import arrow.core.ForId
import arrow.core.Id
import arrow.core.extensions.id.applicative.applicative
import arrow.core.extensions.id.fx.fx
import arrow.typeclasses.MonadContinuation
import org.junit.Assert
import org.junit.Test

class AppTest {

    val output:MutableList<String> = mutableListOf()
    val inputs:MutableList<String> = mutableListOf("1","2","1","2")
    val randomIntegerList = mutableListOf(4,5)

    val testConsoleIO = object:ConsoleIO<ForId>{
        override fun <A> fx(f: suspend MonadContinuation<ForId, *>.() -> A): Kind<ForId, A> {
            return Id.fx<Unit>().fx(f)
        }

        override fun print(message: String): Kind<ForId, Unit> {
            output.add(message)
            return Id.just(Unit)
        }

        override fun read(): Kind<ForId, String> {
            val input = inputs.first()
            inputs.removeAt(0)
            return Id.just(input)
        }

        override fun randomInt(): Kind<ForId, Int> = Id.just(randomIntegerList.first().also { randomIntegerList.removeAt(0) })

    }

    @Test
    fun appShouldTakeMatrixInputFromUserAndMultiplyItWithRandomMatrix(){

        val app:App<ForId> = App(testConsoleIO, Id.applicative())
        app.execute()
        Assert.assertEquals(listOf("Number Of Rows:", "Number Of Columns:", "Enter Matrix Values:","Random matrix contains: 4,5","Multipled Result: 14"), output)

    }

}