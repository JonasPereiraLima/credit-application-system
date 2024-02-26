package me.jonas.credit.application.system.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import me.jonas.credit.application.system.entity.Address
import me.jonas.credit.application.system.entity.Customer
import me.jonas.credit.application.system.exception.BusinessException
import me.jonas.credit.application.system.repository.CustomerRepository
import me.jonas.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.Optional
import java.util.Random

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
    @MockK lateinit var customerRespository: CustomerRepository
    @InjectMockKs lateinit var customerService: CustomerService

    @Test
    fun `should create customer`(){
        //given
        val fakeCustomer: Customer = buildCustomer()
        every { customerRespository.save(any()) } returns fakeCustomer

        //when
        val actual: Customer = customerService.save(fakeCustomer)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRespository.save(fakeCustomer) }
    }

    @Test
    fun `should find customer by id`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        every { customerRespository.findById(fakeId) } returns Optional.of(fakeCustomer)
        //when
        val actual: Customer = customerService.findById(fakeId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRespository.findById(fakeId) }
    }

    @Test
    fun `should not find customer by invalid id and throw BusinessException`(){
        //given
        val fakeId = Random().nextLong()
        every { customerRespository.findById(fakeId) } returns Optional.empty()
        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { customerService.findById(fakeId) }
            .withMessage("Id $fakeId not found")
        verify(exactly = 1) { customerRespository.findById(fakeId) }
    }

    @Test
    fun `should delete customer by id`(){
        //given
        val fakeId = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        every { customerRespository.findById(fakeId) } returns Optional.of(fakeCustomer)
        every { customerRespository.delete(fakeCustomer) } just runs
        //when
        customerService.delete(fakeId)
        //then
        verify(exactly = 1) { customerRespository.findById((fakeId)) }
        verify(exactly = 1) { customerRespository.delete(fakeCustomer) }
    }

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