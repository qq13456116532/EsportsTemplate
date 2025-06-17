package com.esports.esports.config;

import com.esports.esports.model.*;
import com.esports.esports.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.init-data", havingValue = "true") //当 app.init-data=false（默认值）时，整个 DataInitializer 都不会注册
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BannerRepository bannerRepository;
    private final UserRepository userRepository;
    private final PlayerOrderRepository playerOrderRepository;
    private final CommentRepository commentRepository;


    @Override
    public void run(String... args) throws Exception {

        System.out.println("执行数据初始化...");
        // Clear existing data for a clean slate on restart
        playerOrderRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        bannerRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
        
        // Create Categories
        Category lol = new Category();
        lol.setName("英雄联盟");
        categoryRepository.save(lol);

        Category wzry = new Category();
        wzry.setName("王者荣耀");
        categoryRepository.save(wzry);

        Category jcc = new Category();
        jcc.setName("金铲铲");
        categoryRepository.save(jcc);

        // Create Products
        Product p1 = new Product();
        p1.setCategory(wzry);
        p1.setName("王者荣耀-荣耀王者陪练");
        p1.setPrice(new BigDecimal("50.00"));
        p1.setSales(120);
        p1.setViews(1500);
        p1.setImageUrl("/assets/images/mock/banner1.png");
        p1.setDescription("这里是详细的服务描述，介绍服务内容、时长、以及注意事项等。");

        Product p2 = new Product();
        p2.setCategory(lol);
        p2.setName("英雄联盟-钻石到大师");
        p2.setPrice(new BigDecimal("300.00"));
        p2.setSales(45);
        p2.setViews(2300);
        p2.setImageUrl("/assets/images/mock/banner2.png");
        p2.setDescription("专业打手，快速上分，安全可靠。");
        
        Product p3 = new Product();
        p3.setCategory(jcc);
        p3.setName("金铲铲-大师教学局");
        p3.setPrice(new BigDecimal("30.00"));
        p3.setSales(300);
        p3.setViews(800);
        p3.setImageUrl("/assets/images/mock/banner3.png");
        p3.setDescription("顶级思路教学，助你轻松上大师。");

        productRepository.saveAll(List.of(p1, p2, p3));

        // Create Banners
        Banner b1 = new Banner();
        b1.setImageUrl("/assets/images/mock/banner1.png");
        Banner b2 = new Banner();
        b2.setImageUrl("/assets/images/mock/banner2.png");
        Banner b3 = new Banner();
        b3.setImageUrl("/assets/images/mock/banner3.png");
        bannerRepository.saveAll(List.of(b1, b2, b3));
        
        // Create a Mock User and Orders
        User mockUser = new User();
        mockUser.setNickName("电竞大神");
        mockUser.setAvatarUrl("/assets/images/icons/defaultUser.svg");
        mockUser.setOpenId("mock_openid_12345"); // Example openid
        userRepository.save(mockUser);
        
        PlayerOrder order1 = new PlayerOrder();
        order1.setUser(mockUser);
        order1.setProduct(p1);
        order1.setStatus(OrderStatus.ONGOING);

        PlayerOrder order2 = new PlayerOrder();
        order2.setUser(mockUser);
        order2.setProduct(p3);
        order2.setStatus(OrderStatus.PENDING_PAYMENT);

        playerOrderRepository.saveAll(List.of(order1, order2));

        Comment c1 = new Comment();
        c1.setProduct(p1);
        c1.setOrder(order1);
        c1.setUser(mockUser);
        c1.setContent("老板技术好、服务也好，五星好评！");
        c1.setRating(5);

        commentRepository.save(c1);

    }
}