package com.raphaeldelio.elasticsearch.document
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "my-index")
data class Person(
    @Id
    val id: String? = null,

    var name: String
)
