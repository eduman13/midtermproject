package com.ironhack.midtermproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.CreditCard;
import com.ironhack.midtermproject.model.Saving;
import com.ironhack.midtermproject.model.enums.Status;
import com.ironhack.midtermproject.model.users.ThirdParty;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminClControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Checking checking;
    private Saving saving;
    private CreditCard creditCard;
    private ThirdParty thirdParty;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Order(1)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCreditCardSecondaryOwner_PrimaryOwnerIsTheSameSecondaryOwner_BadRequest() throws Exception {
        this.mockMvc.perform(post("/admin/create_credit_card_secondary_owner/1/1"))
                .andExpect(jsonPath("$.message").value("Primary owner and second owner are the same AccountHolder"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCreditCardSecondaryOwner_Ok() throws Exception {
        this.mockMvc.perform(post("/admin/create_credit_card_secondary_owner/2/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(3)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCreditCardSecondaryOwner_CreditCardDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(post("/admin/create_credit_card_secondary_owner/1/2"))
                .andExpect(jsonPath("$.message").value("CreditCard does not exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCreditCardSecondaryOwner_CreditCardHasAlreadyASecondaryOwner_BadRequest() throws Exception {
        this.mockMvc.perform(post("/admin/create_credit_card_secondary_owner/3/1"))
                .andExpect(jsonPath("$.message").value("CreditCard 1 has already a secondary owner"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCreditCardSecondaryOwner_AccountHolderHasAlreadyASecondaryCreditCard_BadRequest() throws Exception {
        this.mockMvc.perform(post("/admin/create_credit_card_secondary_owner/2/1"))
                .andExpect(jsonPath("$.message").value("AccountHolder 2 has already a secondary CreditCard"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    @WithMockUser(username="eddy", password="eddy", roles="ACCOUNT_HOLDER")
    void createChecking_UserAccessDenied_Forbidden() throws Exception {
        checking = new Checking();
            checking.setBalance(new BigDecimal(1000));
            checking.setStatus(Status.ACTIVE);
        this.mockMvc.perform(post("/admin/create_checking/1")
                .content(objectMapper.writeValueAsString(checking))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(7)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createChecking_AccountHolderDoesNotExist_NotFound() throws Exception{
        checking = new Checking();
        checking.setBalance(new BigDecimal(1000));
        checking.setStatus(Status.ACTIVE);
        this.mockMvc.perform(post("/admin/create_checking/90")
                .content(objectMapper.writeValueAsString(checking))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Primary Owner does not exist (id: 90)"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createChecking_createCheckingAccount_OkWithDefaultMinimumBalance() throws Exception{
        checking = new Checking();
            checking.setBalance(new BigDecimal(1000));
            checking.setStatus(Status.ACTIVE);
        this.mockMvc.perform(post("/admin/create_checking/4")
                .content(objectMapper.writeValueAsString(checking))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.minimumBalance").value(250))
                .andExpect(jsonPath("$.penaltyFee").value(40))
                .andExpect(jsonPath("$.monthlyMaintenanceFee").value(12))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(9)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createChecking_createCheckingAccount_Ok() throws Exception{
        checking = new Checking();
            checking.setBalance(new BigDecimal(1000));
            checking.setMinimumBalance(new BigDecimal(300));
            checking.setStatus(Status.ACTIVE);
        this.mockMvc.perform(post("/admin/create_checking/5")
                .content(objectMapper.writeValueAsString(checking))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.minimumBalance").value(300))
                .andExpect(jsonPath("$.penaltyFee").value(40))
                .andExpect(jsonPath("$.monthlyMaintenanceFee").value(12))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(10)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createChecking_createStudentChecking_Ok() throws Exception{
        checking = new Checking();
        checking.setBalance(new BigDecimal(1000));
        checking.setMinimumBalance(new BigDecimal(300));
        checking.setStatus(Status.ACTIVE);
        this.mockMvc.perform(post("/admin/create_checking/6")
                .content(objectMapper.writeValueAsString(checking))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.penaltyFee").value(40))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(11)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createChecking_AccountAlreadyHasPrimaryOwner_BadRequest() throws Exception{
        checking = new Checking();
            checking.setBalance(new BigDecimal(1000));
            checking.setMinimumBalance(new BigDecimal(300));
            checking.setStatus(Status.ACTIVE);
        this.mockMvc.perform(post("/admin/create_checking/1")
                .content(objectMapper.writeValueAsString(checking))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("AccountHolder 1 has already a primary Account"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(12)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createSaving_AccountHolderHasAlreadyAccount_BadRequest() throws Exception {
        saving = new Saving();
        saving.setBalance(new BigDecimal(150));
        saving.setStatus(Status.ACTIVE);
        this.mockMvc.perform(post("/admin/create_saving/1")
                .content(objectMapper.writeValueAsString(saving))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("AccountHolder 1 has already a primary Account"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(13)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createSaving_BadInterestRate_BadRequest() throws Exception {
        saving = new Saving();
            saving.setBalance(new BigDecimal(150));
            saving.setStatus(Status.ACTIVE);
            saving.setInterestRate(new BigDecimal("0.55"));
        this.mockMvc.perform(post("/admin/create_saving/1")
                .content(objectMapper.writeValueAsString(saving))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("must be less than or equal to 0.5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(14)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createSaving_BadMinimumBalance_BadRequest() throws Exception {
        saving = new Saving();
            saving.setBalance(new BigDecimal(150));
            saving.setMinimumBalance(new BigDecimal(10));
            saving.setStatus(Status.ACTIVE);
        this.mockMvc.perform(post("/admin/create_saving/1")
                .content(objectMapper.writeValueAsString(saving))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("minimumBalance not in the required range"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(15)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createSaving_Ok_DefaultMinimumBalanceAndInterestRate() throws Exception {
        saving = new Saving();
        saving.setBalance(new BigDecimal(150));
        saving.setStatus(Status.ACTIVE);
        this.mockMvc.perform(post("/admin/create_saving/13")
                .content(objectMapper.writeValueAsString(saving))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(150))
                .andExpect(jsonPath("$.minimumBalance").value(1000))
                .andExpect(jsonPath("$.penaltyFee").value(40))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.interestRate").value(0.0025))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(16)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createSaving_Ok() throws Exception {
        saving = new Saving();
            saving.setBalance(new BigDecimal(150));
            saving.setMinimumBalance(new BigDecimal(900));
            saving.setInterestRate(new BigDecimal("0.03"));
            saving.setStatus(Status.ACTIVE);
        this.mockMvc.perform(post("/admin/create_saving/14")
                .content(objectMapper.writeValueAsString(saving))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(150))
                .andExpect(jsonPath("$.minimumBalance").value(900))
                .andExpect(jsonPath("$.penaltyFee").value(40))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.interestRate").value(0.03))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(17)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCreditCard_AccountHolderHasAlreadyCreditCard_BadRequest() throws Exception {
        creditCard = new CreditCard();
            creditCard.setBalance(new BigDecimal(10000));
        this.mockMvc.perform(post("/admin/create_credit_card/1")
                .content(objectMapper.writeValueAsString(creditCard))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("AccountHolder 1 has already a CreditCard"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(18)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCreditCard_BadCreditLimit_BadRequest() throws Exception {
        creditCard = new CreditCard();
            creditCard.setBalance(new BigDecimal(10000));
            creditCard.setCreditLimit(new BigDecimal(5));
        this.mockMvc.perform(post("/admin/create_credit_card/5")
                .content(objectMapper.writeValueAsString(creditCard))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("creditLimit not in the required range"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(19)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCreditCard_BadInterestRate_BadRequest() throws Exception {
        creditCard = new CreditCard();
            creditCard.setBalance(new BigDecimal(10000));
            creditCard.setInterestRate(new BigDecimal("0.3"));
        this.mockMvc.perform(post("/admin/create_credit_card/5")
                .content(objectMapper.writeValueAsString(creditCard))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("interestRate not in the required range"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(20)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCreditCard_Ok_DefaultCreditLimitAndInterestRate() throws Exception {
        creditCard = new CreditCard();
            creditCard.setBalance(new BigDecimal(10000));
        this.mockMvc.perform(post("/admin/create_credit_card/5")
                .content(objectMapper.writeValueAsString(creditCard))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(10000))
                .andExpect(jsonPath("$.creditLimit").value(100))
                .andExpect(jsonPath("$.interestRate").value(0.2))
                .andExpect(jsonPath("$.penaltyFee").value(40))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(21)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCreditCard_Ok() throws Exception {
        creditCard = new CreditCard();
            creditCard.setBalance(new BigDecimal(10000));
            creditCard.setCreditLimit(new BigDecimal(200));
            creditCard.setInterestRate(new BigDecimal("0.15"));
        this.mockMvc.perform(post("/admin/create_credit_card/7")
                .content(objectMapper.writeValueAsString(creditCard))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(10000))
                .andExpect(jsonPath("$.creditLimit").value(200))
                .andExpect(jsonPath("$.interestRate").value(0.15))
                .andExpect(jsonPath("$.penaltyFee").value(40))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(22)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCheckingSecondaryOwner_PrimaryOwnerIsTheSameSecondaryOwner_BadRequest() throws Exception {
        this.mockMvc.perform(post("/admin/create_checking_secondary_owner/2/2"))
                .andExpect(jsonPath("$.message").value("Primary owner and second owner are the same AccountHolder"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(23)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCheckingSecondaryOwner_Ok() throws Exception {
        this.mockMvc.perform(post("/admin/create_checking_secondary_owner/1/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(24)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createCheckingSecondaryOwner_AccountHasAlreadyASecondaryOwner_BadRequest() throws Exception {
        this.mockMvc.perform(post("/admin/create_checking_secondary_owner/1/2"))
                .andExpect(jsonPath("$.message").value("AccountHolder 1 has already a secondary Account"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(25)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void getBalanceAccount_AccountDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(get("/admin/get_balance_by_account/20"))
                .andExpect(jsonPath("$.message").value("Primary Owner does not exist (id: 20)"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(26)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void getBalanceAccount_AccountHolderDoesNotHaveAccount_NotFound() throws Exception {
        this.mockMvc.perform(get("/admin/get_balance_by_account/12"))
                .andExpect(jsonPath("$.message").value("Account Holder 12 does not have Account"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(27)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void getBalanceAccount_AccountIsFrozen_BadRequest() throws Exception {
        this.mockMvc.perform(get("/admin/get_balance_by_account/3"))
                .andExpect(jsonPath("$.message").value("Account 3 is Frozen"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(28)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void getBalanceAccount_Ok() throws Exception {
        this.mockMvc.perform(get("/admin/get_balance_by_account/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(29)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void getBalanceCreditCard_UserDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(get("/admin/get_balance_by_credit_card/20"))
                .andExpect(jsonPath("$.message").value("Primary Owner does not exist (id: 20)"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(30)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void getBalanceCreditCard_UserDoesNotHaveCreditCard_NotFound() throws Exception {
        this.mockMvc.perform(get("/admin/get_balance_by_credit_card/2"))
                .andExpect(jsonPath("$.message").value("Account Holder 2 does not have creditCard"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(31)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void getBalanceCreditCard_Ok() throws Exception {
        this.mockMvc.perform(get("/admin/get_balance_by_credit_card/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(32)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void creditAccount_AccountHolderDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(patch("/admin/credit_account/20")
                .param("amount", "5"))
                .andExpect(jsonPath("$.message").value("Primary Owner does not exist (id: 20)"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(33)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void creditAccount_AccountHolderDoesNotHaveAnyAccount_NotFound() throws Exception {
        this.mockMvc.perform(patch("/admin/credit_account/12")
                .param("amount", "5"))
                .andExpect(jsonPath("$.message").value("Account Holder 12 does not have Account"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(34)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void creditAccount_AccountIsFrozen_BadRequest() throws Exception {
        this.mockMvc.perform(patch("/admin/credit_account/3")
                .param("amount", "5"))
                .andExpect(jsonPath("$.message").value("Account 3 is Frozen"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(35)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void creditAccount_Ok() throws Exception {
        this.mockMvc.perform(patch("/admin/credit_account/1")
                .param("amount", "5"))
                .andExpect(jsonPath("$.balance").value(280))
                .andExpect(status().isOk());
    }

    @Test
    @Order(36)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void debtAccount_AccountHolderDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(patch("/admin/debt_account/20")
                .param("amount", "5"))
                .andExpect(jsonPath("$.message").value("Primary Owner does not exist (id: 20)"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(37)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void debtAccount_AccountHolderDoesNotHaveAnyAccount_NotFound() throws Exception {
        this.mockMvc.perform(patch("/admin/debt_account/12")
                .param("amount", "5"))
                .andExpect(jsonPath("$.message").value("Account Holder 12 does not have Account"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(38)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void debtAccount_AccountIsFrozen_BadRequest() throws Exception {
        this.mockMvc.perform(patch("/admin/debt_account/3")
                .param("amount", "5"))
                .andExpect(jsonPath("$.message").value("Account 3 is Frozen"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(39)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void debtAccount_FraudDetectionMoreThanOneTransactionPerSecond_BadRequest() throws Exception {
        this.mockMvc.perform(patch("/admin/debt_account/1")
                .param("amount", "5"))
                .andExpect(jsonPath("$.message").value("Fraud detected on account 1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(40)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void debtAccount_Ok() throws Exception {
        this.mockMvc.perform(patch("/admin/debt_account/8")
                .param("amount", "75"))
                .andExpect(jsonPath("$.balance").value(485))
                .andExpect(status().isOk());
    }


    @Test
    @Order(41)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void debtAccount_Ok_WithPenaltyFee() throws Exception {
        this.mockMvc.perform(patch("/admin/debt_account/10")
                .param("amount", "500"))
                .andExpect(jsonPath("$.balance").value(20))
                .andExpect(status().isOk());
    }

    @Test
    @Order(42)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void creditCreditCard_AccountHolderDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(patch("/admin/credit_credit_card/20")
                .param("amount", "75"))
                .andExpect(jsonPath("$.message").value("Primary Owner does not exist (id: 20)"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(43)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void creditCreditCard_AccountHolderDoesNotHaveAnyCreditCard_NotFound() throws Exception {
        this.mockMvc.perform(patch("/admin/credit_credit_card/12")
                .param("amount", "75"))
                .andExpect(jsonPath("$.message").value("Account Holder 12 does not have creditCard"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(44)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void creditCreditCard_Ok() throws Exception {
        this.mockMvc.perform(patch("/admin/credit_credit_card/1")
                .param("amount", "75"))
                .andExpect(jsonPath("$.balance").value(1075))
                .andExpect(status().isOk());
    }

    @Test
    @Order(45)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void debtCreditCard_AccountHolderDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(patch("/admin/debt_credit_card/20")
                .param("amount", "75"))
                .andExpect(jsonPath("$.message").value("Primary Owner does not exist (id: 20)"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(46)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void debtCreditCard_AccountHolderDoesNotHaveAnyCreditCard_NotFound() throws Exception {
        this.mockMvc.perform(patch("/admin/debt_credit_card/12")
                .param("amount", "75"))
                .andExpect(jsonPath("$.message").value("Account Holder 12 does not have creditCard"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(47)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void debtCreditCard_CreditLimitExceeded_BadRequest() throws Exception {
        this.mockMvc.perform(patch("/admin/debt_credit_card/1")
                .param("amount", "250"))
                .andExpect(jsonPath("$.message").value("creditLimit Exceeded"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(48)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void debtCreditCard_Ok() throws Exception {
        this.mockMvc.perform(patch("/admin/debt_credit_card/1")
                .param("amount", "75"))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(status().isOk());
    }

    @Test
    @Order(49)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void createThirdParty() throws Exception {
        thirdParty = new ThirdParty();
            thirdParty.setUsername("Puyi");
            thirdParty.setHashKey("KobeBryant");
            thirdParty.setPassword("$2a$10$I7UMuPJslD3VKXRfab3oku6Am4X/zMFm4UutWz.Sy1tOp5KcbbUcu");
        this.mockMvc.perform(post("/admin/create_third_party")
                .content(objectMapper.writeValueAsString(thirdParty))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("Puyi"))
                .andExpect(jsonPath("$.password").value("$2a$10$I7UMuPJslD3VKXRfab3oku6Am4X/zMFm4UutWz.Sy1tOp5KcbbUcu"))
                .andExpect(jsonPath("$.hashKey").value("f109887264f2b2377c1d91ac0ad9296a3d249f26d28d9e58df04906e2624af37"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(50)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void checkingSecondaryOwner_AccountDoesNotExist_NotFound() throws Exception {
        this.mockMvc.perform(post("/admin/create_checking_secondary_owner/13/20"))
                .andExpect(jsonPath("$.message").value("Account does not exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(51)
    @WithMockUser(username="admin", password="admin", roles="ADMIN")
    void checkingSecondaryOwner_AccountHaveSecondaryOwner_BadRequest() throws Exception {
        this.mockMvc.perform(post("/admin/create_checking_secondary_owner/3/2"))
                .andExpect(jsonPath("$.message").value("Account 2 has already a secondary owner"))
                .andExpect(status().isBadRequest());
    }
}