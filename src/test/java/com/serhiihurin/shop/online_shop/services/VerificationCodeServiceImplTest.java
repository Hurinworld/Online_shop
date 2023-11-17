package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.VerificationCodeRepository;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.VerificationCode;
import com.serhiihurin.shop.online_shop.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class VerificationCodeServiceImplTest {
    @Mock
    private VerificationCodeRepository verificationCodeRepository;
    private VerificationCodeService verificationCodeService;
    private User testUser;


    @BeforeEach
    void setUp() {
        verificationCodeService = new VerificationCodeServiceImpl(verificationCodeRepository);
        testUser = User.builder()
                .id(1L)
                .firstName("User")
                .lastName("For test")
                .email("testUser@gmail.com")
                .password("password")
                .cash(26665.0)
                .role(Role.CLIENT)
                .build();
    }

    @Test
    void createVerificationCode() {
        //given
        Mockito.when(verificationCodeRepository.findByUserId(testUser.getId()))
                .thenReturn(Optional.empty());
        //when
        VerificationCode createdCode = verificationCodeService.createVerificationCode(testUser);
        //then
        Mockito.verify(verificationCodeRepository, Mockito.never()).deleteVerificationCodeByUserId(testUser.getId());
        Mockito.verify(verificationCodeRepository).save(any(VerificationCode.class));
    }

    @Test
    void createVerificationCode_WhenUserHasExistingCode_ShouldDeleteAndCreateNewCode() {
        //given
        VerificationCode existingCode = new VerificationCode();
        existingCode.setVerificationCode("existingCode");
        existingCode.setUser(testUser);

        // Mock behavior of the repository
        Mockito.when(verificationCodeRepository.findByUserId(testUser.getId()))
                .thenReturn(Optional.of(existingCode));
        //when
        VerificationCode createdCode = verificationCodeService.createVerificationCode(testUser);
        //then
        Mockito.verify(verificationCodeRepository).deleteVerificationCodeByUserId(testUser.getId());
        Mockito.verify(verificationCodeRepository).save(any(VerificationCode.class));
    }

    @Test
    void generateVerificationCode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method =
                VerificationCodeServiceImpl.class.getDeclaredMethod("generateVerificationCode");
        method.setAccessible(true);
        String verificationCode = (String) method.invoke(verificationCodeService);
        assertNotNull(verificationCode);
    }
}