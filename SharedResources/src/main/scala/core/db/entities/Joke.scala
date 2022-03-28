package core.db.entities

import core.db.entities.enums.{JokeCategory, JokeType}

case class Joke(id: Long,
                category: JokeCategory,
                `type`: JokeType,
                content: String,
                is_safe: Boolean,
                lang: String)