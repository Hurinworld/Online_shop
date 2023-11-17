package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.FeedbackRepository;
import com.serhiihurin.shop.online_shop.dto.FeedbackUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Feedback;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.ProductRate;
import com.serhiihurin.shop.online_shop.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {
    @Mock
    private FeedbackRepository feedbackRepository;
    private FeedbackService feedbackService;
    private Feedback testFeedback;
    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        feedbackService = new FeedbackServiceImpl(feedbackRepository);
        testUser = User.builder()
                .id(1L)
                .firstName("User")
                .lastName("For test")
                .email("testUser@gmail.com")
                .password("password")
                .cash(26665.0)
                .role(Role.CLIENT)
                .build();
        testProduct = Product.builder()
                .id(1L)
                .name("AMD Ryzen 7 5700X")
                .description("CPU")
                .amount(100)
                .price(8500.0)
                .build();
        testFeedback = Feedback.builder()
                .id(1L)
                .text("Test text")
                .rate(ProductRate.GOOD)
                .time(LocalDateTime.now().minusMinutes(10))
                .user(testUser)
                .product(testProduct)
                .build();
    }

    @Test
    void getAllFeedbacks() {
        //when
        feedbackService.getAllFeedbacks();
        //then
        Mockito.verify(feedbackRepository).findAll();
    }

    @Test
    void getFeedbacksByProductId() {
        // given
        List<Feedback> expectedFeedbacks = List.of(testFeedback);

        Mockito.when(feedbackRepository.getFeedbacksByProductId(testProduct.getId())).thenReturn(expectedFeedbacks);
        // when
        List<Feedback> actualFeedbacks = feedbackService.getFeedbacksByProductId(testProduct.getId());
        // then
        assertThat(expectedFeedbacks).isEqualTo(actualFeedbacks);
        Mockito.verify(feedbackRepository).getFeedbacksByProductId(testProduct.getId());
    }

    @Test
    void getFeedbacksByClientId() {
        // given
        List<Feedback> expectedFeedbacks = List.of(testFeedback);

        Mockito.when(feedbackRepository.getFeedbacksByUserId(testUser.getId())).thenReturn(expectedFeedbacks);
        // when
        List<Feedback> actualFeedbacks = feedbackService.getFeedbacksByClientId(testUser.getId());
        // then
        assertThat(expectedFeedbacks).isEqualTo(actualFeedbacks);
        Mockito.verify(feedbackRepository).getFeedbacksByUserId(testUser.getId());
    }

    @Test
    void getFeedback() {
        //when
        Mockito.when(feedbackRepository.findById(testFeedback.getId())).thenReturn(Optional.of(testFeedback));
        Feedback capturedValue = feedbackService.getFeedback(testFeedback.getId());
        //then
        assertThat(capturedValue).usingRecursiveComparison().isEqualTo(testFeedback);
    }

    @Test
    void saveFeedback() {
        //when
        feedbackService.saveFeedback(testFeedback);
        //then
        ArgumentCaptor<Feedback> feedbackArgumentCaptor = ArgumentCaptor.forClass(Feedback.class);
        Mockito.verify(feedbackRepository).save(feedbackArgumentCaptor.capture());
        Feedback capturedValue = feedbackArgumentCaptor.getValue();
        assertThat(capturedValue).isEqualTo(testFeedback);
    }

    @Test
    void updateFeedback() {
        //given
        String newText = "New text";
        ProductRate newRate = ProductRate.AVERAGE;
        LocalDateTime oldFeedbackTime = testFeedback.getTime();
        FeedbackUpdateRequestDTO feedbackRequestDTO = FeedbackUpdateRequestDTO.builder()
                .text(newText)
                .rate(newRate)
                .build();
        //when
        feedbackService.updateFeedback(feedbackRequestDTO, testFeedback);
        //then
        ArgumentCaptor<Feedback> feedbackArgumentCaptor = ArgumentCaptor.forClass(Feedback.class);
        Mockito.verify(feedbackRepository).save(feedbackArgumentCaptor.capture());
        Feedback capturedValue = feedbackArgumentCaptor.getValue();
        assertThat(capturedValue.getText()).isEqualTo(newText);
        assertThat(capturedValue.getRate()).isEqualTo(newRate);
        assertThat(capturedValue.getTime()).isNotEqualTo(oldFeedbackTime);
    }

    @Test
    void deleteFeedback() {
        //when
        feedbackService.deleteFeedback(testFeedback.getId());
        //then
        Mockito.verify(feedbackRepository).deleteById(testFeedback.getId());
    }
}