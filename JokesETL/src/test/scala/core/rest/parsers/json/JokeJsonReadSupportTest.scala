package core.rest.parsers.json

import core.db.entities.Joke
import core.db.entities.enums.{JokeCategory, JokeType}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.{JsResultException, Json}

class JokeJsonReadSupportTest
  extends AnyFlatSpec
    with JokeJsonReadSupport
    with Matchers{

  "A JokeJsonReadSupport" should "correctly return 1 Joke, for 1 single joke given in json" in {
    val givenJson =
      """
        |{
        |    "error": false,
        |    "amount": 1,
        |    "jokes": [
        |        {
        |            "category": "Programming",
        |            "type": "single",
        |            "joke": "I've got a really good UDP joke to tell you but I don’t know if you'll get it.",
        |            "flags": {
        |                "nsfw": false,
        |                "religious": false,
        |                "political": false,
        |                "racist": false,
        |                "sexist": false,
        |                "explicit": false
        |            },
        |            "id": 0,
        |            "safe": true,
        |            "lang": "en"
        |        }
        |    ]
        |}
        |""".stripMargin

    val expectedResult = Seq(
      Joke(
        0,
        JokeCategory.withName("Programming"),
        JokeType.withName("single"),
        "I've got a really good UDP joke to tell you but I don’t know if you'll get it.",
        true,
        "en")
    )

    assert(Json.parse(givenJson).as[Seq[Joke]] == expectedResult)
  }

  "A JokeJsonReadSupport" should "correctly return 1 Joke, for 1 twopart joke given in json" in {
    val givenJson =
      """
        |{
        |    "error": false,
        |    "category": "Programming",
        |    "type": "twopart",
        |    "setup": "Why do programmers wear glasses?",
        |    "delivery": "Because they need to C#",
        |    "flags": {
        |        "nsfw": false,
        |        "religious": false,
        |        "political": false,
        |        "racist": false,
        |        "sexist": false,
        |        "explicit": false
        |    },
        |    "id": 50,
        |    "safe": true,
        |    "lang": "en"
        |}
        |""".stripMargin

    val expectedResult = Seq(
      Joke(
        50,
        JokeCategory.withName("Programming"),
        JokeType.withName("twopart"),
        "Why do programmers wear glasses? >>>Because they need to C#",
        true,
        "en")
    )

    assert(Json.parse(givenJson).as[Seq[Joke]] == expectedResult)
  }

  "A JokeJsonReadSupport" should "correctly return 3 Joke, for 3 jokes with mixed types given in json" in {
    val givenJson =
      """
        |{
        |    "error": false,
        |    "amount": 3,
        |    "jokes": [
        |        {
        |            "category": "Programming",
        |            "type": "twopart",
        |            "setup": "What's the object-oriented way to become wealthy?",
        |            "delivery": "Inheritance.",
        |            "flags": {
        |                "nsfw": false,
        |                "religious": false,
        |                "political": false,
        |                "racist": false,
        |                "sexist": false,
        |                "explicit": false
        |            },
        |            "id": 46,
        |            "safe": true,
        |            "lang": "en"
        |        },
        |        {
        |            "category": "Programming",
        |            "type": "single",
        |            "joke": "UDP is better in the COVID era since it avoids unnecessary handshakes.",
        |            "flags": {
        |                "nsfw": false,
        |                "religious": false,
        |                "political": false,
        |                "racist": false,
        |                "sexist": false,
        |                "explicit": false
        |            },
        |            "id": 259,
        |            "safe": true,
        |            "lang": "en"
        |        },
        |        {
        |            "category": "Programming",
        |            "type": "twopart",
        |            "setup": "How do you generate a random string?",
        |            "delivery": "Put a Windows user in front of Vim and tell them to exit.",
        |            "flags": {
        |                "nsfw": false,
        |                "religious": false,
        |                "political": false,
        |                "racist": false,
        |                "sexist": false,
        |                "explicit": false
        |            },
        |            "id": 127,
        |            "safe": true,
        |            "lang": "en"
        |        }
        |    ]
        |}
        |""".stripMargin

    val expectedResult = Seq(
      Joke(
        46,
        JokeCategory.withName("Programming"),
        JokeType.withName("twopart"),
        "What's the object-oriented way to become wealthy? >>>Inheritance.",
        true,
        "en"),
      Joke(
        259,
        JokeCategory.withName("Programming"),
        JokeType.withName("single"),
        "UDP is better in the COVID era since it avoids unnecessary handshakes.",
        true,
        "en"),
      Joke(
        127,
        JokeCategory.withName("Programming"),
        JokeType.withName("twopart"),
        "How do you generate a random string? >>>Put a Windows user in front of Vim and tell them to exit.",
        true,
        "en"),
    )

    assert(Json.parse(givenJson).as[Seq[Joke]] === expectedResult)
  }

  "A JokeJsonReadSupport" should "correctly return 1 Joke, for 1 joke with with missing flags (nonessential values) given in json" in {
    val givenJson =
      """
        |{
        |    "error": false,
        |    "amount": 1,
        |    "jokes": [
        |        {
        |            "category": "Programming",
        |            "type": "single",
        |            "joke": "I've got a really good UDP joke to tell you but I don’t know if you'll get it.",
        |            "id": 0,
        |            "safe": true,
        |            "lang": "en"
        |        }
        |    ]
        |}
        |""".stripMargin

    val expectedResult = Seq(
      Joke(
        0,
        JokeCategory.withName("Programming"),
        JokeType.withName("single"),
        "I've got a really good UDP joke to tell you but I don’t know if you'll get it.",
        true,
        "en")
    )

    assert(Json.parse(givenJson).as[Seq[Joke]] == expectedResult)
  }

  "A JokeJsonReadSupport" should "correctly return 0 Joke, for 0 jokes given in json" in {
    val givenJson =
      """
        |{
        |    "error": false,
        |    "amount": 0,
        |    "jokes": [
        |
        |    ]
        |}
        |""".stripMargin

    val expectedResult = Nil

    assert(Json.parse(givenJson).as[Seq[Joke]] === expectedResult)
  }

  "A JokeJsonReadSupport" should "fail parsing, if any of the joke essential elements is missing in json" in {
    val givenJsons = Seq(
      """
        |{
        |        "error": false,
        |        "type": "twopart",
        |        "setup": "Why do programmers wear glasses?",
        |        "delivery": "Because they need to C#",
        |        "flags": {
        |          "nsfw": false,
        |          "religious": false,
        |          "political": false,
        |          "racist": false,
        |          "sexist": false,
        |          "explicit": false
        |        },
        |        "id": 50,
        |        "safe": true,
        |        "lang": "en"
        |      }
        |""".stripMargin,
      """
        |{
        |        "error": false,
        |        "category": "Programming",
        |        "setup": "Why do programmers wear glasses?",
        |        "delivery": "Because they need to C#",
        |        "flags": {
        |          "nsfw": false,
        |          "religious": false,
        |          "political": false,
        |          "racist": false,
        |          "sexist": false,
        |          "explicit": false
        |        },
        |        "id": 50,
        |        "safe": true,
        |        "lang": "en"
        |      }
        |""".stripMargin,
      """
        |{
        |        "error": false,
        |        "category": "Programming",
        |        "type": "twopart",
        |        "delivery": "Because they need to C#",
        |        "flags": {
        |          "nsfw": false,
        |          "religious": false,
        |          "political": false,
        |          "racist": false,
        |          "sexist": false,
        |          "explicit": false
        |        },
        |        "id": 50,
        |        "safe": true,
        |        "lang": "en"
        |      }
        |""".stripMargin,
      """
        |{
        |        "error": false,
        |        "category": "Programming",
        |        "type": "twopart",
        |        "setup": "Why do programmers wear glasses?",
        |        "flags": {
        |          "nsfw": false,
        |          "religious": false,
        |          "political": false,
        |          "racist": false,
        |          "sexist": false,
        |          "explicit": false
        |        },
        |        "id": 50,
        |        "safe": true,
        |        "lang": "en"
        |      }
        |""".stripMargin,
      """
        |{
        |        "error": false,
        |        "category": "Programming",
        |        "type": "twopart",
        |        "setup": "Why do programmers wear glasses?",
        |        "delivery": "Because they need to C#",
        |        "flags": {
        |          "nsfw": false,
        |          "religious": false,
        |          "political": false,
        |          "racist": false,
        |          "sexist": false,
        |          "explicit": false
        |        },
        |        "safe": true,
        |        "lang": "en"
        |      }
        |""".stripMargin,
      """
        |{
        |        "error": false,
        |        "category": "Programming",
        |        "type": "twopart",
        |        "setup": "Why do programmers wear glasses?",
        |        "delivery": "Because they need to C#",
        |        "flags": {
        |          "nsfw": false,
        |          "religious": false,
        |          "political": false,
        |          "racist": false,
        |          "sexist": false,
        |          "explicit": false
        |        },
        |        "id": 50,
        |        "lang": "en"
        |      }
        |""".stripMargin,
      """
        |{
        |        "error": false,
        |        "category": "Programming",
        |        "type": "twopart",
        |        "setup": "Why do programmers wear glasses?",
        |        "delivery": "Because they need to C#",
        |        "flags": {
        |          "nsfw": false,
        |          "religious": false,
        |          "political": false,
        |          "racist": false,
        |          "sexist": false,
        |          "explicit": false
        |        },
        |        "id": 50,
        |        "safe": true
        |      }
        |""".stripMargin,


    )

    assertThrows[JsResultException](Json.parse(givenJsons(0)).as[Seq[Joke]])
    assertThrows[JsResultException](Json.parse(givenJsons(1)).as[Seq[Joke]])
    assertThrows[JsResultException](Json.parse(givenJsons(2)).as[Seq[Joke]])
    assertThrows[JsResultException](Json.parse(givenJsons(3)).as[Seq[Joke]])
    assertThrows[JsResultException](Json.parse(givenJsons(4)).as[Seq[Joke]])
    assertThrows[JsResultException](Json.parse(givenJsons(5)).as[Seq[Joke]])
    assertThrows[JsResultException](Json.parse(givenJsons(6)).as[Seq[Joke]])
  }

  "A JokeJsonReadSupport" should "fail parsing, if any essential value is null" in {
    val givenJsons = Seq(
      """
        |{
        |    "error": false,
        |    "category": null,
        |    "type": "twopart",
        |    "setup": "Why do programmers wear glasses?",
        |    "delivery": "Because they need to C#",
        |    "flags": {
        |        "nsfw": false,
        |        "religious": false,
        |        "political": false,
        |        "racist": false,
        |        "sexist": false,
        |        "explicit": false
        |    },
        |    "id": 50,
        |    "safe": true,
        |    "lang": "en"
        |}
        |""".stripMargin,
      """
        |{
        |    "error": false,
        |    "category": "Programming",
        |    "type": null,
        |    "setup": "Why do programmers wear glasses?",
        |    "delivery": "Because they need to C#",
        |    "flags": {
        |        "nsfw": false,
        |        "religious": false,
        |        "political": false,
        |        "racist": false,
        |        "sexist": false,
        |        "explicit": false
        |    },
        |    "id": 50,
        |    "safe": true,
        |    "lang": "en"
        |}
        |""".stripMargin,
      """
        |{
        |    "error": false,
        |    "category": "Programming",
        |    "type": "twopart",
        |    "setup": null,
        |    "delivery": "Because they need to C#",
        |    "flags": {
        |        "nsfw": false,
        |        "religious": false,
        |        "political": false,
        |        "racist": false,
        |        "sexist": false,
        |        "explicit": false
        |    },
        |    "id": 50,
        |    "safe": true,
        |    "lang": "en"
        |}
        |""".stripMargin,
      """
        |{
        |    "error": false,
        |    "category": "Programming",
        |    "type": "twopart",
        |    "setup": "Why do programmers wear glasses?",
        |    "delivery": null,
        |    "flags": {
        |        "nsfw": false,
        |        "religious": false,
        |        "political": false,
        |        "racist": false,
        |        "sexist": false,
        |        "explicit": false
        |    },
        |    "id": 50,
        |    "safe": true,
        |    "lang": "en"
        |}
        |""".stripMargin,
      """
        |{
        |    "error": false,
        |    "category": "Programming",
        |    "type": "twopart",
        |    "setup": "Why do programmers wear glasses?",
        |    "delivery": "Because they need to C#",
        |    "flags": {
        |        "nsfw": false,
        |        "religious": false,
        |        "political": false,
        |        "racist": false,
        |        "sexist": false,
        |        "explicit": false
        |    },
        |    "id": null,
        |    "safe": true,
        |    "lang": "en"
        |}
        |""".stripMargin,
      """
        |{
        |    "error": false,
        |    "category": "Programming",
        |    "type": "twopart",
        |    "setup": "Why do programmers wear glasses?",
        |    "delivery": "Because they need to C#",
        |    "flags": {
        |        "nsfw": false,
        |        "religious": false,
        |        "political": false,
        |        "racist": false,
        |        "sexist": false,
        |        "explicit": false
        |    },
        |    "id": 50,
        |    "safe": null,
        |    "lang": "en"
        |}
        |""".stripMargin,
      """
        |{
        |    "error": false,
        |    "category": "Programming",
        |    "type": "twopart",
        |    "setup": "Why do programmers wear glasses?",
        |    "delivery": "Because they need to C#",
        |    "flags": {
        |        "nsfw": false,
        |        "religious": false,
        |        "political": false,
        |        "racist": false,
        |        "sexist": false,
        |        "explicit": false
        |    },
        |    "id": 50,
        |    "safe": true,
        |    "lang": null
        |}
        |""".stripMargin
    )
    assertThrows[JsResultException](Json.parse(givenJsons(0)).as[Seq[Joke]])
    assertThrows[JsResultException](Json.parse(givenJsons(1)).as[Seq[Joke]])
    assertThrows[JsResultException](Json.parse(givenJsons(2)).as[Seq[Joke]])
    assertThrows[JsResultException](Json.parse(givenJsons(3)).as[Seq[Joke]])
    assertThrows[JsResultException](Json.parse(givenJsons(4)).as[Seq[Joke]])
  }

  "A JokeJsonReadSupport" should "fail parsing if error message is given" in {
    val givenJson =
      """
        |{
        |    "error": true,
        |    "internalError": false,
        |    "code": 106,
        |    "message": "No matching joke found",
        |    "causedBy": [
        |        "No jokes were found that match your provided filter(s)."
        |    ],
        |    "additionalInfo": "Error while finalizing joke filtering: No jokes were found that match your provided filter(s).",
        |    "timestamp": 1648369641023
        |}
        |""".stripMargin

    assertThrows[JsResultException](Json.parse(givenJson).as[Seq[Joke]])
  }

  "A JokeJsonReadSupport" should "fail processing if message schema is corrupted" in {
    val givenJson =
      """
        |{
        |    "error": false,
        |    "jokes": [
        |
        |    ]
        |}
        |""".stripMargin

    assertThrows[JsResultException](Json.parse(givenJson).as[Seq[Joke]])
  }
}