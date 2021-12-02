package io.github.mybatis.pal.autoconfigure;

import io.github.mybatis.pal.ConsumeTimeInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Properties;

/**
 * @author cause
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
@EnableConfigurationProperties(MybatisPalProperties.class)
@Lazy(false)
public class MybatisPalAutoConfigure implements InitializingBean {

  @Autowired
  private List<SqlSessionFactory> sqlSessionFactoryList;

  @Autowired
  private MybatisPalProperties mybatisPalProperties;

  @Override
  public void afterPropertiesSet() {

    ConsumeTimeInterceptor interceptor = new ConsumeTimeInterceptor();
    Properties properties = new Properties();
    properties.setProperty("limitMilliSecond", mybatisPalProperties.getLimitMilliSecond());
    interceptor.setProperties(properties);
    for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
      org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
      if (!containsInterceptor(configuration, interceptor)) {
        configuration.addInterceptor(interceptor);
      }
    }
  }

  /**
   * 是否已经存在相同的拦截器
   *
   * @param configuration
   * @param interceptor
   * @return
   */
  private boolean containsInterceptor(org.apache.ibatis.session.Configuration configuration, Interceptor interceptor) {
    try {
      // getInterceptors since 3.2.2
      return configuration.getInterceptors().contains(interceptor);
    } catch (Exception e) {
      return false;
    }
  }

}
