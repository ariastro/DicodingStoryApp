package io.astronout.dicodingstoryapp.domain

import io.astronout.dicodingstoryapp.domain.model.Login
import io.astronout.dicodingstoryapp.domain.model.Register
import io.astronout.dicodingstoryapp.vo.Resource
import kotlinx.coroutines.flow.Flow

interface AuthUsecase {

    fun login(email: String, password: String): Flow<Resource<Login>>

    fun register(name: String, email: String, password: String): Flow<Resource<Register>>

}