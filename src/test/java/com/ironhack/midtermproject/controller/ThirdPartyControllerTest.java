package com.ironhack.midtermproject.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ThirdPartyControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Order(1)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void debtAccount_Ok_WithPenaltyFee() throws Exception {
        this.mockMvc.perform(patch("/third_party/debt_account/5")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "500")
                .param("secret_key", "Fito"))
                .andExpect(jsonPath("$.balance").value(20))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void creditAccount_UserDoesNotHasNotAccess_Forbidden() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_account/1")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "5")
                .param("secret_key", "Pepe"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditAccount_WrongHashKey_Forbidden() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_account/1")
                .header("hash_key", "Example")
                .param("amount", "5")
                .param("secret_key", "Pepe"))
                .andExpect(jsonPath("$.message").value("Access denied"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditAccount_WrongSecretKey_Forbidden() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_account/1")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "5")
                .param("secret_key", "OscarMayer"))
                .andExpect(jsonPath("$.message").value("Access denied"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditAccount_WrongSecretKeyAndHashKey_Forbidden() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_account/1")
                .header("hash_key", "Example")
                .param("amount", "5")
                .param("secret_key", "OscarMayer"))
                .andExpect(jsonPath("$.message").value("Access denied"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(6)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditAccount_AccountDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_account/125")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "5")
                .param("secret_key", "Pepe"))
                .andExpect(jsonPath("$.message").value("Account 125 does not exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditAccount_AccountisFrozen_BadRequest() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_account/3")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "5")
                .param("secret_key", "Macarena"))
                .andExpect(jsonPath("$.message").value("Account 3 is Frozen"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditAccount_Ok() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_account/7")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "5")
                .param("secret_key", "Copernico"))
                .andExpect(jsonPath("$.balance").value(565))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void debtAccount_AccountisFrozen_BadRequest() throws Exception {
        this.mockMvc.perform(patch("/third_party/debt_account/3")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "5")
                .param("secret_key", "Macarena"))
                .andExpect(jsonPath("$.message").value("Account 3 is Frozen"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(10)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void debtAccount_Ok() throws Exception {
        this.mockMvc.perform(patch("/third_party/debt_account/2")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "5")
                .param("secret_key", "Oscar"))
                .andExpect(jsonPath("$.balance").value(1025))
                .andExpect(status().isOk());
    }

    @Test
    @Order(11)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditCreditCard_CreditCardDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_credit_card/10")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "75")
                .param("secret_key", "Pepe"))
                .andExpect(jsonPath("$.message").value("CreditCard 10 does not exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(12)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditCreditCard_WrongHashKey_Forbidden() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_credit_card/1")
                .header("hash_key", "Example")
                .param("amount", "75")
                .param("secret_key", "Cupido"))
                .andExpect(jsonPath("$.message").value("Access denied"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(13)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditCreditCard_WrongSecretKey_Forbidden() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_credit_card/1")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "75")
                .param("secret_key", "Example"))
                .andExpect(jsonPath("$.message").value("Access denied"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(14)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditCreditCard_WrongSecretKeyAndHashKey_Forbidden() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_credit_card/1")
                .header("hash_key", "Example")
                .param("amount", "75")
                .param("secret_key", "Example"))
                .andExpect(jsonPath("$.message").value("Access denied"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(15)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void creditCreditCard_Ok() throws Exception {
        this.mockMvc.perform(patch("/third_party/credit_credit_card/1")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "7")
                .param("secret_key", "Cupido"))
                .andExpect(jsonPath("$.balance").value(1007))
                .andExpect(status().isOk());
    }

    @Test
    @Order(16)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void debtCreditCard_CreditLimitExceeded_BadRequest() throws Exception {
        this.mockMvc.perform(patch("/third_party/debt_credit_card/1")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "200")
                .param("secret_key", "Cupido"))
                .andExpect(jsonPath("$.message").value("creditLimit Exceeded"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(17)
    @WithMockUser(username="eduman", password="eduman13", roles="THIRD_PARTY")
    void debtCreditCard_Ok() throws Exception {
        this.mockMvc.perform(patch("/third_party/debt_credit_card/1")
                .header("hash_key", "f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a")
                .param("amount", "100")
                .param("secret_key", "Cupido"))
                .andExpect(jsonPath("$.balance").value(907))
                .andExpect(status().isOk());
    }
}