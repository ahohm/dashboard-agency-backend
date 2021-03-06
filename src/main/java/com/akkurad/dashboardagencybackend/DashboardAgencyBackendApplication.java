package com.akkurad.dashboardagencybackend;

import com.akkurad.dashboardagencybackend.dao.IApplicationDao;
import com.akkurad.dashboardagencybackend.dao.IRoleDao;
import com.akkurad.dashboardagencybackend.model.Application;
import com.akkurad.dashboardagencybackend.model.EApplicationType;
import com.akkurad.dashboardagencybackend.model.ERole;
import com.akkurad.dashboardagencybackend.model.Role;
import com.akkurad.dashboardagencybackend.service.impl.EnrollAdminServiceImpl;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
//@EnableConfigServer
@AllArgsConstructor
public class DashboardAgencyBackendApplication {

    private EnrollAdminServiceImpl enrollAdminServiceImpl;
    private IRoleDao iRoleDao;
    private IApplicationDao iApplicationDao;

    public static void main(String[] args) {
        SpringApplication.run(DashboardAgencyBackendApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public void insertRoles(){
        List<Role> roles = new ArrayList<>();
        if (!iRoleDao.findByName(ERole.ROLE_ADMIN).isPresent()){
            roles.add(new Role(null , ERole.ROLE_ADMIN));
        }
        if (!iRoleDao.findByName(ERole.ROLE_MODERATOR).isPresent()){
            roles.add(new Role(null , ERole.ROLE_MODERATOR));
        }
        if (!iRoleDao.findByName(ERole.ROLE_USER).isPresent()){
            roles.add(new Role(null , ERole.ROLE_USER));
        }

        iRoleDao.saveAll(roles);
    }

    @Bean
    public void insertApplications(){
        List<Application> applications = new ArrayList<>();
        if (!iApplicationDao.findByName(EApplicationType.KEYLESS).isPresent()){
            applications.add(new Application(null , EApplicationType.KEYLESS));
        }
        if (!iApplicationDao.findByName(EApplicationType.OWNER).isPresent()){
            applications.add(new Application(null , EApplicationType.OWNER));
        }
        if (!iApplicationDao.findByName(EApplicationType.CUSTOMER).isPresent()){
            applications.add(new Application(null , EApplicationType.CUSTOMER));
        }
        iApplicationDao.saveAll(applications);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("172.17.0.1");
        jedisConnectionFactory.setPort(6379);
        return jedisConnectionFactory;
    }

    @Bean
    RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public void enrollAdmin() {
        try {
            enrollAdminServiceImpl.runOrg1();
            enrollAdminServiceImpl.runOrg2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
