package me.jonas.credit.application.system.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotNull
import me.jonas.credit.application.system.entity.Credit
import me.jonas.credit.application.system.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Invalid Input.")val creditValue: BigDecimal,
    @field:Future val dayFirstOfInstallment: LocalDate,
    val numberOfInstallments: Int, // TODO: fazer validação
    @field:NotNull(message = "Invalid Input.") val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
