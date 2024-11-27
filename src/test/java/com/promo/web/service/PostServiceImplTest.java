package com.promo.web.service;

import com.promo.web.dto.RestaurantPostDto;
import com.promo.web.dto.TechnologyPostDto;
import com.promo.web.dto.UserDto;
import com.promo.web.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class PostServiceImplTest {

    @Autowired
    private TechnologyPostService technologyPostService;

    @Autowired
    private RestaurantPostService restaurantPostService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Test
    void createPostsAndDivideByCategory() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);
        User createdUser = userService.getUserByEmail(user1.getEmail());

        MockMultipartFile logoFile = new MockMultipartFile("file1", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile1 = new MockMultipartFile("file2", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile2 = new MockMultipartFile("file3", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );

        MockMultipartFile[] images = new MockMultipartFile[]{multipartFile1};
        MockMultipartFile[] videos = new MockMultipartFile[]{multipartFile2};

        TechnologyPostDto technologyPostDto = TechnologyPostDto.builder()
                .title("TechnologyTitle")
                .subTitle("Sub")
                .description("Description")
                .category(Category.Technology)
                .subCategory(SubCategory.AI_APPLICATION)
                .visibility("Private")
                .isOwnWork(true)
                .isPortrait(true)
                .playStoreUrl("https://play.google.com/store/apps/details?id=com.world.lotto")
                .appStoreUrl("https://apps.apple.com/kr/app/world-lotto/id6505033228?l=en-GB")
                .webUrl("https://play.google.com/store/apps/details?id=com.world.lotto")
                .tags(List.of("Tag1", "Tag2"))
                .build();

        technologyPostService.createPost(createdUser.getId(), technologyPostDto, logoFile, images, videos);

        RestaurantPostDto restaurantPostDto = RestaurantPostDto.builder()
                .title("RestaurantTitle")
                .subTitle("Sub")
                .description("Description")
                .category(Category.Technology)
                .subCategory(SubCategory.AI_APPLICATION)
                .visibility("Private")
                .isOwnWork(true)
                .isPortrait(true)
                .webUrl("https://play.google.com")
                .location("location")
                .tags(List.of("Tag1", "Tag2"))
                .build();

        restaurantPostService.createPost(createdUser.getId(), restaurantPostDto, logoFile, images, videos);

        List<TechnologyPost> wholeTechnologyPosts = technologyPostService.getWholePosts();
        List<RestaurantPost> wholeRestaurantPosts = restaurantPostService.getWholePosts();
        List<Post> wholePosts = postService.getWholePosts();

        assertEquals(wholeTechnologyPosts.isEmpty(), false);
        assertEquals(wholeTechnologyPosts.size(), 1);

        assertEquals(wholeRestaurantPosts.isEmpty(), false);
        assertEquals(wholeRestaurantPosts.size(), 1);

        assertEquals(wholePosts.isEmpty(), false);
        assertEquals(wholePosts.size(), 2);

        TechnologyPost technologyPost = wholeTechnologyPosts.get(0);
        RestaurantPost restaurantPost = wholeRestaurantPosts.get(0);

        assertEquals(technologyPost.getId(), wholePosts.get(0).getId());
        assertEquals(technologyPost.getTitle(), wholePosts.get(0).getTitle());

        assertEquals(restaurantPost.getId(), wholePosts.get(1).getId());
        assertEquals(restaurantPost.getTitle(), wholePosts.get(1).getTitle());
    }
}