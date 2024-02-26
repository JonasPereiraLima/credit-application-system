package me.jonas.credit.application.system.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import io.mockk.verify
import me.jonas.credit.application.system.entity.Address
import me.jonas.credit.application.system.entity.Credit
import me.jonas.credit.application.system.entity.Customer
import me.jonas.credit.application.system.repository.CreditRepository
import me.jonas.credit.application.system.service.impl.CreditService
import me.jonas.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.Random
import java.util.UUID

@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK lateinit var creditRepository: CreditRepository
    @MockK lateinit var customerService: CustomerService
    @InjectMockKs lateinit var creditService: CreditService

    @BeforeEach fun setUp(){
        MockKAnnotations.init(this)
        //creditService = CreditService(creditRepository, customerService)
    }

    @AfterEach fun tearDown(){
        unmockkAll()
    }

    @Test
    fun `should create credit`(){
        //given
        val fakeCredit: Credit = buildCredit()
        every { customerService.findById(fakeCredit.customer?.id!!) } returns fakeCredit.customer!!
        every { creditRepository.save(fakeCredit) } returns fakeCredit
        //when
        val actual: Credit = this.creditService.save(fakeCredit)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should find all credits by customer id`(){
        //given
        val customerid: Long = buildCredit().customer?.id!!
        val creditList: List<Credit> = listOf(buildCredit(), buildCredit())
        every { creditService.findAllByCustomer(customerid) } returns creditList
        //when
        val actual: List<Credit> = creditService.findAllByCustomer(customerid)
        //then
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).isSameAs(creditList)
        verify(exactly = 1) { creditRepository.findAllByCustomer(customerid) }
    }

    @Test
    fun `should find a credit by credit code and customer id`(){
        //given
        val creditCode: UUID = UUID.randomUUID()
        val credit: Credit = buildCredit()
        val customerId: Long = credit.customer?.id!!
        every { creditRepository.findByCreditCode(creditCode) } returns credit
        //when
        val actual: Credit = creditService.findByCreditCode(customerId, creditCode)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(credit)
        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }
    }


    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(500.0),
        dayFirstInstallment: LocalDate = LocalDate.of(2024, Month.APRIL, 20),
        numberOfInstallments: Int = 5,
        customer: Customer = buildCustomer()
    ): Credit = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer
    )

    private fun buildCustomer(
        firstName: String = "Jonas",
        lastName: String = "Pereira",
        cpf: String = "74376455806",
        email: String = "Jonas@email.com",
        password: String = "123456",
        zipCode: String = "12345",
        street: String = "Rua das rosas",
        income: BigDecimal = BigDecimal.valueOf(2500.0),
        id: Long =  1L
    ) = Customer (
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street
        ),
        income = income,
        id = id
    )
}