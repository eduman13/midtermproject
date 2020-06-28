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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountHolderControllerTest {

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
    @WithMockUser(username="jose_mourinho", password="the_special_one", roles="ACCOUNT_HOLDER")
    void getBalance_Fail_UserNotExist_Forbidden() throws Exception {
        this.mockMvc.perform(get("/account_holder/get_balance/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void getBalance_Fail_UserNotAllowed_Forbidden() throws Exception {
        this.mockMvc.perform(get("/account_holder/get_balance/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @WithMockUser(username="eddas", password="eddy", roles="ACCOUNT_HOLDER")
    public void getBalance_AccountDoesNotBelongsToUser_Forbidden() throws Exception {
        this.mockMvc.perform(get("/account_holder/get_balance/1"))
                .andExpect(jsonPath("$.message").value("Access denied"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    @WithMockUser(username="eddyLover", password="eddy", roles="ACCOUNT_HOLDER")
    public void getBalance_AccountHolderDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(get("/account_holder/get_balance/30"))
                .andExpect(jsonPath("$.message").value("Account Holder does not exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    @WithMockUser(username="user12", password="eddy", roles="ACCOUNT_HOLDER")
    public void getBalance_AccountHolderDoesNotHaveAccount_NotFound() throws Exception {
        this.mockMvc.perform(get("/account_holder/get_balance/12"))
                .andExpect(jsonPath("$.message").value("Account Holder 12 does not have any Account"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    @WithMockUser(username="eddyLover", password="eddy", roles="ACCOUNT_HOLDER")
    public void getBalance_AccountFrozen_BadRequest() throws Exception {
        this.mockMvc.perform(get("/account_holder/get_balance/3"))
                .andExpect(jsonPath("$.message").value("Account 3 is Frozen"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    @WithMockUser(username="eddas", password="eddy", roles="ACCOUNT_HOLDER")
    public void getBalance_Ok() throws Exception {
        this.mockMvc.perform(get("/account_holder/get_balance/2"))
                .andExpect(jsonPath("$.balance").value(1040))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithMockUser(username="eddas", password="eddy", roles="ACCOUNT_HOLDER")
    void creditAccount_AccountDoesNotBelongsToIdOwner_Forbidden() throws Exception {
        this.mockMvc.perform(patch("/account_holder/transfer_account/1/2")
                .param("amount", "10000")
                .param("beneficiaryName", "Nadie"))
                .andExpect(jsonPath("$.message").value("Access denied"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(9)
    @WithMockUser(username="eddy", password="eddy", roles="ACCOUNT_HOLDER")
    void creditAccount_AccountHolderDoesNotExist_NoFound() throws Exception {
        this.mockMvc.perform(patch("/account_holder/transfer_account/345/2")
                .param("amount", "10000")
                .param("beneficiaryName", "Nadie"))
                .andExpect(jsonPath("$.message").value("Account Holder does not exist"))
                .andExpect(status().isNotFound());
    }

//    @Test
//    @Order(10)
//    @WithMockUser(username="user18", password="eddy", roles="ACCOUNT_HOLDER")
//    void creditAccount_AccountHolderIsTheOwnerOfTheBeneficiaryAccount_BadRequest() throws Exception {
//        this.mockMvc.perform(patch("/account_holder/transfer_account/18/11")
//                .param("amount", "10000")
//                .param("beneficiaryName", "Nadie"))
//                .andExpect(jsonPath("$.message").value("Account Holder is the owner of the beneficiary account"))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    @Order(10)
    @WithMockUser(username="user12", password="eddy", roles="ACCOUNT_HOLDER")
    void creditAccount_AccountHolderDoesNotHaveAccount_NotFound() throws Exception {
        this.mockMvc.perform(patch("/account_holder/transfer_account/12/2")
                .param("amount", "10")
                .param("beneficiaryName", "Nadie"))
                .andExpect(jsonPath("$.message").value("Account Holder does not have account"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(11)
    @WithMockUser(username="eddy", password="eddy", roles="ACCOUNT_HOLDER")
    void creditAccount_AmountNotEnough_BadRequest() throws Exception {
        this.mockMvc.perform(patch("/account_holder/transfer_account/1/2")
                .param("amount", "1000000")
                .param("beneficiaryName", "Nadie"))
                .andExpect(jsonPath("$.message").value("Amount for transfer not enough"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(12)
    @WithMockUser(username="eddy", password="eddy", roles="ACCOUNT_HOLDER")
    void creditAccount_BeneficaryAccountDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(patch("/account_holder/transfer_account/1/90")
                .param("amount", "10")
                .param("beneficiaryName", "Nadie"))
                .andExpect(jsonPath("$.message").value("Beneficiary account does not exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(13)
    @WithMockUser(username="eddas", password="eddy", roles="ACCOUNT_HOLDER")
    void creditAccount_Ok() throws Exception {
        this.mockMvc.perform(patch("/account_holder/transfer_account/2/11")
                .param("amount", "10")
                .param("beneficiaryName", "Nadie"))
                .andExpect(jsonPath("$.balance").value(1030))
                .andExpect(status().isOk());
    }

    @Test
    @Order(14)
    @WithMockUser(username="user16", password="eddy", roles="ACCOUNT_HOLDER")
    void creditAccount_Ok_WithPenaltyFee() throws Exception {
        this.mockMvc.perform(patch("/account_holder/transfer_account/16/4")
                .param("amount", "500")
                .param("beneficiaryName", "Nadie"))
                .andExpect(jsonPath("$.balance").value(20))
                .andExpect(status().isOk());
    }

    @Test
    @Order(15)
    @WithMockUser(username="user12", password="eddy", roles="ACCOUNT_HOLDER")
    void getBalance_AccountHolderDoesNotHaveAnyAccount_NotFound() throws Exception {
        this.mockMvc.perform(get("/account_holder/get_balance/12"))
                .andExpect(jsonPath("$.message").value("Account Holder 12 does not have any Account"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(16)
    @WithMockUser(username="user19", password="eddy", roles="ACCOUNT_HOLDER")
    void creditAccount_AccountHolderIsTheOwnerOfTheBeneficiaryAccount_BadRequest() throws Exception {
        this.mockMvc.perform(patch("/account_holder/transfer_account/19/12")
                .param("amount", "10")
                .param("beneficiaryName", "Nadie"))
                .andExpect(jsonPath("$.message").value("Account Holder is the owner of the beneficiary account"))
                .andExpect(status().isBadRequest());
    }
}