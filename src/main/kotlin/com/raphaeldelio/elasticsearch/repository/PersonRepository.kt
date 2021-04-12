package com.raphaeldelio.elasticsearch.repository

import com.raphaeldelio.elasticsearch.document.Person
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface PersonRepository : ElasticsearchRepository<Person, String>
