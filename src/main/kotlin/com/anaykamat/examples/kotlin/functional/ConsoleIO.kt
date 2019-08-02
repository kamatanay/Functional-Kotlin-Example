package com.anaykamat.examples.kotlin.functional

import arrow.Kind
import arrow.typeclasses.MonadContinuation

interface ConsoleIO<F> {

    fun print(message:String): Kind<F, Unit>
    fun read():Kind<F, String>
    fun randomInt():Kind<F,Int>

    fun <A> fx(f: suspend MonadContinuation<F, *>.() -> A): Kind<F, A>

}