package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.UserRepository;
import com.serhiihurin.shop.online_shop.dto.PasswordUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.entity.Product;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import com.serhiihurin.shop.online_shop.dto.RegisterRequestDTO;
import com.serhiihurin.shop.online_shop.services.interfaces.UserService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Timed("getting_user")
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Could not find user"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email)
                .orElseThrow(() -> new ApiRequestException("Could not find user"));
    }

    @Override
    public User createUser(RegisterRequestDTO registerRequestDTO) {
        User user = User.builder()
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .cash(registerRequestDTO.getCash())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(registerRequestDTO.getRole())
                .build();
        return userRepository.save(user);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User updateUser(User currentAuthenticatedUser, UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getFirstName() != null) {
            currentAuthenticatedUser.setFirstName(userRequestDTO.getFirstName());
        }
        if (userRequestDTO.getLastName() != null) {
            currentAuthenticatedUser.setLastName(userRequestDTO.getLastName());
        }
        if (userRequestDTO.getCash() != null) {
            currentAuthenticatedUser.setCash(userRequestDTO.getCash());
        }
        if (userRequestDTO.getPassword() != null) {
            currentAuthenticatedUser.setPassword(userRequestDTO.getPassword());
        }

        return userRepository.save(currentAuthenticatedUser);
    }

    @Override
    public void subscribeForNotificationAboutProductAvailability(Product product, User currentAuthenticatedUser) {
        List<Product> productAvailabilityList = currentAuthenticatedUser.getProductAvailabilityList();

        if (productAvailabilityList.stream().anyMatch(p -> p.getId().equals(product.getId()))) {
            return;
        }

        productAvailabilityList.add(product);
        currentAuthenticatedUser.setProductAvailabilityList(productAvailabilityList);
        userRepository.save(currentAuthenticatedUser);
    }

    @Override
    public void unsubscribeFromNotificationAboutProductAvailability(Product product, User user) {
        user.getProductAvailabilityList().remove(product);
        userRepository.save(user);
    }


    @Override
    public User updateUsername(User currenAuthenticatedUser, String email) {
        if (!currenAuthenticatedUser.getEmail().equals(email)) {
            currenAuthenticatedUser.setEmail(email);
        }
        return userRepository.save(currenAuthenticatedUser);
    }

    @Override
    public void updatePassword(User currentAuthenticatedUser, PasswordUpdateRequestDTO passwordUpdateRequestDTO) {
        if (passwordUpdateRequestDTO.getPassword() == null) {
            throw new ApiRequestException("New password cannot be empty");
        }
        if (currentAuthenticatedUser.getPassword().equals(passwordUpdateRequestDTO.getPassword())) {
            throw new ApiRequestException("New password must differ from the old one");
        }
        currentAuthenticatedUser.setPassword(passwordEncoder.encode(passwordUpdateRequestDTO.getPassword()));
        userRepository.save(currentAuthenticatedUser);
        log.info("Changed password of account: {}", currentAuthenticatedUser.getEmail());
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
