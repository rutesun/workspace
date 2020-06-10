package com.rutesun.transaction

import org.springframework.data.repository.CrudRepository

interface ItemRepository: CrudRepository<Item, Number>