package com.raphaeldelio.elasticsearch.controller

import com.raphaeldelio.elasticsearch.document.Person
import com.raphaeldelio.elasticsearch.repository.PersonRepository
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/person")
class PersonController(
    val personRepository: PersonRepository
) {

    @GetMapping
    fun getAll(): List<Person> {
        return personRepository.findAll().toList()
    }

    @PostMapping
    fun save(@RequestBody person: Person): Person {
        personRepository.save(person)
        return person
    }

    @PutMapping
    fun updateNameById(@RequestParam id: String, @RequestParam name: String): Optional<Person> {
        val person = personRepository.findById(id)
        person.ifPresent {
            it.name = name
            personRepository.save(person.get())
        }
        return person
    }

    @DeleteMapping
    fun deleteById(@RequestParam id: String) {
        personRepository.deleteById(id)
    }
}
