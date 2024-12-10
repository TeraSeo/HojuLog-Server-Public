package com.hojunara.web.service;

import com.hojunara.web.dto.request.RestaurantPostDto;
import com.hojunara.web.dto.request.TechnologyPostDto;
import com.hojunara.web.dto.request.UserDto;
import com.hojunara.web.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

        MockMultipartFile[] images = new MockMultipartFile[]{multipartFile1};

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
                .youtubeUrl("https://youtube/videos")
                .tags(List.of("Tag1", "Tag2"))
                .build();

        technologyPostService.createPost(createdUser.getEmail(), technologyPostDto, logoFile, images);

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
                .youtubeUrl("https://youtube/videos")
                .location("location")
                .tags(List.of("Tag1", "Tag2"))
                .build();

        restaurantPostService.createPost(createdUser.getEmail(), restaurantPostDto, logoFile, images);

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

    @Test
    void getPostsLatestPagination() {
        Page<Post> posts = postService.getPostsByPageNCondition(PageRequest.of(0, 2), "Latest");
        assertEquals(posts.getNumberOfElements(), 2);
    }

}