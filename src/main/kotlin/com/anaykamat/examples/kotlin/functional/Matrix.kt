package com.anaykamat.examples.kotlin.functional

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.andThen

sealed class MatrixError{
    object CountOfValuesDoNotMatchMatrixSize:MatrixError()
    object SizeMismatch:MatrixError()
}

class Matrix private constructor(val values:List<Int>, val rows:Int, val columns:Int){

    companion object {
        fun build(values:List<Int>, rows:Int, columns:Int): Either<MatrixError, Matrix> =
                if (values.count() == rows * columns)
                    Right(Matrix(values, rows, columns))
                else
                    Left(MatrixError.CountOfValuesDoNotMatchMatrixSize)

        fun toList(matrix:Matrix):List<List<Int>> = matrix.values.chunked(matrix.columns)

        fun transpose(matrix:Matrix):Matrix
            = toList(matrix)
                .fold(
                    (0 until matrix.columns).map { emptyList<Int>() }
                ){ resultList, rowList ->
                    resultList.zip(rowList,{list, value -> list + listOf(value)})
                }
                .flatten()
                .let {
                    Matrix(it, matrix.columns, matrix.rows)
                }

        fun multiply(firstMatrix: Matrix, secondMatrix: Matrix): Either<MatrixError, Matrix> =
            if (firstMatrix.columns != secondMatrix.rows)
                Left(MatrixError.SizeMismatch)
            else
                validMatrixMultiplication(firstMatrix, secondMatrix)

        private fun validMatrixMultiplication(
            firstMatrix: Matrix,
            secondMatrix: Matrix
        ): Either<MatrixError, Matrix> {
            val firstMatrixList = toList(firstMatrix)
            val secondMatrixList = (Companion::transpose).andThen(Companion::toList)(secondMatrix)

            return firstMatrixList.map { firstMatrixRow ->
                secondMatrixList.map { secondMatrixRow ->
                    firstMatrixRow.zip(secondMatrixRow) { firstMatrixElement, secondMatrixElement ->
                        firstMatrixElement * secondMatrixElement
                    }.sum()
                }
            }.flatten().let { build(it, firstMatrix.rows, secondMatrix.columns) }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix

        if (values != other.values) return false
        if (rows != other.rows) return false
        if (columns != other.columns) return false

        return true
    }


}