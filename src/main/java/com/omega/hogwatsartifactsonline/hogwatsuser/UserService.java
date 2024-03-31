
package com.omega.hogwatsartifactsonline.hogwatsuser;

import com.omega.hogwatsartifactsonline.Exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public List<HogwartsUser> findAll() {
        return this.userRepository.findAll();
    }

    public HogwartsUser findById(Integer userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

    public HogwartsUser save(HogwartsUser newHogwartsUser) {
        // We NEED to encode plain password before saving to the DB! TODO
        return this.userRepository.save(newHogwartsUser);
    }

    public HogwartsUser update(Integer userId, HogwartsUser update) {
        HogwartsUser oldHogwartsUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        oldHogwartsUser.setUsername(update.getUsername());
        oldHogwartsUser.setEnabled(update.isEnabled());
        oldHogwartsUser.setRoles(update.getRoles());
        return userRepository.save(oldHogwartsUser);
    }

    public void delete(Integer userId) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        this.userRepository.deleteById(userId);
    }
}
