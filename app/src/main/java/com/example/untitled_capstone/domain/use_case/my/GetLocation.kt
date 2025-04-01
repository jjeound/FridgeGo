package com.example.untitled_capstone.domain.use_case.my

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.repository.MyRepository
import javax.inject.Inject

class GetLocation @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(): Resource<String> {
        return myRepository.getLocation()
    }
}