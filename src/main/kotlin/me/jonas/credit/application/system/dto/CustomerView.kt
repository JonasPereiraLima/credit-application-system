package me.jonas.credit.application.system.dto

import me.jonas.credit.application.system.entity.Customer
import java.math.BigDecimal

data class CustomerView(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val zipCode: String,
    val street: String
) {
    constructor(customer: Customer): this (
        firstName = customer.firstName,
        lastName = customer.lastName,
        cpf = customer.cpf,
        email = customer.email,
        income = customer.income,
        zipCode = customer.address.zipCode,
        street = customer.address.street
    )
}
