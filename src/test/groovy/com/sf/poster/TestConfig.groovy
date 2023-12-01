package com.sf.poster

import com.sf.poster.repository.CustomerRepository
import com.sf.poster.repository.PostRepository

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean

@TestConfiguration
class TestConfig {

    @MockBean
    CustomerRepository customerRepository
    @MockBean
    PostRepository postRepository
}
