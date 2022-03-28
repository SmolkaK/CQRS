package core.etl.initializer

import core.rest.JokeRestParams
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * Happy scenario tests
 */

class InitialLoadParamsBuilderTest
  extends AnyFlatSpec
    with Matchers {

  "A InitialLoadParamsBuilder" should "build 1 JokeRestParams, for numberOfJokes = 1" in {

    val result = InitialLoadParamsBuilder.initialLoadRestRequestsParams(Nil, None, numberOfJokes = 1, 5)
    val expectedResult = Seq(JokeRestParams(Nil, None, Some(Range.inclusive(1, 1))))

    assert(result === expectedResult)
  }

  "A InitialLoadParamsBuilder" should "build 2 correct JokeRestParams, for numberOfJokes = 2 and batchSize = 1" in {
    val result = InitialLoadParamsBuilder.initialLoadRestRequestsParams(Nil, None, numberOfJokes = 2, 1).toVector
    val expectedResult = Seq(JokeRestParams(Nil, None, Some(Range.inclusive(1, 1))), JokeRestParams(Nil, None, Some(Range.inclusive(2, 2))))

    assert(result === expectedResult)
  }

  "A InitialLoadParamsBuilder" should "build 0 correct JokeRestParams, for numberOfJokes = 0" in {
    val result = InitialLoadParamsBuilder.initialLoadRestRequestsParams(Nil, None, numberOfJokes = 0, 1).toVector
    val expectedResult = Nil

    assert(result === expectedResult)
  }
}