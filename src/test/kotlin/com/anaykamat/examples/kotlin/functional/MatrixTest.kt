package com.anaykamat.examples.kotlin.functional

import arrow.core.Either
import arrow.core.Left
import arrow.core.extensions.either.fx.fx
import org.junit.Assert
import org.junit.Test

class MatrixTest {

    @Test
    fun itShouldBuildMatrixOfGivenSize(){
        val matrix: Either<MatrixError, Matrix> = Matrix.build(listOf(1,2,3,4),2,2)
        Assert.assertTrue(matrix is Either.Right<Matrix>)
    }

    @Test
    fun itShouldReturnErrorDuringBuildIfNumberOfValuesDoNotMatchSizeOfMatrix(){
        val matrix: Either<MatrixError, Matrix> = Matrix.build(listOf(1,2,3),2,2)
        Assert.assertEquals(Either.Left(MatrixError.CountOfValuesDoNotMatchMatrixSize), matrix)
    }

    @Test
    fun matrixShouldBeEqualIfTheirSizeAndValuesAreSame(){


        Either.fx<MatrixError>().fx {
            val (firstMatrix) = Matrix.build(listOf(1,2,3,4),2,2)
            val (secondMatrix) = Matrix.build(listOf(1,2,3,4),2,2)
            Assert.assertEquals(firstMatrix, secondMatrix)
        }

    }

    @Test
    fun matrixShouldNotBeEqualIfTheirSizeAndValuesAreNotSame(){
        Either.fx<MatrixError>().fx {
            val (firstMatrix) = Matrix.build(listOf(1,2,3,4),2,2)
            val (secondMatrix) = Matrix.build(listOf(1,2,3,4),1,4)
            Assert.assertNotEquals(firstMatrix, secondMatrix)
        }
    }

    @Test
    fun toListShouldGiveANestedListMatchingTheSizeOfMatrix(){
        Either.fx<MatrixError>().fx {
            val (matrix) = Matrix.build(listOf(1,1,2,2,3,3,4,4),4,2)

            val valuesInNestedList:List<List<Int>> = Matrix.toList(matrix)

            Assert.assertEquals(listOf(listOf(1,1),listOf(2,2),listOf(3,3), listOf(4,4)), valuesInNestedList)
        }
    }

    @Test
    fun transposeShouldGiveTheTransposedMatrix(){
        Either.fx<MatrixError>().fx {
            val (matrix) = Matrix.build(listOf(1,1,2,2,3,3,4,4),4,2)
            val (expectedTransposedMatrix) = Matrix.build(listOf(1,2,3,4,1,2,3,4),2,4)

            val transposedMatrix = Matrix.transpose(matrix)

            Assert.assertEquals(expectedTransposedMatrix, transposedMatrix)
        }
    }

    @Test
    fun multiplyShouldMultiplyTwoMatrices(){
        Either.fx<MatrixError>().fx {
            val (firstMatrix) = Matrix.build(listOf(1,2,3,4),2,2)
            val (secondMatrix) = Matrix.build(listOf(1,2,3,4,5,6),2,3)

            val (multipliedMatrix) = Matrix.multiply(firstMatrix, secondMatrix)

            val (expectedMultipliedMatrix) = Matrix.build(listOf(9,12,15,19,26,33),2,3)

            Assert.assertEquals(expectedMultipliedMatrix, multipliedMatrix)
        }
    }

    @Test
    fun multiplyShouldGiveSizeMismatchErrorIfSizeOfMatricesDontMatch(){
        Either.fx<MatrixError>().fx {
            val (firstMatrix) = Matrix.build(listOf(1,2,3,4),2,2)
            val (secondMatrix) = Matrix.build(listOf(1,2,3,4,5,6),1,6)

            val multipliedMatrix = Matrix.multiply(firstMatrix, secondMatrix)

            Assert.assertEquals(Left(MatrixError.SizeMismatch), multipliedMatrix)
        }
    }

}