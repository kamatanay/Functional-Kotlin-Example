package com.anaykamat.examples.kotlin.functional

import arrow.Kind
import arrow.core.Either
import arrow.core.extensions.either.fx.fx
import arrow.core.fix
import arrow.typeclasses.Applicative

class App<F>(val consoleIO:ConsoleIO<F>, val applicative: Applicative<F>):ConsoleIO<F> by consoleIO {

    private fun <A> take(times:Int, applicative: Applicative<F>, f:() -> Kind<F,A>):Kind<F,List<A>>{
        return (0 until times).map{ f()}
            .foldRight(applicative.just(emptyList())) { value, accumulator  ->
                applicative.run { accumulator.map2(value, { (list, value) -> listOf(value) + list }) }
            }
    }

    fun execute(): Kind<F, Unit>{

        return fx {
            print("Number Of Rows:").bind()
            val numberOfRows = read().bind().toInt()
            print("Number Of Columns:").bind()
            val numberOfColumns = read().bind().toInt()
            print("Enter Matrix Values:").bind()

            val matrixValues = take(numberOfRows*numberOfColumns, applicative){ read() }.bind().map(String::toInt)
            val randomValues = take(numberOfColumns, applicative){ randomInt() }.bind()

            Either.fx<MatrixError>().fx {
                val (matrix) = Matrix.build(matrixValues, numberOfRows, numberOfColumns)
                val (randomMatrix) = Matrix.build(randomValues, numberOfColumns, 1)
                val (multiplicationResult) = Matrix.multiply(matrix, randomMatrix)
                Pair(randomMatrix, multiplicationResult)
            }
            .fix()
            .fold({_ -> print("There was an error").bind()}){ (randomMatrix, multiplicationMatrix) ->
                print("Random matrix contains: ${randomMatrix.values.joinToString(",")}").bind()
                print("Multipled Result: ${multiplicationMatrix.values.joinToString(",")}").bind()
            }
        }

    }

}