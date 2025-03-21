package com.example.untitled_capstone.domain.use_case.my

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.domain.model.Profile
import com.example.untitled_capstone.domain.repository.LoginRepository
import com.example.untitled_capstone.domain.repository.MyRepository
import javax.inject.Inject

class GetOtherProfile @Inject constructor(
    private val myRepository: MyRepository
) {
    suspend operator fun invoke(nickname: String): Resource<Profile> {
        return myRepository.getOtherProfile(nickname)
    }
}