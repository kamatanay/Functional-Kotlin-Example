package com.anaykamat.examples.kotlin.functional

import arrow.Kind
import arrow.effects.ForIO
import arrow.effects.IO
import arrow.effects.extensions.io.applicative.applicative
import arrow.effects.extensions.io.fx.fx
import arrow.effects.fix
import arrow.typeclasses.MonadContinuation
import kotlin.random.Random

val consoleIO:ConsoleIO<ForIO> = object: ConsoleIO<ForIO>{
    override fun <A> fx(f: suspend MonadContinuation<ForIO, *>.() -> A): Kind<ForIO, A> {
        return IO.fx().fx(f)
    }

    override fun print(message: String): Kind<ForIO, Unit> = IO { println(message) }

    override fun read(): Kind<ForIO, String> = IO { readLine() ?: "" }

    override fun randomInt(): Kind<ForIO, Int>  = IO { Random.nextInt(0,100) }


}

fun main(args:Array<String>){

    App(consoleIO, IO.applicative()).execute().fix().unsafeRunSync()

}