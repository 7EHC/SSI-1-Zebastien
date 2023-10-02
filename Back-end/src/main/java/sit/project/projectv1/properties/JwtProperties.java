package sit.project.projectv1.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@EnableConfigurationProperties
@Getter
@Setter
public class JwtProperties {
    private String secretKey;
    private Integer tokenIntervalInHour;
    private Integer refreshTokenIntervalInHour;
}
