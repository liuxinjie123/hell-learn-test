package com.springboot.hello.property;

import com.springboot.hello.dto.MyPoJo;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

@ConstructorBinding
@ConfigurationProperties(prefix = "acme")
@PropertySource(value = "application.yml")
@Component
@Data
@NoArgsConstructor
@Validated
public class AcmeProperties implements Serializable {
    /**
     * enabled
     */
    private boolean enabled;

    /**
     * remote address
     */
    @NotNull
    private InetAddress remoteAddress;

    /**
     * map
     */
    private Map<String, String> map;

    /**
     * list
     */
    private List<MyPoJo> myPoJoList;

    /**
     * security
     */
    private Security security;

    public AcmeProperties(boolean enabled, InetAddress remoteAddress, Security security) {
        this.enabled = enabled;
        this.remoteAddress = remoteAddress;
        this.security = security;
    }


    @Data
    @NoArgsConstructor
    public static class Security {
        /**
         * username
         */
        private String username;

        /**
         * password
         */
        private String password;

        private List<String> roles;

        public Security(String username, String password, @DefaultValue("USER") List<String> roles) {
            this.username = username;
            this.password = password;
            this.roles = roles;
        }
    }
}
