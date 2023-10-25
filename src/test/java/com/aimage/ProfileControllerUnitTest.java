package com.aimage;


import com.aimage.web.ProfileController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileControllerUnitTest {

    @Test
    @DisplayName("realProfile이 조회된다.")
    void findRealProfile1() {
        // given
        String expectedProfile = "real";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");
        env.addActiveProfile(expectedProfile);

        ProfileController profileController = new ProfileController(env);

        // when
        String profile = profileController.profile();

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    @DisplayName("realProfile이 없으면 마지막이 조회된다.")
    void findRealProfile2() {
        // given
        String expectedProfile = "oauth";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile("real-db");
        env.addActiveProfile(expectedProfile);

        ProfileController profileController = new ProfileController(env);

        // when
        String profile = profileController.profile();

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    @DisplayName("activeProfile이 없으면 default가 조회된다.")
    void findRealProfile3() {
        // given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();
        ProfileController profileController = new ProfileController(env);

        // when
        String profile = profileController.profile();

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}